package com.example.kotlintest.screens.tonometer

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.contec.bp.code.base.ContecDevice
import com.contec.bp.code.bean.ContecBluetoothType
import com.contec.bp.code.callback.BluetoothSearchCallback
import com.contec.bp.code.callback.CommunicateCallback
import com.contec.bp.code.connect.ContecSdk
import com.contec.bp.code.tools.Utils
import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.screens.tonometer.model.TonometerModel
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.util.BluetoothRepository
import com.example.kotlintest.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.String


val devices = listOf(
    "NIBP01", "NIBP03", "NIBP04", "NIBP07", "NIBP08", "NIBP09", "NIBP11"
)
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


    val systolicPressure: Float = 0f,//Todo group them in class
    val pulseRate: Float = 0f,
    val pressureValue: String = "",
    val pressureIcon: Int = R.mipmap.ic_s_d_blood_pressure,

    )

sealed class TonometerEvents {
    data class ShowMsg(val msg: String) : TonometerEvents()

}

sealed class TonometerAction {
    data class OnLambChange(val patientPosition: PatientBodyPart) : TonometerAction()
    data object CheckBluetooth : TonometerAction()
    data object StopBluetoothAndCommunication : TonometerAction()
    data object BluetoothRequestFinished : TonometerAction()
    data class OnAgeGroupChange(val ageGroup: AgeGroup) : TonometerAction()
    data class OnSittingPosChange(
        val patientType: PositionType
    ) : TonometerAction()
}


@HiltViewModel
class TonometerViewModel @Inject constructor(
    private val sdk: ContecSdk,
    @ApplicationContext private val context: Context,
    private val bluetoothRepository: BluetoothRepository
) : BaseViewModel<TonometerState, TonometerEvents, TonometerAction>(initialState = TonometerState()) {
    override fun handleAction(action: TonometerAction) {
        when (action) {
            TonometerAction.CheckBluetooth -> checkBluetooth()
            TonometerAction.StopBluetoothAndCommunication -> stopBluetoothAndCommunication()
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
        //region old logic for test ,maybe it will be changed it to normal classes
        //        mutableState.update { it ->
//            val updatedList = it.bodyLamp.map { it1 ->
//                if (it1::class == patientPosition::class)
//                    when (it1) {
//                        is PatientBodyPart.LeftArm -> {
//                            it.copy(patientIcon = R.drawable.ic_patient_left_arm)
//                            it1.copy(fontSize = 20)
//                        }
//
//                        is PatientBodyPart.LeftLeg -> {
//                            it.copy(patientIcon = R.drawable.ic_patient_left_leg)
//                            it1.copy(fontSize = 20)
//                        }
//
//                        is PatientBodyPart.RightArm -> {
//                            it.copy(patientIcon = R.drawable.ic_patient_rigth_arm)
//                            it1.copy(fontSize = 20)
//                        }
//
//                        is PatientBodyPart.RightLeg -> {
//                            it.copy(patientIcon = R.drawable.ic_patient_rigth_leg)
//                            it1.copy(fontSize = 20)
//                        }
//                    }
//                else
//                    when (it1) {
//                        is PatientBodyPart.LeftArm -> {
//                            it1.copy(fontSize = 14)
//                        }
//
//                        is PatientBodyPart.LeftLeg -> {
//                            it1.copy(fontSize = 14)
//                        }
//
//                        is PatientBodyPart.RightArm -> {
//                            it1.copy(fontSize = 14)
//                        }
//
//                        is PatientBodyPart.RightLeg -> {
//                            it1.copy(fontSize = 14)
//                        }
//                    }
//            }
//            // Split updated list back into left and right lamps
//            val updatedLeftLamp =
//                updatedList.filter { it is PatientBodyPart.LeftArm || it is PatientBodyPart.LeftLeg }
//            val updatedRightLamp =
//                updatedList.filter { it is PatientBodyPart.RightArm || it is PatientBodyPart.RightLeg }
//
//
//            // Compute new patientIcon
//            val updatedIcon = when (patientPosition) {
//                is PatientBodyPart.LeftArm -> R.drawable.ic_patient_left_arm
//                is PatientBodyPart.LeftLeg -> R.drawable.ic_patient_left_leg
//                is PatientBodyPart.RightArm -> R.drawable.ic_patient_rigth_arm
//                is PatientBodyPart.RightLeg -> R.drawable.ic_patient_rigth_leg
//            }
//
//
//            it.copy(
//                patientIcon = updatedIcon,
//                bodyLamp = updatedList,
//                leftLamp = updatedLeftLamp,
//                rightLamp = updatedRightLamp
//            )
//        }
        //endregion may be will change sealed class to normal class
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
                is PatientBodyPart.RightArm -> R.drawable.ic_patient_rigth_arm
                is PatientBodyPart.RightLeg -> R.drawable.ic_patient_rigth_leg
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
        if (sdk != null) {
            sdk.stopBluetoothSearch();
            sdk.stopCommunicate();
        }
    }

    private val jsonDecoder = Json { ignoreUnknownKeys = true } // Ignore unknown fields

    val communicateCallback = object : CommunicateCallback {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCommunicateSuccess(json: String?) {
            Logger.i(TAG, "onCommunicateSuccess" + json)

            try {
                var jsonArray = JSONObject(json).getJSONArray("BloodPressureData")

                for (i in 0..<jsonArray.length()) {
                    val jsonObject: JSONObject = jsonArray.optJSONObject(i)
                    val systolicPressure = jsonObject.optString("SystolicPressure")
                    val diastolicPressure = jsonObject.optString("DiastolicPressure")
                    val date = jsonObject.optString("Date")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            json?.let {
                val model: TonometerModel = jsonDecoder.decodeFromString(it)
                val latestScan = model.BloodPressureData.maxByOrNull {
                    LocalDateTime.parse(it.Date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                }
                Logger.i(TAG, "onCommunicateSuccess: " + latestScan?.SystolicPressure)
                Logger.i(TAG, "onCommunicateSuccess: " + latestScan?.PulseRate)
                if (latestScan?.SystolicPressure != null && latestScan?.PulseRate != null) mutableState.update {
                    it.copy(
                        pressureValue = "${latestScan?.SystolicPressure} / ${latestScan?.PulseRate}",
                        systolicPressure = latestScan.SystolicPressure?.toFloat() ?: 0f,
                        pulseRate = latestScan.PulseRate?.toFloat() ?: 0f
                    )
                }
            }

        }

        override fun onCommunicateFailed(p0: Int) {
            Logger.e(TAG, "onCommunicateFailed" + p0.toString())

        }

        override fun onCommunicateProgress(p0: Int) {
            Logger.e(TAG, "onCommunicateProgress" + p0.toString())
        }

    }
    private val TAG = "TonometerViewModel"
    fun checkBluetooth() {
        if (mutableState.value.shouldRequestBluetooth) {
            if (bluetoothRepository.isBluetoothEnabled()) {
                sdk.init(ContecBluetoothType.TYPE_FF, false)
                sdk.startBluetoothSearch(
                    object : BluetoothSearchCallback {
                        override fun onContecDeviceFound(contecDevice: ContecDevice?) {

                            if (contecDevice?.name == null) {
                                return
                            }

                            //打印设备名称
                            Logger.e(TAG, contecDevice.getName())


                            if (devices.any { contecDevice.name.startsWith(it) }) {
                                Logger.i(TAG, Utils.bytesToHexString(contecDevice.getRecord()))
                                sdk.startCommunicate(
                                    context, contecDevice, communicateCallback
                                )
                            }
                        }

                        override fun onSearchError(p0: Int) {
                        }

                        override fun onSearchComplete() {
                        }
                    }, 200000
                )
            } else {
                //you should enable bluetooth msg
                sendEvent(TonometerEvents.ShowMsg("Please enable bluetooth"))
            }
        }
    }

}