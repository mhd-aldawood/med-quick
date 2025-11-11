package com.example.kotlintest.screens.stethoscope

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.audio.AudioProcessor
import com.example.kotlintest.core.bluetooth.BluetoothScanner
import com.example.kotlintest.core.devicesWorker.StethoScopeWorker
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.di.StethoScopeQualifier
import com.example.kotlintest.screens.home.DeviceCategory
import com.example.kotlintest.screens.stethoscope.model.AuscultationRecord
import com.example.kotlintest.screens.stethoscope.model.AuscultationSite
import com.example.kotlintest.screens.stethoscope.model.DeleteBtnStatus
import com.example.kotlintest.screens.stethoscope.model.PlayBtnStatus
import com.example.kotlintest.screens.stethoscope.model.RecordingStateIcon
import com.example.kotlintest.ui.theme.CeruleanBlue
import com.example.kotlintest.ui.theme.PaleCerulean
import com.example.kotlintest.util.AuscultationRecordLoader
import com.example.kotlintest.util.Logger
import com.example.kotlintest.util.calculateTimeDifference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StethoScopeViewModel @Inject constructor(
    private val bluetoothScanner: BluetoothScanner,
    val deviceManager: DeviceManager,
    val audioProcessor: AudioProcessor,
    @StethoScopeQualifier val stethoScopeWorker: StethoScopeWorker,
    val auscultationRecordLoader: AuscultationRecordLoader
) : BaseViewModel<StethoScopeState, StethoScopeEvents, StethoScopeAction>(
    initialState = StethoScopeState()
) {
    var start: Long = 0

    init {
        deviceManager.setDeviceModels(DeviceCategory.DigitalStethoscope)
        mutableState.update { it.copy(auscultationRecordList = auscultationRecordLoader.loadAuscultationRecords()) }
    }

    override fun handleAction(action: StethoScopeAction) {
        when (action) {
            StethoScopeAction.SearchAndConnectToStethoScope -> handleSearchAndConnectToStethoScope()
            StethoScopeAction.StopBluetoothAndCommunication -> {
                stethoScopeWorker.disconnect(release = true)
            }

            is StethoScopeAction.ChangeAuscultationSite -> handleChangeAuscultationSite(action.index)
            is StethoScopeAction.CheckClick -> sendEvent(StethoScopeEvents.NavigateBack)
            is StethoScopeAction.RecordClick -> handleRecordClick()
            is StethoScopeAction.DeleteRecord -> handleRecordDeleted(action.recordPath, action.icon)
            is StethoScopeAction.PlayRecord -> handlePlayRecord(action.recordPath, action.icon)
        }
    }

    private fun handlePlayRecord(recordPath: String?, icon: PlayBtnStatus) {
        // Ensure recordPath is valid
        if (recordPath.isNullOrEmpty()) {
            StethoScopeEvents.ShowMsg("No Record Found")
            return
        }

        // Search for the record in the auscultationRecordList based on the file path
        val record = mutableState.value.auscultationRecordList.find { it.file == recordPath }

        // If the record is not found, show message
        if (record == null) {
            StethoScopeEvents.ShowMsg("Record not found")
            return
        }

        // Handle play/pause logic based on the icon
        viewModelScope.launch(Dispatchers.Default) {
            if (icon == PlayBtnStatus.Play) {
                startPlayingRecord(recordPath, record)
            } else if (icon == PlayBtnStatus.Pause) {
                stopPlayingRecord(record)
            }
        }
    }


    fun changeIconThroughId(id: String, icon: PlayBtnStatus): Unit =
        mutableState.update { currentState ->
            // Create a new list with the updated record
            val updatedList = currentState.auscultationRecordList.map { r ->
                if (r.id == id) {
                    r.copy(playIcon = icon)
                } else {
                    r
                }
            }.toMutableList()
            // Return the updated state
            currentState.copy(auscultationRecordList = updatedList)
        }

    private fun handleRecordDeleted(filePath: String?, icon: DeleteBtnStatus) {
        if (filePath == null) return
        when (icon) {
            DeleteBtnStatus.DeleteIcon -> markPendingDeletion(filePath)   // flip to "Sure"
            DeleteBtnStatus.SureIcon -> confirmDeletion(filePath)       // delete file + remove item
            else -> Unit
        }
    }

    /** 1) Mark the row as "are you sure?" without touching disk */
    private fun markPendingDeletion(path: String) {
        viewModelScope.launch(Dispatchers.Default) {
            mutableState.update { state ->
                val idx = state.auscultationRecordList.indexOfFirst { it.file.toString() == path }
                if (idx < 0) return@update state

                val newList = state.auscultationRecordList.toMutableList().apply {
                    this[idx] = this[idx].copy(deleteBtn = DeleteBtnStatus.SureIcon)
                }
                state.copy(auscultationRecordList = newList)
            }
        }
    }

    /** Remove the item by file path and publish a new list ref */
    fun removeRecordFromState(path: String) {
        mutableState.update { state ->
            val newList = state.auscultationRecordList
                .filterNot { it.file.toString() == path }
                .toMutableList()
            state.copy(auscultationRecordList = newList)
        }
    }

    private fun getSelectedAuscultationSiteLocation(): String? =
        mutableState.value.listOfAuscultationSite.firstOrNull { it.textColor == Color.White }?.text

    private fun handleRecordClick() {
        // Step 1: Check if auscultation site location is selected
        val selectedLocation = getSelectedAuscultationSiteLocation()
        if (selectedLocation == null) {
            sendEvent(StethoScopeEvents.ShowMsg("Please select auscultation Location"))
            return
        }

        // Step 2: Check if device is connected
        if (!deviceManager.isConnected()) {
            sendEvent(StethoScopeEvents.ShowMsg("Please connect to device"))
            return
        }

        // Step 3: Handle recording or stopping recording based on the current state
        if (mutableState.value.recordState == RecordingStateIcon.RECORD.icon) {
            startRecording(selectedLocation)
        } else {
            stopRecording()
        }
    }

    // Start recording logic
    private fun startRecording(selectedLocation: Any) {
        viewModelScope.launch(Dispatchers.Default) {
            // Record start time
            start = System.currentTimeMillis()

            // Start the recording
            stethoScopeWorker.startRecording()

            // Update the state with the new record
            mutableState.update { state ->
                val newRecord = AuscultationRecord(
                    title = selectedLocation.toString(),
                    file = stethoScopeWorker.audioProcessor.recordedFilePath(),
                    duration = "", // Timer starts here
                    heartWave = state.heartValue,
                    isCashed = false // Handle duration later
                )

                val updatedList = state.auscultationRecordList.toMutableList().apply {
                    add(0, newRecord) // Add the new record at the beginning
                }

                state.copy(
                    recordState = RecordingStateIcon.STOP.icon,
                    auscultationRecordList = updatedList
                )
            }
        }
    }

    // Stop recording logic
    private fun stopRecording() {
        // Stop the recording
        stethoScopeWorker.stopRecording()

        // Update the state with the finished record
        mutableState.update { state ->
            // Calculate duration and update the first record in the list
            val updatedFirstRecord = state.auscultationRecordList.first().copy(
                isCashed = true,
                duration = start.calculateTimeDifference(System.currentTimeMillis()) // Timer ends here
            )

            val updatedList = state.auscultationRecordList.toMutableList().apply {
                this[0] = updatedFirstRecord // Update the first record
            }

            state.copy(
                recordState = RecordingStateIcon.RECORD.icon,
                auscultationRecordList = updatedList
            )
        }
    }

    private fun handleChangeAuscultationSite(index: Int) {
        mutableState.update { s ->
            val list = s.listOfAuscultationSite.toMutableList()

            // reset old selection
            if (s.selectedIndex in list.indices) {
                val old = list[s.selectedIndex]
                list[s.selectedIndex] = old.copy(
                    textColor = PaleCerulean,
                    backGroundColor = Color.Transparent
                )
            }

            // apply new selection
            val current = list[index]
            list[index] = current.copy(
                textColor = Color.White,
                backGroundColor = CeruleanBlue
            )

            s.copy(listOfAuscultationSite = list, selectedIndex = index)
        }
    }

    private fun connectToDevice(mac: String) {
        stethoScopeWorker.connect(
            mac = mac,
        )
        viewModelScope.launch(Dispatchers.Default) {//Gathering the data from the device here for the waves
            stethoScopeWorker.spkFlow.collect { pkd ->
                delay(50)
                mutableState.update { it.copy(heartValue = pkd.toList()) }
            }
        }

    }

    fun stopPlayingRecord(record: AuscultationRecord) {
        // Stop the record and change icon to play
        audioProcessor.stopPlayingRecord()
        // Update the state using mutableState to reflect the play/pause change
        changeIconThroughId(
            record.id, PlayBtnStatus.Play
        )
    }

    fun startPlayingRecord(recordPath: String, record: AuscultationRecord) {
        // Play the record and change icon to pause
        audioProcessor.playRecord(recordPath) {
            audioProcessor.stopPlayingRecord()
            changeIconThroughId(record.id, PlayBtnStatus.Play)
        }
        // Update the state using mutableState to reflect the play/pause change
        changeIconThroughId(record.id, PlayBtnStatus.Pause)
    }

    /** 2) Confirm: delete the file (IO) then drop the row from state */
    fun confirmDeletion(path: String) {
        viewModelScope.launch {
            deleteFileOnIO(path)
            removeRecordFromState(path)
        }
    }

    /** Disk work lives on IO */
    private suspend fun deleteFileOnIO(path: String) = withContext(Dispatchers.IO) {
        runCatching {
            val f = File(path)
            if (f.exists()) f.delete()
        }
    }


    private val TAG = "StethoScopeViewModel"
    private fun handleSearchAndConnectToStethoScope() {//IGNORE the error here
        viewModelScope.launch(Dispatchers.IO) {
            if (mutableState.value.shouldRequestBluetooth) {
                if (deviceManager.bluetoothScanner.isBluetoothEnabled()) {
                    bluetoothScanner.startDiscovery(targetNames = deviceManager.getDeviceModels()) { devices ->
                        Logger.i(TAG, "Device Found: ${devices.name}  ${devices.address}")
                        connectToDevice(devices.address)
                    }
                }
            }
        }
    }
}

data class StethoScopeState(
    val headerDataSection: HeaderDataSection = HeaderDataSection(
        title = "Digital Stethoscope", titleIcon = R.drawable.ic_bluetooth_on
    ),
    val heartValue: List<Short> = mutableListOf<Short>(),
    val shouldRequestBluetooth: Boolean = true,
    val pickLocation: String = "Pick Auscultation Site",
    val listOfAuscultationSite: List<AuscultationSite> = listOf<AuscultationSite>(
        AuscultationSite(
            text = "Structure of cardiac auscultation aortic area ( body structure)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Structure of cardiac auscultation pulmonic area ( body structure)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Structure of cardiac auscultation tricuspid area ( body structure)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Structure of cardiac auscultation mitral area ( body structure)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Anterior upper lung field (2nd ICS, MCL)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Anterior lower lung field (4th ICS, MCL)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Posterior upper lung field (T3 paraspinal)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Posterior lower lung field (T7 paraspinal)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Right mid-axillary lung field (5th ICS)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Left mid-axillary lung field (5th ICS)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
    ),
    val recordState: Int = RecordingStateIcon.RECORD.icon,
    val checkIcon: Int = R.mipmap.ic_check,
    val auscultationRecordList: MutableList<AuscultationRecord> = mutableListOf<AuscultationRecord>(
//        AuscultationRecord(
//            title = "Structure of cardiac auscultation aortic area ( body structure)",
//            duration = "10:30",
//            isCashed = false,
//            heartWave = List<Short>(2048) { i ->
//                val freq = 0.05f
//                val amp = (sin(i * freq) * Short.MAX_VALUE * 0.5).toInt().toShort()
//                (amp * (0.5 + 0.5 * sin(i * 0.001))).toInt().toShort()
//            },
//        ),
//        AuscultationRecord(
//            title = "Posterior upper lung field (T3 paraspinal)",
//            duration = "10:30",
//            isCashed = true,
//            heartWave = List<Short>(2048) { i ->
//                val freq = 0.05f
//                val amp = (sin(i * freq) * Short.MAX_VALUE * 0.5).toInt().toShort()
//                (amp * (0.5 + 0.5 * sin(i * 0.001))).toInt().toShort()
//            }
//        )
    ),
    val selectedIndex: Int = -1,
)

sealed class StethoScopeEvents {
    data object NavigateBack : StethoScopeEvents()
    data class ShowMsg(val msg: String) : StethoScopeEvents()
}

sealed class StethoScopeAction {
    data object SearchAndConnectToStethoScope : StethoScopeAction()
    data object StopBluetoothAndCommunication : StethoScopeAction()
    data object RecordClick : StethoScopeAction()
    data object CheckClick : StethoScopeAction()
    data class ChangeAuscultationSite(val index: Int) : StethoScopeAction()
    data class PlayRecord(val recordPath: String?, val icon: PlayBtnStatus) : StethoScopeAction()
    data class DeleteRecord(val recordPath: String?, val icon: DeleteBtnStatus) :
        StethoScopeAction()

}
