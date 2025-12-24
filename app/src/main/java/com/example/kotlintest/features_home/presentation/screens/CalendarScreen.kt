package com.example.kotlintest.features_home.presentation.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.KotlinTestTheme
import com.example.kotlintest.util.scalePxToDp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kotlintest.component.DynamicCalendarNew
import com.example.kotlintest.core.EventsEffect
import com.example.kotlintest.core.model.CalendarAppointmentCardModel
import com.example.kotlintest.features_home.presentation.screens.views.MainHeaderSection
import com.example.kotlintest.features_home.presentation.screens.views.RightContentSection
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.PaleCerulean
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.White
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.yellow

@Composable
fun CalendarScreen(navController: NavController, viewModel: CalendarViewModel = hiltViewModel(),onBtnClick: (String) -> Unit) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel) {
        when(it){
            is CalendarEvents.NavigateToExamination -> {
               onBtnClick(it.appointmentsId)
            }
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp)
                .weight(12f),
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(scalePxToDp(85f)),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                MainHeaderSection(navController = navController)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .weight(1f),
            )
            {
                Box(modifier = Modifier.fillMaxSize()) {
                    DynamicCalendarNew(
                        endHour = 23,        // calendar ends at 12 PM
                        endDays = 7,         // today + next 7 days
                        events = uiState.appointmentsList,
                        modifier = Modifier.fillMaxSize(), onCardClick = { id ->
                            viewModel.trySendAction(CalendarActions.OnCardClicked(id))

                        }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp)
                .weight(4f)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(scalePxToDp(85f)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RightHeaderSection()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .weight(1f),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    uiState.selectedCard?.let {
                        RightContentSection(it, onBtnClick = { id ->
                            viewModel.trySendAction(CalendarActions.BtnClicked(id))
                        })
                    }
                }
            }
        }
    }

}


@Composable
fun RightHeaderSection() {
    Row() {
        OutlinedButton(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = yellow,
                contentColor = Lotion,
                disabledContainerColor = Color.White,
                disabledContentColor = PrimaryMidLinkColor
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, White),
            modifier = Modifier
                .height(scalePxToDp(70f))
                .width(scalePxToDp(310f))

        ) {
            Icon(
                painter = painterResource(R.drawable.ic_med_ongoing_visit_icon),
                contentDescription = "Menu",
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(scalePxToDp(70f))
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Ongoing Visit",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = Lotion,
                    fontSize = 14.sp
                )
            )
            Spacer(Modifier.width(10.dp))
        }
        Spacer(Modifier.weight(1f))
        Icon(
            painter = painterResource(R.drawable.ic_med_notification_icon),
            contentDescription = "Menu",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(scalePxToDp(70f))
        )

    }

}


@Composable
fun HomeScreenPrev() {
    CalendarScreen(navController = NavController(LocalContext.current), onBtnClick = {})
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape"
)
@Composable
fun HomeScreenPreview() {
    KotlinTestTheme {
        HomeScreenPrev()
    }
}
