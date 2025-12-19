package com.example.kotlintest.features_appointment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlintest.features_appointment.data.model.CreateAppointmentResponse
import com.example.kotlintest.features_appointment.data.model.DoctorSlotsItemResponse
import com.example.kotlintest.features_appointment.data.model.DoctorsAvailabilityItemResponse
import com.example.kotlintest.features_appointment.data.model.ScheduleItemResponse
import com.example.kotlintest.features_appointment.data.model.SlotItemResponse
import com.example.kotlintest.features_appointment.data.model.SpecialtiesResponse
import com.example.kotlintest.features_appointment.domain.model.ComplaintInfoInBookDto
import com.example.kotlintest.features_appointment.domain.model.CreateAppointmentReq
import com.example.kotlintest.features_appointment.domain.model.DoctorsAvailabilityReq
import com.example.kotlintest.features_appointment.domain.model.PatientInfoInBookDto
import com.example.kotlintest.features_appointment.domain.usecase.CreateAppointmentUseCase
import com.example.kotlintest.features_appointment.domain.usecase.GetDoctorsAvailabilityUseCase
import com.example.kotlintest.features_appointment.domain.usecase.GetSpecialtiesUseCase
import com.example.kotlintest.features_appointment.presentation.events.AppointmentCreateScreenEvent
import com.example.kotlintest.features_appointment.presentation.events.AppointmentCreateUiEvent
import com.example.kotlintest.features_appointment.presentation.states.AppointmentCreateScreenState
import com.example.kotlintest.util.CustomDateTimeFormatter.combineDateAndTimeToUtc
import com.example.kotlintest.util.CustomDateTimeFormatter.combineUtcDateAndTime
import com.example.kotlintest.util.CustomDateTimeFormatter.getDateOnly
import com.example.kotlintest.util.data.local.SharedPreferanceRepository
import com.example.kotlintest.util.data.model.DateOfBirth
import com.example.kotlintest.util.data.model.MainResources
import com.example.kotlintest.util.data.model.RequestStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val sharedPreferanceRepository: SharedPreferanceRepository,
    private val getSpecialtiesUseCase: GetSpecialtiesUseCase,
    private val getDoctorsAvailabilityUseCase: GetDoctorsAvailabilityUseCase,
    private val createAppointmentUseCase: CreateAppointmentUseCase

):ViewModel() {

    private val _appointmentCreateUiEvent = MutableSharedFlow<AppointmentCreateUiEvent>()
    val appointmentCreateUiEvent = _appointmentCreateUiEvent.asSharedFlow()
    private val _createAppointmentScreenScreenState = MutableStateFlow(AppointmentCreateScreenState())
    val createAppointmentScreenScreenState: StateFlow<AppointmentCreateScreenState> = _createAppointmentScreenScreenState.asStateFlow()

    fun onEvent(event: AppointmentCreateScreenEvent){
        when(event){
            /////////////////////  PersonalInformationContent  /////////////////////
            is AppointmentCreateScreenEvent.OnPatientNameChangedEvent->{
                _createAppointmentScreenScreenState.update { it.copy(patientName = event.patientName)}
            }
            is AppointmentCreateScreenEvent.OnPatientDateOfBirthChangedEvent->{
                _createAppointmentScreenScreenState.update { it.copy(patientDateOfBirth = event.patientDateOfBirth)}
            }
            is AppointmentCreateScreenEvent.OnPatientGenderChangedEvent->{
                _createAppointmentScreenScreenState.update { it.copy(patientGender = event.patientGender)}
            }

            /////////////////////  AppointmentCreateComplaintContent   /////////////////////
            is AppointmentCreateScreenEvent.OnPatientComplaintChangedEvent->{
                _createAppointmentScreenScreenState.update { it.copy(patientComplaint = event.patientComplaint)}
            }
            is AppointmentCreateScreenEvent.OnPatientMedicalHistoryChangedEvent->{
                _createAppointmentScreenScreenState.update { it.copy(patientMedicalHistory = event.patientMedicalHistory)}
            }
            is AppointmentCreateScreenEvent.OnPatientNotesChangedEvent->{
                _createAppointmentScreenScreenState.update { it.copy(patientNotes = event.patientNotes)}
            }

            /////////////////////  AppointmentCreateDetailsContent  /////////////////////
            is AppointmentCreateScreenEvent.OnSelectedSpecialtyEvent->{
                _createAppointmentScreenScreenState.update { it.copy(
                    specialtySelectedItem = event.specialty,
                    doctorsAvailabilityList =  RequestStates(data = DoctorsAvailabilityItemResponse()),
                    scheduleSelectedItem = ScheduleItemResponse(),
                    doctorSelectedItem = DoctorSlotsItemResponse(),
                    slotSelectedItem = SlotItemResponse()
                ) }
                getDoctorsAvailabilityList()
            }
            is AppointmentCreateScreenEvent.OnSelectedScheduleEvent->{
                _createAppointmentScreenScreenState.update { it.copy(
                    scheduleSelectedItem = event.schedule,
                    doctorSelectedItem = DoctorSlotsItemResponse(),
                    slotSelectedItem = SlotItemResponse()
                ) }
            }
            is AppointmentCreateScreenEvent.OnSelectedDoctorEvent->{
                _createAppointmentScreenScreenState.update { it.copy(
                    doctorSelectedItem = event.doctor,
                    slotSelectedItem = SlotItemResponse()

                ) }
            }
            is AppointmentCreateScreenEvent.OnSelectedSlotEvent->{
                _createAppointmentScreenScreenState.update { it.copy(
                    slotSelectedItem = event.slot
                ) }
            }
            ////////////  CreateAppointment  /////////////////////
            is AppointmentCreateScreenEvent.OnCreateAppointmentEvent->{
                viewModelScope.launch {
                    if (validateCreateAppointment()) {
                        createAppointment()
                    }
                }
            }

            else->{}
        }
    }
    init {
        getSpecialtiesList()
    }

    fun getSpecialtiesList(){
        getSpecialtiesUseCase(_createAppointmentScreenScreenState.value.specialtiesList.data.pageNumber?.plus(1)?:1).onEach {
            when(it){
                is MainResources.Sucess->{
                    _createAppointmentScreenScreenState.update { currentState ->
                        currentState.copy(
                            specialtiesList =RequestStates(isSuccess = true, data = it.data?: SpecialtiesResponse())
                        )
                    }
                }
                is MainResources.isLoading->{
                    _createAppointmentScreenScreenState.update { currentState ->
                        currentState.copy(
                            specialtiesList = RequestStates(isLoading = true , data = SpecialtiesResponse())
                        )
                    }
                }
                is MainResources.isError->{
                    _createAppointmentScreenScreenState.update { currentState ->
                        currentState.copy(
                            specialtiesList =RequestStates(isError = true , error = it.message?:"",data = SpecialtiesResponse())
                        )
                    }
                }
                else->{

                }
            }
        }.launchIn(viewModelScope)
    }


    fun getDoctorsAvailabilityList(){
        getDoctorsAvailabilityUseCase(
            DoctorsAvailabilityReq(
                specialityId = _createAppointmentScreenScreenState.value.specialtySelectedItem.id,
                forecastedDaysCount = 7
            )
        ).onEach {
            when(it){
                is MainResources.Sucess->{
                    _createAppointmentScreenScreenState.update { currentState ->
                        currentState.copy(
                            doctorsAvailabilityList =RequestStates(isSuccess = true, data = it.data?: DoctorsAvailabilityItemResponse())
                        )
                    }
                }
                is MainResources.isLoading->{
                    _createAppointmentScreenScreenState.update { currentState ->
                        currentState.copy(
                            doctorsAvailabilityList = RequestStates(isLoading = true , data = DoctorsAvailabilityItemResponse())
                        )
                    }
                }
                is MainResources.isError->{
                    _createAppointmentScreenScreenState.update { currentState ->
                        currentState.copy(
                            doctorsAvailabilityList =RequestStates(isError = true , error = it.message?:"",data = DoctorsAvailabilityItemResponse())
                        )
                    }
                }
                else->{

                }
            }
        }.launchIn(viewModelScope)
    }


    fun createAppointment(){
        createAppointmentUseCase(
            CreateAppointmentReq(
                patient = PatientInfoInBookDto(
                    name = _createAppointmentScreenScreenState.value.patientName,
                    dateOfBirth = getDateOnly(_createAppointmentScreenScreenState.value.patientDateOfBirth),
                    gender =_createAppointmentScreenScreenState.value.patientGender
                ),
                appointmentDetails = ComplaintInfoInBookDto(
                    patientComplaint = _createAppointmentScreenScreenState.value.patientComplaint,
                    patientMedicalHistory = _createAppointmentScreenScreenState.value.patientMedicalHistory,
                    notesForDoctor = _createAppointmentScreenScreenState.value.patientNotes
                ),
                doctorId = _createAppointmentScreenScreenState.value.doctorSelectedItem.doctorId,
                scheduleDateAndTime = combineUtcDateAndTime(
                    utcDateTime = _createAppointmentScreenScreenState.value.scheduleSelectedItem.scheduleDate,
                    utcTime = _createAppointmentScreenScreenState.value.slotSelectedItem.startTime
                )
            )
        ).onEach {
            when(it){
                is MainResources.Sucess->{
                    _createAppointmentScreenScreenState.update { currentState ->
                        currentState.copy(
                            createAppointmentRequest =RequestStates(isSuccess = true, data = it.data?: CreateAppointmentResponse())
                        )
                    }

                    //  SUCCESS SNACKBAR
                    _appointmentCreateUiEvent.emit(
                        AppointmentCreateUiEvent.ShowSnackBarAndPop(
                            message = "Appointment created successfully"
                        )
                    )
                }
                is MainResources.isLoading->{
                    _createAppointmentScreenScreenState.update { currentState ->
                        currentState.copy(
                            createAppointmentRequest = RequestStates(isLoading = true , data = CreateAppointmentResponse())
                        )
                    }
                }
                is MainResources.isError->{
                    _createAppointmentScreenScreenState.update { currentState ->
                        currentState.copy(
                            createAppointmentRequest =RequestStates(isError = true , error = it.message?:"",data = CreateAppointmentResponse())
                        )
                    }
                    // ERROR SNACKBAR
                    _appointmentCreateUiEvent.emit(
                        AppointmentCreateUiEvent.ShowSnackBar(
                            it.message ?: "Failed to create appointment"
                        )
                    )
                }
                else->{

                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun validateCreateAppointment(): Boolean {
        val state = _createAppointmentScreenScreenState.value

        return when {
            state.patientName.isBlank() -> {
                _appointmentCreateUiEvent.emit(
                    AppointmentCreateUiEvent.ShowSnackBar("Patient name is required")
                )
                false
            }

            state.patientDateOfBirth == DateOfBirth() -> {
                _appointmentCreateUiEvent.emit(
                    AppointmentCreateUiEvent.ShowSnackBar("Please select patient Date Of Birth")
                )
                false
            }

            state.patientGender == 0 -> {
                _appointmentCreateUiEvent.emit(
                    AppointmentCreateUiEvent.ShowSnackBar("Please select patient gender")
                )
                false
            }

            state.doctorSelectedItem.doctorId == null -> {
                _appointmentCreateUiEvent.emit(
                    AppointmentCreateUiEvent.ShowSnackBar("Please select a doctor")
                )
                false
            }

            state.slotSelectedItem.startTime.isNullOrBlank() -> {
                _appointmentCreateUiEvent.emit(
                    AppointmentCreateUiEvent.ShowSnackBar("Please select a time slot")
                )
                false
            }

            else -> true
        }
    }
}