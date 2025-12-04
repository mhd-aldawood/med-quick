package com.example.kotlintest.screens.tonometer

import androidx.compose.ui.graphics.Color
import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.core.workers.Worker
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.di.TonometerQualifier
import com.example.kotlintest.screens.home.models.DeviceCategory
import com.example.kotlintest.screens.tonometer.models.AgeGroup
import com.example.kotlintest.screens.tonometer.models.AgeGroupBtn
import com.example.kotlintest.screens.tonometer.models.PatientBodyPart
import com.example.kotlintest.screens.tonometer.models.PatientPosition
import com.example.kotlintest.screens.tonometer.models.PositionType
import com.example.kotlintest.screens.tonometer.models.SelectStatus
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import javax.inject.Inject

val items = listOf(
    "(血压计型号:CONTEC08A)",
    "(血压计型号:CONTEC08C)",
    "(血压计型号:CONTEC08E)",
    "(血压计型号:ABPM50)",
    "(血压计型号:ABPM70)",
    "(血压计型号:ABPM60)"
)
//TODO re-organize this state be grouping relevant classes together
data class TonometerState(
    val headerDataSection: HeaderDataSection = HeaderDataSection(
        title = "Tonometer", titleIcon = R.drawable.ic_bluetooth_on
    ),

    val shouldRequestBluetooth: Boolean = true,


    val leftLamp: List<PatientBodyPart> = mutableListOf<PatientBodyPart>(
        PatientBodyPart.LeftArm(), PatientBodyPart.LeftLeg()
    ),
    val rightLamp: List<PatientBodyPart> = mutableListOf<PatientBodyPart>(
        PatientBodyPart.RightArm(), PatientBodyPart.RightLeg()
    ),


    val ageGroup: AgeGroup = AgeGroup.None,
    val patientPositionList: List<PatientPosition> = mutableListOf<PatientPosition>(
        PatientPosition(
            type = PositionType.Sitting,
            selectStatus = SelectStatus.NotSelected,
            icon = R.drawable.ic_sitting_position_not_clicked
        ),
        PatientPosition(
            type = PositionType.Lying,
            selectStatus = SelectStatus.NotSelected,
            icon = R.drawable.ic_lying_position_not_clicked
        )
    ),
    val patientIcon: Int = R.drawable.ic_patient_shape,
    val ageGroupBtn: List<AgeGroupBtn> = mutableListOf<AgeGroupBtn>(
        AgeGroupBtn(
            ageGroup = AgeGroup.NewPorn,
            backgroundColor = Color.White,
            ageGroupTextColor = PrimaryMidLinkColor
        ), AgeGroupBtn(
            ageGroup = AgeGroup.Child,
            backgroundColor = Color.White,
            ageGroupTextColor = PrimaryMidLinkColor
        ), AgeGroupBtn(
            ageGroup = AgeGroup.Adult,
            backgroundColor = Color.White,
            ageGroupTextColor = PrimaryMidLinkColor
        )
    ),


    val systolicPressure: Double = 0.0,//Todo group them in class
    val pulseRate: Double = 0.0,
    val pressureValue: String = "",
    val pressureIcon: Int = R.mipmap.ic_s_d_blood_pressure,

    )

sealed class TonometerEvents {
    data class ShowMsg(val msg: String) : TonometerEvents()
}

sealed class TonometerAction {
    data class OnLambChange(val patientPosition: PatientBodyPart) : TonometerAction()
    data object BluetoothRequestFinished : TonometerAction()
    data class OnAgeGroupChange(val ageGroup: AgeGroup) : TonometerAction()
    data class OnSittingPosChange(
        val patientType: PositionType
    ) : TonometerAction()
    data class Bluetooth(val command: BluetoothCommand) : TonometerAction()

}


@HiltViewModel
class TonometerViewModel @Inject constructor(
    @TonometerQualifier private val worker: Worker,
    private val deviceManager: DeviceManager
) : BaseViewModel<TonometerState, TonometerEvents, TonometerAction>(initialState = TonometerState()) {
    init {
        deviceManager.setDeviceModels(DeviceCategory.ElectronicSphygmomanometer)
    }
    override fun handleAction(action: TonometerAction) {
        when (action) {
            TonometerAction.BluetoothRequestFinished -> mutableState.update {
                it.copy(
                    shouldRequestBluetooth = false
                )
            }

            is TonometerAction.OnLambChange -> onLambChange(action.patientPosition)
            is TonometerAction.OnAgeGroupChange -> onAgeGroupChange(action.ageGroup)
            is TonometerAction.OnSittingPosChange -> onPosChange(
                action.patientType
            )

            is TonometerAction.Bluetooth -> when (action.command) {
                BluetoothCommand.SearchAndCommunicate -> searchAndCommunicate()
                BluetoothCommand.StopBluetoothAndCommunication -> stopBluetoothAndCommunication()
            }
        }
    }

    private fun onPosChange(clicked: PositionType) {
        mutableState.update { state ->
            val updatedList = state.patientPositionList.map { position ->
                if (position.type == clicked) {
                    if (position.selectStatus == SelectStatus.Selected) {
                        position.copy(
                            selectStatus = SelectStatus.NotSelected,
                            icon = if (position.type == PositionType.Sitting) R.drawable.ic_sitting_position_not_clicked
                            else R.drawable.ic_lying_position_not_clicked
                        )
                    } else {
                        position.copy(
                            selectStatus = SelectStatus.Selected,
                            icon = if (position.type == PositionType.Sitting) R.drawable.ic_sitting_position_clicked
                            else R.drawable.ic_lying_position_clicked
                        )
                    }
                } else {
                    position.copy(
                        selectStatus = SelectStatus.NotSelected,
                        icon = if (position.type == PositionType.Sitting) R.drawable.ic_sitting_position_not_clicked
                        else R.drawable.ic_lying_position_not_clicked
                    )
                }
            }

            state.copy(patientPositionList = updatedList)
        }
    }

    private fun onLambChange(patientPosition: PatientBodyPart) {
        //TODO re-write this logic again
        mutableState.update { state ->

            // Update leftLamp
            val updatedLeftLamp = state.leftLamp.map { part ->
                val isSelected = part::class == patientPosition::class
                when (part) {
                    is PatientBodyPart.LeftArm -> part.copy(fontSize = if (isSelected) 20 else 14)
                    is PatientBodyPart.LeftLeg -> part.copy(fontSize = if (isSelected) 20 else 14)
                    // The following are not in leftLamp but included for completeness
                    is PatientBodyPart.RightArm -> part.copy(fontSize = if (isSelected) 20 else 14)
                    is PatientBodyPart.RightLeg -> part.copy(fontSize = if (isSelected) 20 else 14)
                }
            }

            // Update rightLamp
            val updatedRightLamp = state.rightLamp.map { part ->
                val isSelected = part::class == patientPosition::class
                when (part) {
                    is PatientBodyPart.RightArm -> part.copy(fontSize = if (isSelected) 20 else 14)
                    is PatientBodyPart.RightLeg -> part.copy(fontSize = if (isSelected) 20 else 14)
                    // The following are not in rightLamp but included for completeness
                    is PatientBodyPart.LeftArm -> part.copy(fontSize = if (isSelected) 20 else 14)
                    is PatientBodyPart.LeftLeg -> part.copy(fontSize = if (isSelected) 20 else 14)
                }
            }

            // Compute patientIcon
            val updatedIcon = when (patientPosition) {
                is PatientBodyPart.LeftArm -> R.drawable.ic_patient_left_arm
                is PatientBodyPart.LeftLeg -> R.drawable.ic_patient_left_leg
                is PatientBodyPart.RightArm -> R.drawable.ic_patient_right_arm
                is PatientBodyPart.RightLeg -> R.drawable.ic_patient_right_leg
            }

            state.copy(
                leftLamp = updatedLeftLamp,
                rightLamp = updatedRightLamp,
                patientIcon = updatedIcon
            )
        }

    }

    private fun onAgeGroupChange(ageGroup: AgeGroup) {
        mutableState.update { it ->
            val updatedAgeGroupBtn = it.ageGroupBtn.map { obj ->
                if (obj.ageGroup == ageGroup) {
                    obj.copy(backgroundColor = PrimaryMidLinkColor, ageGroupTextColor = Color.White)
                } else {
                    obj.copy(backgroundColor = Color.White, ageGroupTextColor = PrimaryMidLinkColor)
                }
            }
            it.copy(ageGroupBtn = updatedAgeGroupBtn, ageGroup = ageGroup)
        }
    }

    private fun stopBluetoothAndCommunication() {
        worker.stopWork()
    }

    private val jsonDecoder = Json { ignoreUnknownKeys = true } // Ignore unknown fields

    private val TAG = "TonometerViewModel"
    fun searchAndCommunicate() {
        if (mutableState.value.shouldRequestBluetooth) {
            if (deviceManager.bluetoothScanner.isBluetoothEnabled()) {
                worker.startWork { result ->
                    mutableState.update {
                        it.copy(
                            pressureValue = result.getString("pressureValue"),
                            systolicPressure = result.getDouble("systolicPressure"),
                            pulseRate = result.getDouble("pulseRate")
                        )
                    }

                }

            } else {
                //you should enable bluetooth msg
                sendEvent(TonometerEvents.ShowMsg("Please enable bluetooth"))
            }
        }
    }

}