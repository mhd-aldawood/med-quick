package com.example.kotlintest.features_autentication.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlintest.ui.theme.KotlinTestTheme
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.SpaceCadet
import com.example.kotlintest.ui.theme.YankeesBlue
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.util.scalePxToDp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.example.kotlintest.ui.theme.CeruleanBlue
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.Platinum
import com.example.kotlintest.ui.theme.rhDisplayExtraBold
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.ui.theme.rhDisplaySemiBold
import androidx.compose.ui.input.key.Key
import com.example.kotlintest.features_autentication.presentation.states.AuthScreenState
import com.example.kotlintest.ui.theme.FrenchWine
import com.example.kotlintest.ui.theme.deepDarkBlue
import kotlinx.coroutines.delay


@Composable
fun OtpCardScreen(
    authScreenState : AuthScreenState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onOtpResend: () -> Unit = {},
    onOtpComplete: (String) -> Unit = {}
) {


    CustomCard(
        modifier = modifier,
        header = { OtpCardHeader("Verify your account") },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {

            }
            OtpCardContent(
                authScreenState = authScreenState,
                onOtpComplete = onOtpComplete
            )
            if (authScreenState.checkOtpRequest.isLoading ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(70.dp),
                        color = PrimaryMidLinkColor,
                        strokeWidth = 6.dp,
                        trackColor = Platinum
                    )
                }
            }

        },
        bottom = {OtpCardBottom (
            onBack = onBack,
            onOtpResend = onOtpResend
        )}
    )

}

@Composable
fun OtpCardHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.rhDisplayBold.copy(
            color = SpaceCadet,
            fontSize = 30.sp
        )
    )
}

@Composable
fun OtpCardBottom(
    onBack: () -> Unit = {},
    onOtpResend: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Back",
                style = MaterialTheme.typography.rhDisplayBlack.copy(
                    fontSize = 16.sp,
                    color =PrimaryMidLinkColor,
                    textDecoration = TextDecoration.Underline,
                ),
                modifier = Modifier.clickable { onBack() }
                    .padding(start = 40.dp)
            )

            // RESEND
            ResendCodeCountdown(
                totalSeconds = 60,
                onResend = onOtpResend
            )
        }

    }
}

@Composable
fun OtpCardContent(
    authScreenState : AuthScreenState,
    onOtpComplete: (String) -> Unit
) {
    val userName = authScreenState.checkUserName
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Spacer(Modifier.height(scalePxToDp(60f)))

        Text(
            text = "Welcome $userName.",
            style = MaterialTheme.typography.rhDisplayMedium.copy(
                color = SpaceCadet,
                fontSize = 20.sp
            )

        )

        Spacer(Modifier.height(4.dp))

        Row {
            Text(
                text = "4 digit code was sent to: ",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = SpaceCadet,
                    fontSize = 12.sp
                )
            )
            Text(
                text = "$userName",
                style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                    color = CeruleanBlue,
                    fontSize = 12.sp
                )

            )
        }

        Spacer(Modifier.height(scalePxToDp(120f)))

        OtpInput(
            onComplete = { code ->
                onOtpComplete(code)  // Auto submit callback
            }
        )

        if (authScreenState.checkOtpRequest.isSuccess  && !authScreenState.checkOtpRequest.data.isTokenValid){
            Spacer(Modifier.height(scalePxToDp(75f)))
            Text(
                color = FrenchWine,
                text = "Your Otp Code not valid")
        }
    }
}

@Composable
fun OtpDigitBox(
    otpLength:Int,
    value: String,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        textStyle =  MaterialTheme.typography.rhDisplayExtraBold.copy(
            color = YankeesBlue,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
        ),
        singleLine = true,
        modifier = modifier
            .width((240/otpLength).dp)
            .height(60.dp)
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown &&
                    event.key == Key.Backspace
                ) {
                    onBackspace()
                    true
                } else false
            },
        decorationBox = { inner ->
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                inner()

                Spacer(Modifier.height(4.dp))

                Box(
                    Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .background(Platinum)
                )
            }
        }
    )
}

@Composable
fun OtpInput(
    onComplete: (String) -> Unit
) {
    val otpLength = 4
    val values = remember { List(otpLength) { mutableStateOf("") } }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        repeat(otpLength) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)               // << Equal width
                    .padding(horizontal = 4.dp)
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                OtpDigitBox(
                    otpLength = otpLength,
                    value = values[index].value,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            values[index].value = newValue

                            // Move to next if one digit typed
                            if (newValue.isNotEmpty() && index < otpLength - 1) {
                                focusRequesters[index + 1].requestFocus()
                            }

                            // Auto submit when all digits typed
                            if (values.all { it.value.isNotEmpty() }) {
                                keyboardController?.hide()
                                onComplete(values.joinToString("") { it.value })
                            }
                        }
                    },
                    onBackspace = {
                        if (values[index].value.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                        values[index].value = ""
                    },
                    modifier = Modifier.focusRequester(focusRequesters[index])
                )
            }
        }
    }

    // Auto-focus first box
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}

@Composable
fun ResendCodeCountdown(
    totalSeconds: Int = 60,
    onResend: () -> Unit
) {
    var secondsLeft by remember { mutableStateOf(0) }
    var isEnabled by remember { mutableStateOf(true) }

    // Countdown effect
    LaunchedEffect(secondsLeft) {
        if (secondsLeft > 0) {
            isEnabled = false
            delay(1000)
            secondsLeft--
        } else {
            isEnabled = true
        }
    }

    OutlinedButton(
        onClick =  {
            if (isEnabled) {
                onResend()
                secondsLeft = totalSeconds    // restart timer
                isEnabled = false
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryMidLinkColor,
            contentColor = Lotion,
            disabledContainerColor = Color.White,
            disabledContentColor = PrimaryMidLinkColor
        ),
        enabled = isEnabled,
        shape = RoundedCornerShape(40.dp),
        border = BorderStroke(1.dp, deepDarkBlue),
        modifier = Modifier
            .height(scalePxToDp(124f))
            .width(scalePxToDp(500f))

    ) {
        Text(
            text = if (isEnabled)
                "Resend Code"
            else
                "Resend Code in ${secondsLeft}sec",
            style = MaterialTheme.typography.rhDisplayBlack.copy(
                fontSize = 16.sp
            )

        )
    }

}

@Composable
fun OtpCardScreenPrev()
{
    OtpCardScreen(
        authScreenState = AuthScreenState(),
    )
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp"
)
@Composable
fun OtpCardScreenPreview() {
    KotlinTestTheme {
        OtpCardScreenPrev()
    }
}
