package com.example.kotlintest.features_appointment.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kotlintest.R
import com.example.kotlintest.features_appointment.presentation.components.CustomSwitch
import com.example.kotlintest.ui.theme.KotlinTestTheme
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.White
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.util.scalePxToDp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlintest.features_appointment.data.model.DoctorSlotsItemResponse
import com.example.kotlintest.features_appointment.data.model.ScheduleItemResponse
import com.example.kotlintest.features_appointment.data.model.SlotItemResponse
import com.example.kotlintest.features_appointment.data.model.SpecialtiesResponse
import com.example.kotlintest.features_appointment.data.model.SpecialtyItemResponse
import com.example.kotlintest.features_appointment.presentation.components.CustomThreeStateSwitch
import com.example.kotlintest.features_appointment.presentation.components.DateItemCard
import com.example.kotlintest.features_appointment.presentation.components.DateOfBirthInput
import com.example.kotlintest.features_appointment.presentation.components.DoctorItemCard
import com.example.kotlintest.features_appointment.presentation.components.FloatingLabelUnderlineCustomTextField
import com.example.kotlintest.features_appointment.presentation.components.PaymentMethodItemCard
import com.example.kotlintest.features_appointment.presentation.components.SlotItemCard
import com.example.kotlintest.features_appointment.presentation.components.SpecialtyItemCard
import com.example.kotlintest.features_appointment.presentation.components.SwitchState
import com.example.kotlintest.features_appointment.presentation.events.AppointmentCreateScreenEvent
import com.example.kotlintest.features_appointment.presentation.events.AppointmentCreateUiEvent
import com.example.kotlintest.features_appointment.presentation.states.AppointmentCreateScreenState
import com.example.kotlintest.features_appointment.presentation.viewmodel.AppointmentViewModel
import com.example.kotlintest.ui.theme.FrenchWine
import com.example.kotlintest.ui.theme.LavenderGray
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.ui.theme.Platinum
import com.example.kotlintest.ui.theme.DarkSilver
import com.example.kotlintest.ui.theme.YankeesBlue
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.util.data.model.DateOfBirth

@Composable
fun AppointmentCreateScreen(
    viewModel: AppointmentViewModel = hiltViewModel(),
    navController: NavController,
) {
    val appointmentCreateScreenState by viewModel.createAppointmentScreenScreenState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.appointmentCreateUiEvent.collect { event ->
            when (event) {
                is AppointmentCreateUiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message =  event.message,
                        duration = SnackbarDuration.Short
                    )
                }

                is AppointmentCreateUiEvent.ShowSnackBarAndPop -> {

                    // Show snackbar
                    snackBarHostState.showSnackbar(
                        message =  event.message,
                        duration = SnackbarDuration.Short
                    )

                    // Pop back stack AFTER snackbar
                    navController.popBackStack()
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.background(Lotion),
        snackbarHost = { SnackbarHost(snackBarHostState){ snackbarData ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkSilver
                ),
                modifier = Modifier
                    .padding(vertical = 35.dp, horizontal = 100.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = snackbarData.visuals.message,
                    style = MaterialTheme.typography.rhDisplayMedium.copy(
                        color = Lotion,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(20.dp),
                    textAlign = TextAlign.Start
                )
            }
        } }
    ) {

        AppointmentCreateContainer(
            navController = navController,
            appointmentCreateScreenState = appointmentCreateScreenState,
            onPatientNameChanged = { patientName ->
                viewModel.onEvent(AppointmentCreateScreenEvent.OnPatientNameChangedEvent(patientName))
            },

            onPatientDateOfBirthChanged = { patientDateOfBirth ->
                viewModel.onEvent(
                    AppointmentCreateScreenEvent.OnPatientDateOfBirthChangedEvent(
                        patientDateOfBirth
                    )
                )
            },

            onPatientGenderChanged = { patientGender ->
                viewModel.onEvent(
                    AppointmentCreateScreenEvent.OnPatientGenderChangedEvent(
                        patientGender
                    )
                )
            },

            onPatientComplaintChanged = { patientComplaint ->
                viewModel.onEvent(
                    AppointmentCreateScreenEvent.OnPatientComplaintChangedEvent(
                        patientComplaint
                    )
                )
            },

            onPatientMedicalHistoryChanged = { patientMedicalHistory ->
                viewModel.onEvent(
                    AppointmentCreateScreenEvent.OnPatientMedicalHistoryChangedEvent(
                        patientMedicalHistory
                    )
                )
            },

            onPatientNotesChanged = { patientNotes ->
                viewModel.onEvent(
                    AppointmentCreateScreenEvent.OnPatientNotesChangedEvent(
                        patientNotes
                    )
                )
            },

            onSpecialtySelected = { specialty ->
                viewModel.onEvent(AppointmentCreateScreenEvent.OnSelectedSpecialtyEvent(specialty = specialty))
            },

            onSelectedSchedule = { schedule ->
                viewModel.onEvent(AppointmentCreateScreenEvent.OnSelectedScheduleEvent(schedule = schedule))
            },

            onSelectedDoctor = { doctor ->
                viewModel.onEvent(AppointmentCreateScreenEvent.OnSelectedDoctorEvent(doctor = doctor))
            },

            onSelectedSlot = { slot ->
                viewModel.onEvent(AppointmentCreateScreenEvent.OnSelectedSlotEvent(slot = slot))
            },

            onCreateAppointment = {
                viewModel.onEvent(AppointmentCreateScreenEvent.OnCreateAppointmentEvent)
            }

        )
    }
}











@Composable
fun AppointmentCreateContainer (
    navController: NavController,
    appointmentCreateScreenState : AppointmentCreateScreenState,
    onPatientNameChanged: (String) -> Unit,
    onPatientDateOfBirthChanged:(DateOfBirth) -> Unit,
    onPatientGenderChanged: (Int) -> Unit,
    onPatientComplaintChanged: (String) -> Unit,
    onPatientMedicalHistoryChanged: (String) -> Unit,
    onPatientNotesChanged: (String) -> Unit,
    onSpecialtySelected: (SpecialtyItemResponse) -> Unit,
    onSelectedSchedule: (ScheduleItemResponse) -> Unit,
    onSelectedDoctor:(DoctorSlotsItemResponse) -> Unit,
    onSelectedSlot:(SlotItemResponse) -> Unit,
    onCreateAppointment: () -> Unit = {},

){
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Lotion),) {
        Row(modifier = Modifier.fillMaxWidth()
            .height(scalePxToDp(85f)),
            verticalAlignment = Alignment.CenterVertically) {
            AppointmentCreateHeaderSection(
                navController = navController,
                onCreateAppointment = onCreateAppointment)
        }
        Box(modifier = Modifier.fillMaxSize()
            .background(Lotion)
            .padding(top=20.dp)
            .weight(1f)) {
            AppointmentCreateContentSection(
                appointmentCreateScreenState = appointmentCreateScreenState,
                onPatientNameChanged = onPatientNameChanged,
                onPatientDateOfBirthChanged = onPatientDateOfBirthChanged,
                onPatientGenderChanged = onPatientGenderChanged,
                onPatientComplaintChanged = onPatientComplaintChanged,
                onPatientMedicalHistoryChanged = onPatientMedicalHistoryChanged,
                onPatientNotesChanged = onPatientNotesChanged,
                onSpecialtySelected = onSpecialtySelected,
                onSelectedSchedule =onSelectedSchedule,
                onSelectedDoctor = onSelectedDoctor,
                onSelectedSlot = onSelectedSlot,
            )
        }

    }

    if (appointmentCreateScreenState.createAppointmentRequest.isLoading)
    {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(scalePxToDp(500f)),
                color = PrimaryMidLinkColor,
                strokeWidth = 18.dp,
                trackColor = Platinum
            )
        }
    }


}
@Composable
fun AppointmentCreateHeaderSection(
    navController: NavController,
    onCreateAppointment: () -> Unit = {},
)
{
    Text(
        text = "Create Appointment",
        style = MaterialTheme.typography.rhDisplayBlack.copy(
            color = PrimaryMidLinkColor,
            fontSize = 28.sp
        )
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))
        OutlinedButton(
            onClick =  {
                onCreateAppointment()
                //navController.safeNavigate(NavDestination.HOME_SCREEN)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryMidLinkColor ,
                contentColor = Lotion,
                disabledContainerColor = Color.White,
                disabledContentColor = PrimaryMidLinkColor
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, White),
            modifier = Modifier
                .height(scalePxToDp(70f))
                .width( scalePxToDp(225f))

        ) {
            Icon(
                painter = painterResource(R.drawable.ic_med_confirmed_appointment_icon),
                contentDescription = "Menu",
                tint = Color.Unspecified ,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(scalePxToDp(70f))
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Confirm",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = Lotion ,
                    fontSize = 14.sp
                )
            )
            Spacer(Modifier.width(10.dp))
        }
        Spacer(Modifier.width(25.dp))
        Icon(
            painter = painterResource(R.drawable.ic_cancel),
            contentDescription = "Menu",
            tint = Color.Unspecified ,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(scalePxToDp(80f))
                .clickable {
                    navController.popBackStack()
                }
        )
    }


}

@Composable
fun AppointmentCreateContentSection(
    appointmentCreateScreenState : AppointmentCreateScreenState,
    onPatientNameChanged: (String) -> Unit,
    onPatientDateOfBirthChanged:(DateOfBirth) -> Unit,
    onPatientGenderChanged: (Int) -> Unit,
    onPatientComplaintChanged: (String) -> Unit,
    onPatientMedicalHistoryChanged: (String) -> Unit,
    onPatientNotesChanged: (String) -> Unit,
    onSpecialtySelected: (SpecialtyItemResponse) -> Unit,
    onSelectedSchedule: (ScheduleItemResponse) -> Unit,
    onSelectedDoctor:(DoctorSlotsItemResponse) -> Unit,
    onSelectedSlot:(SlotItemResponse) -> Unit,
)
{
    Row(
        modifier = Modifier.fillMaxSize()
    ) {


        Column(
            modifier = Modifier.weight(1f)
                .fillMaxSize()
                .verticalScroll( rememberScrollState())
        ) {
            AppointmentCreateCard( modifier= Modifier.height(scalePxToDp(777f)), content = {AppointmentCreatePersonalInformationContent(
                appointmentCreateScreenState = appointmentCreateScreenState,
                onPatientNameChanged = onPatientNameChanged,
                onPatientDateOfBirthChanged = onPatientDateOfBirthChanged,
                onPatientGenderChanged = onPatientGenderChanged
            )})
            Spacer(modifier = Modifier.height(40.dp))
            AppointmentCreateCard(modifier= Modifier.wrapContentHeight(), content = {AppointmentCreateComplaintContent(
                appointmentCreateScreenState = appointmentCreateScreenState,
                onPatientComplaintChanged = onPatientComplaintChanged,
                onPatientMedicalHistoryChanged = onPatientMedicalHistoryChanged,
                onPatientNotesChanged = onPatientNotesChanged
            )})
            Spacer(modifier = Modifier.height(20.dp))
        }
        Spacer(Modifier.width(30.dp))
        Column(
            modifier = Modifier.weight(1f)
                .fillMaxSize()
                .verticalScroll( rememberScrollState())
        ) {
            AppointmentCreateCard( modifier= Modifier.height(scalePxToDp(777f)), content = {AppointmentCreateDetailsContent(
                appointmentCreateScreenState = appointmentCreateScreenState,
                onSpecialtySelected = onSpecialtySelected,
                onSelectedSchedule = onSelectedSchedule,
                onSelectedDoctor = onSelectedDoctor,
                onSelectedSlot = onSelectedSlot
            )})
            Spacer(modifier = Modifier.height(40.dp))
            AppointmentCreateCard(modifier= Modifier.height(scalePxToDp(700f)), content = {AppointmentCreatePaymentContent()})
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}

@Composable
fun AppointmentCreatePersonalInformationContent(
    appointmentCreateScreenState : AppointmentCreateScreenState,
    onPatientNameChanged: (String) -> Unit,
    onPatientDateOfBirthChanged:(DateOfBirth) -> Unit,
    onPatientGenderChanged: (Int) -> Unit,
)
{
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Personal Information",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_med_scan_appointmenticon),
                contentDescription = "Menu",
                tint = Color.Unspecified ,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(scalePxToDp(75f))
            )
            Spacer(modifier = Modifier.width(18.dp))
        }
        Spacer(Modifier.height(10.dp))

        FloatingLabelUnderlineCustomTextField(
            value = appointmentCreateScreenState.patientName,
            onValueChange = { onPatientNameChanged(it) },
            label = "Patient Name",
            placeHolder = "Patient Name",
            modifier = Modifier.fillMaxWidth()
                .padding(end = 18.dp)
        )
        Spacer(Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Date of Birth",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 12.sp
                ),
                modifier = Modifier.padding(end = 15.dp)
            )
            DateOfBirthInput(
                dateOfBirth = appointmentCreateScreenState.patientDateOfBirth ,
                onPatientDateOfBirthChanged = onPatientDateOfBirthChanged
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Gender",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 12.sp
                ),

            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Male",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.width(10.dp))
            CustomThreeStateSwitch(
                state = when (appointmentCreateScreenState.patientGender){
                    0 -> SwitchState.CENTER
                    1 ->  SwitchState.LEFT
                    2 -> SwitchState.RIGHT
                    else -> 0
                } as SwitchState,
                onStateChange = { when (it){
                    SwitchState.CENTER -> onPatientGenderChanged(0)
                    SwitchState.LEFT -> onPatientGenderChanged(1)
                    SwitchState.RIGHT -> onPatientGenderChanged(2)
                    else -> onPatientGenderChanged(0)
                } },
                trackWidth = scalePxToDp(110f),
                trackHeight =scalePxToDp(55f),
                thumbSize = scalePxToDp(45f),
                trackColor = White,
                thumbColorOn = PrimaryMidLinkColor,
                thumbColorOff = Periwinkle,
                borderWidth = 1.dp,
                borderColor = PrimaryMidLinkColor
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Female",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.width(18.dp))

        }
        Spacer(Modifier.weight(1f))
        Row() {
            Box(
                modifier = Modifier.height(scalePxToDp(294f))
                    .width(scalePxToDp(240f))
                    .background(Periwinkle,
                        shape = RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center,){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.ic_med_upload_icon),
                        contentDescription = null,
                        modifier = Modifier.height(scalePxToDp(86f))
                            .width(scalePxToDp(86f)),
                        contentScale = ContentScale.FillWidth

                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Choose an ID to upload",
                        style = MaterialTheme.typography.rhDisplayMedium.copy(
                            color = YankeesBlue,
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center
                        ),
                    )
                }

            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier.height(scalePxToDp(294f))
                    .width(scalePxToDp(240f))
                    .background(Periwinkle,
                        shape = RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center,){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.ic_med_capture_icon),
                        contentDescription = null,
                        modifier = Modifier.height(scalePxToDp(86f))
                            .width(scalePxToDp(86f)),
                        contentScale = ContentScale.FillWidth

                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Capture ID with camera",
                        style = MaterialTheme.typography.rhDisplayMedium.copy(
                            color = YankeesBlue,
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center
                        ),
                    )
                }

            }
        }
    }
}

@Composable
fun AppointmentCreateDetailsContent(
    appointmentCreateScreenState : AppointmentCreateScreenState,
    onSpecialtySelected: (SpecialtyItemResponse) -> Unit,
    onSelectedSchedule: (ScheduleItemResponse) -> Unit,
    onSelectedDoctor:(DoctorSlotsItemResponse) -> Unit,
    onSelectedSlot:(SlotItemResponse) -> Unit,
)
{
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tele-Consultation",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            var checkedBook by remember { mutableStateOf(false) }
            CustomSwitch(
                checked = checkedBook,
                onCheckedChange ={ checkedBook = it },
                trackWidth = scalePxToDp(100f),
                trackHeight =scalePxToDp(55f),
                thumbSize = scalePxToDp(45f),
                trackColor = White,
                thumbColorOn = PrimaryMidLinkColor,
                thumbColorOff = Periwinkle,
                borderWidth = 1.dp,
                borderColor = PrimaryMidLinkColor
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Book",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.width(18.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        //var specialtySelectedItem by remember { mutableStateOf< SpecialtyItem>(SpecialtyItem()) }  // -1 = none selected
        //var dates by remember { mutableStateOf<List<DateTimeItem>>(emptyList()) }
        //var dateSelectedItem by remember { mutableStateOf(-1) }  // -1 = none selected
        //var doctors by remember { mutableStateOf<List<DoctorItem>>(emptyList()) }
        //var doctorSelectedItem by remember { mutableStateOf<DoctorItem>(DoctorItem()) }  // -1 = none selected
        //var slots by remember { mutableStateOf<List<SlotItem>>(emptyList()) }
        //var slotSelectedItem by remember { mutableStateOf(-1) }  // -1 = none selected

        if (appointmentCreateScreenState.specialtiesList.isLoading )
        {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(scalePxToDp(114f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(scalePxToDp(114f)),
                    color = PrimaryMidLinkColor,
                    strokeWidth = 6.dp,
                    trackColor = Platinum
                )
            }
        }
        else if (appointmentCreateScreenState.specialtiesList.isError)
        {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(scalePxToDp(114f))
                    .padding(end = 18.dp)
                    .background(Lotion),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(20.dp))
                Text(
                    text = appointmentCreateScreenState.specialtiesList.error,
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        color = FrenchWine,
                        fontSize = 12.sp
                    )
                )
            }
        }
        else if (appointmentCreateScreenState.specialtiesList.isSuccess)
        {
            SpecialtyList(appointmentCreateScreenState.specialtiesList.data ,
                onSelectSpecialty = {
                    if (appointmentCreateScreenState.specialtySelectedItem != it)
                    {
                        onSpecialtySelected(it)
                    }
                } ,
                specialtySelectedItem = appointmentCreateScreenState.specialtySelectedItem)
        }


        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Select Appointment",
            style = MaterialTheme.typography.rhDisplayMedium.copy(
                color = PrimaryMidLinkColor,
                fontSize = 12.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        if(appointmentCreateScreenState.specialtySelectedItem.id != 0) {
            if (appointmentCreateScreenState.doctorsAvailabilityList.isLoading ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(scalePxToDp(80f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(scalePxToDp(80f)),
                        color = PrimaryMidLinkColor,
                        strokeWidth = 6.dp,
                        trackColor = Platinum
                    )
                }
            }
            else if (appointmentCreateScreenState.doctorsAvailabilityList.isError)
            {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(scalePxToDp(80f))
                        .padding(end = 18.dp)
                        .background(Lotion),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(20.dp))
                    Text(
                        text = appointmentCreateScreenState.doctorsAvailabilityList.error,
                        style = MaterialTheme.typography.rhDisplayBold.copy(
                            color = FrenchWine,
                            fontSize = 12.sp
                        )
                    )
                }
            }
            else if (appointmentCreateScreenState.doctorsAvailabilityList.isSuccess)
            {
                if (!appointmentCreateScreenState.doctorsAvailabilityList.data.schedules .isEmpty())
                {
                    DatesList(appointmentCreateScreenState.doctorsAvailabilityList.data.schedules,
                        onSelectDate = {
                            if (appointmentCreateScreenState.scheduleSelectedItem != it)
                            {
                                onSelectedSchedule(it)
                            }

                        } ,
                        dateSelectedItem = appointmentCreateScreenState.scheduleSelectedItem)
                }
                else
                {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .height(scalePxToDp(80f))
                            .padding(end = 18.dp)
                            .background(Lotion),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(20.dp))
                        Text(
                            text = "There is not any available dates, contact your operations manager.",
                            style = MaterialTheme.typography.rhDisplayBold.copy(
                                color = LavenderGray,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
        else
        {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(scalePxToDp(80f))
                    .padding(end = 18.dp)
                    .background(Lotion),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(20.dp))
                Text(
                    text = "Select Speciality to view available dates",
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        color = LavenderGray,
                        fontSize = 12.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        if (appointmentCreateScreenState.scheduleSelectedItem != ScheduleItemResponse())
        {


            if (!appointmentCreateScreenState.scheduleSelectedItem.doctors.isEmpty())
            {
                DoctorsList(appointmentCreateScreenState.scheduleSelectedItem.doctors,
                    onSelectDoctor = {
                        if(appointmentCreateScreenState.doctorSelectedItem != it)
                        {
                            onSelectedDoctor(it)
                        }

                    } ,
                    doctorSelectedItem = appointmentCreateScreenState.doctorSelectedItem)
            }
            else
            {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(scalePxToDp(85f))
                        .padding(end = 18.dp)
                        .background(Lotion),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(20.dp))
                    Text(
                        text = "There is not any available doctors, change the date or contact your operations manager.",
                        style = MaterialTheme.typography.rhDisplayBold.copy(
                            color = LavenderGray,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
        else
        {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(scalePxToDp(85f))
                    .padding(end = 18.dp)
                    .background(Lotion),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(20.dp))
                Text(
                    text = "Select Date to view available doctors",
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        color = LavenderGray,
                        fontSize = 12.sp
                    )
                )
            }
        }


        Spacer(modifier = Modifier.height(15.dp))

        if (appointmentCreateScreenState.doctorSelectedItem != DoctorSlotsItemResponse())
        {
            if (!appointmentCreateScreenState.doctorSelectedItem.slots.isEmpty()) {
                SlotsList(appointmentCreateScreenState.scheduleSelectedItem,
                    doctor = appointmentCreateScreenState.doctorSelectedItem,
                    onSelectSlot = {

                        if(appointmentCreateScreenState.slotSelectedItem != it)
                        {
                            onSelectedSlot(it)
                        }

                    } ,
                    slotSelectedItem = appointmentCreateScreenState.slotSelectedItem)
            }
            else
            {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(scalePxToDp(85f))
                        .padding(end = 18.dp)
                        .background(Lotion),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(20.dp))
                    Text(
                        text = "There is not any available slots, change the doctor or contact your operations manager.",
                        style = MaterialTheme.typography.rhDisplayBold.copy(
                            color = LavenderGray,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
        else
        {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(scalePxToDp(85f))
                    .padding(end = 18.dp)
                    .background(Lotion),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(20.dp))
                Text(
                    text = "Select Doctor to view available slots",
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        color = LavenderGray,
                        fontSize = 12.sp
                    )
                )
            }
        }


    }
}


@Composable
fun SpecialtyList(specialties: SpecialtiesResponse, onSelectSpecialty: (SpecialtyItemResponse) -> Unit = {}, specialtySelectedItem: SpecialtyItemResponse) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
            .height(scalePxToDp(114f)),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 3.dp , end = 18.dp)
    ) {
        items(specialties.items ) {  specialty ->

            SpecialtyItemCard(
                specialtyName = specialty.name,
                doctorsAvailableNum = specialty.availableDoctorsCount,
                selected = (specialtySelectedItem == specialty),
                onClick = {onSelectSpecialty(specialty)  }
            )
        }
    }
}


@Composable
fun DatesList(dates: List<ScheduleItemResponse> , onSelectDate: (ScheduleItemResponse) -> Unit = {} , dateSelectedItem:ScheduleItemResponse ) {

    LazyRow(
        modifier = Modifier.fillMaxWidth()
            .height(scalePxToDp(80f)),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 3.dp , end = 18.dp)
    ) {
        items(dates) {  date ->

            DateItemCard(
                dateTime = date.scheduleDate,
                slotsNum = date.totalSlotsCount,
                selected = (dateSelectedItem == date),
                onClick = {onSelectDate(date)  },
            )
        }
    }
}

@Composable
fun DoctorsList(doctors: List<DoctorSlotsItemResponse>, onSelectDoctor: (DoctorSlotsItemResponse) -> Unit = {}, doctorSelectedItem:DoctorSlotsItemResponse ) {

    LazyRow(
        modifier = Modifier.fillMaxWidth()
            .height(scalePxToDp(85f)),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 3.dp , end = 18.dp)
    ) {
        items(doctors ) {  doctor ->

            DoctorItemCard (
                doctorName = doctor.doctorName,
                doctorCharge = doctor.doctorCharge,
                doctorChargeCurrency = doctor.doctorChargeCurrency,
                appointmentDuration = doctor.appointmentDurationInMinutes,
                selected = (doctorSelectedItem == doctor),
                onClick = {onSelectDoctor(doctor) },
            )
        }
    }
}



@Composable
fun SlotsList(schedule: ScheduleItemResponse ,doctor: DoctorSlotsItemResponse , onSelectSlot: (SlotItemResponse) -> Unit = {} , slotSelectedItem:SlotItemResponse) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
            .height(scalePxToDp(85f)),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 3.dp , end = 18.dp)
    ) {
        items(doctor.slots ) {  slot ->

            SlotItemCard (
                slotDateTime = schedule.scheduleDate,
                slotTime = slot.startTime,
                slotCharge = doctor.doctorCharge,
                slotChargeCurrency = doctor.doctorChargeCurrency,
                slotAppointmentDuration = slot.appointmentDurationInMinutes,
                selected = (slotSelectedItem == slot),
                onClick = {onSelectSlot(slot)},
            )
        }
    }
}


@Composable
fun  PaymentMethodTypesList(paymentMethodTypes: List<String>) {

    var selectedIndex by remember { mutableStateOf(-1) }  // -1 = none selected

    LazyRow(
        modifier = Modifier.fillMaxWidth()
            .height(scalePxToDp(160f)),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 3.dp , end = 18.dp)
    ) {
        itemsIndexed(paymentMethodTypes ) { index, item ->

            PaymentMethodItemCard (
                paymentMethodName = item,
                selected = (selectedIndex == index),
                onClick = {
                    selectedIndex = index        // select ONLY this item
                },
            )
        }
    }
}

@Composable
fun AppointmentCreateComplaintContent(
    appointmentCreateScreenState : AppointmentCreateScreenState,
    onPatientComplaintChanged: (String) -> Unit,
    onPatientMedicalHistoryChanged: (String) -> Unit,
    onPatientNotesChanged: (String) -> Unit,
)
{
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Complaint:",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(scalePxToDp(56f)))

        FloatingLabelUnderlineCustomTextField(
            value = appointmentCreateScreenState.patientComplaint,
            onValueChange = { onPatientComplaintChanged(it)},
            label = "Patient’s complaint",

            placeHolder = "What is the patient’s complaint",
            modifier = Modifier.fillMaxWidth()
                .padding(end = 18.dp),
            maxLines = 6,
            singleLine = false,
            height = 311f
        )

        Spacer(Modifier.height(scalePxToDp(56f)))

        FloatingLabelUnderlineCustomTextField(
            value = appointmentCreateScreenState.patientMedicalHistory,
            onValueChange = { onPatientMedicalHistoryChanged(it) },
            label = "Patient’s Medical History",

            placeHolder = "Patient’s last injuries , operations, illness or genetic issues  in the family",
            modifier = Modifier.fillMaxWidth()
                .padding(end = 18.dp),
            maxLines = 6,
            singleLine = false,
            height = 311f
        )

        Spacer(Modifier.height(scalePxToDp(56f)))

        FloatingLabelUnderlineCustomTextField(
            value = appointmentCreateScreenState.patientNotes,
            onValueChange = { onPatientNotesChanged(it)},
            label = "Notes for doctor",

            placeHolder = "Notes you think the doctor must know",
            modifier = Modifier.fillMaxWidth()
                .padding(end = 18.dp),
            maxLines = 6,
            singleLine = false,
            height = 311f
        )
        Spacer(Modifier.height(scalePxToDp(56f)))

        Spacer(Modifier.weight(1f))

        Row() {
            Box(
                modifier = Modifier.height(scalePxToDp(294f))
                    .width(scalePxToDp(240f))
                    .background(Periwinkle,
                        shape = RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center,){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.ic_med_upload_icon),
                        contentDescription = null,
                        modifier = Modifier.height(scalePxToDp(86f))
                            .width(scalePxToDp(86f)),
                        contentScale = ContentScale.FillWidth

                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Choose an ID to upload",
                        style = MaterialTheme.typography.rhDisplayMedium.copy(
                            color = YankeesBlue,
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center
                        ),
                    )
                }

            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier.height(scalePxToDp(294f))
                    .width(scalePxToDp(240f))
                    .background(Periwinkle,
                        shape = RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center,){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.ic_med_capture_icon),
                        contentDescription = null,
                        modifier = Modifier.height(scalePxToDp(86f))
                            .width(scalePxToDp(86f)),
                        contentScale = ContentScale.FillWidth

                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Capture ID with camera",
                        style = MaterialTheme.typography.rhDisplayMedium.copy(
                            color = YankeesBlue,
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center
                        ),
                    )
                }

            }
        }
    }
}

@Composable
fun AppointmentCreatePaymentContent()
{
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Payment",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            var checkedPaid by remember { mutableStateOf(false) }
            CustomSwitch(
                checked = checkedPaid,
                onCheckedChange ={ checkedPaid = it },
                trackWidth = scalePxToDp(100f),
                trackHeight =scalePxToDp(55f),
                thumbSize = scalePxToDp(45f),
                trackColor = White,
                thumbColorOn = PrimaryMidLinkColor,
                thumbColorOff = Periwinkle,
                borderWidth = 1.dp,
                borderColor = PrimaryMidLinkColor
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Paid",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.width(18.dp))

        }
        Spacer(Modifier.height(10.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(scalePxToDp(100f))
            .padding(end = 18.dp)
            .background(Periwinkle, shape = RoundedCornerShape(5.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(25.dp))
            Text(
                text = "Consultation Fee",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 12.sp
                )
            )
            Spacer(Modifier.width(25.dp))
            Box(
                modifier = Modifier.height(scalePxToDp(65f))
                    .width(scalePxToDp(120f))
                    .background(White,
                        shape = RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center,){
                Text(
                    text = "140",
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        color = PrimaryMidLinkColor,
                        fontSize = 13.sp
                    ),
                    textAlign = TextAlign.Center,
                )}

            Spacer(Modifier.width(25.dp))
            Text(
                text = "AED",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 12.sp
                )
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Payment Method",
            style = MaterialTheme.typography.rhDisplayMedium.copy(
                color = PrimaryMidLinkColor,
                fontSize = 12.sp
            )
        )
        Spacer(Modifier.height(10.dp))

        PaymentMethodTypesList(paymentMethodTypes)
        Spacer(Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(end = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var insuranceProvider by remember { mutableStateOf("") }
            FloatingLabelUnderlineCustomTextField(
                value = insuranceProvider,
                onValueChange = { newText ->
                    insuranceProvider = newText },
                label = "Insurance Provider",
                placeHolder = "Insurance Provider",
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(20.dp))
            var insuranceType by remember { mutableStateOf("") }
            FloatingLabelUnderlineCustomTextField(
                value = insuranceType,
                onValueChange = { newText ->
                    insuranceType = newText },
                label = "Insurance Type",
                placeHolder = "Insurance Type",
                modifier = Modifier.weight(1f)
            )
        }

    }
}

@Composable
fun AppointmentCreateCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults
            .cardColors(
                containerColor = White
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier= Modifier.fillMaxSize()
            .padding(top= 30.dp , start = 15.dp, bottom = 30.dp)) {
            content()
        }
    }
}

@Composable
fun AppointmentCreateScreenPrev()
{
    AppointmentCreateContainer(
        navController = NavController(LocalContext.current),
        appointmentCreateScreenState = AppointmentCreateScreenState(),
        onSpecialtySelected = {},
        onSelectedSchedule = {},
        onSelectedDoctor = {},
        onSelectedSlot = {},
        onPatientNameChanged = {},
        onPatientGenderChanged = {},
        onPatientComplaintChanged = {},
        onPatientMedicalHistoryChanged = {},
        onPatientNotesChanged = {},
        onPatientDateOfBirthChanged = {}
    )

}
@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape"
)
@Composable
fun AppointmentScreenPreview() {
    KotlinTestTheme {
        AppointmentCreateScreenPrev()
    }
}




var  paymentMethodTypes = listOf(
    "Insurance",
    "Credit / Debit",
    "Cash"
)