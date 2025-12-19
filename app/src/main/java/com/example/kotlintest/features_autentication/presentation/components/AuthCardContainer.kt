package com.example.kotlintest.features_autentication.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kotlintest.features_autentication.presentation.events.AuthScreenEvent
import com.example.kotlintest.features_autentication.presentation.viewmodel.AuthViewModel

@Composable
fun AuthCardContainer(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    //var screen by remember { mutableStateOf<AuthCardScreen>(AuthCardScreen.ProviderScreen) }
    val authScreenState by viewModel.authScreenState.collectAsState()
    Box(modifier = modifier) {
        AnimatedCustomCard(
            navController = navController,
            authScreenState = authScreenState,
            modifier = modifier,
            screen = authScreenState.screen,
            onProviderSelected = {provider ->
                //screen = AuthCardScreen.OtpScreen
                viewModel.onEvent(AuthScreenEvent.SaveSelectedProviderEvent(provider))
                //viewModel.onEvent(AuthScreenEvent.ChangeAuthScreenCardEvent(AuthCardScreen.CheckUserNameScreen))

            },
            onCheckUserNameChanged ={checkUserName ->
                viewModel.onEvent(AuthScreenEvent.OnCheckUserNameChangedEvent(checkUserName))
            },
            onCheckUsernameClick = {
                viewModel.onEvent(AuthScreenEvent.CheckUserNameEvent)
                //viewModel.onEvent(AuthScreenEvent.CheckUserNameChangeScreenEvent)
            },
            onOtpBack = {
                //screen = AuthCardScreen.ProviderScreen
                viewModel.onEvent(AuthScreenEvent.ResetEvent)
                viewModel.onEvent(AuthScreenEvent.ChangeAuthScreenCardEvent(AuthCardScreen.ProviderScreen))
            },
            onOtpResend = {
                viewModel.onEvent(AuthScreenEvent.ResendOtpCodeEvent)
            },
            onOtpComplete = { otp ->
                //screen = AuthCardScreen.SignInScreen
                viewModel.onEvent(AuthScreenEvent.CheckOtpCodeEvent(otp))
            },
            onPasswordChanged = {password ->
                viewModel.onEvent(AuthScreenEvent.OnPasswordChangedEvent(password))
            },
            onConfirmPasswordChanged = {confirmPassword ->
                viewModel.onEvent(AuthScreenEvent.OnConfirmPasswordChangedEvent(confirmPassword))
            },
            onResetPasswordClick = {
                viewModel.onEvent(AuthScreenEvent.OnResetPasswordClickEvent)
            },
            onSignInUserNameChanged = {signInUserName ->
                viewModel.onEvent(AuthScreenEvent.OnSignInUserNameChangedEvent(signInUserName))
            },
            onSignInPasswordChanged = {signInPassword ->
                viewModel.onEvent(AuthScreenEvent.OnSignInPasswordChangedEvent(signInPassword))
            },
            onSignInClickEvent = {
                viewModel.onEvent(AuthScreenEvent.OnSignInClickEvent)
            }
        )
    }

}