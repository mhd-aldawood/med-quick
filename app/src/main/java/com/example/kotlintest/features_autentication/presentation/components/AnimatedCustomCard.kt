package com.example.kotlintest.features_autentication.presentation.components

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kotlintest.MainActivity
import com.example.kotlintest.NavDestination
import com.example.kotlintest.features_autentication.data.model.ClinicItemResponse
import com.example.kotlintest.features_autentication.presentation.states.AuthScreenState
import com.example.kotlintest.features_autentication.presentation.viewmodel.AuthViewModel
import com.example.kotlintest.navigation.safeNavigate
import com.example.kotlintest.util.scalePxToDp
import kotlin.jvm.java

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCustomCard(
    navController: NavController,
    authScreenState : AuthScreenState,
    modifier: Modifier = Modifier,
    screen: AuthCardScreen,
    onProviderSelected: (ClinicItemResponse) -> Unit,
    onCheckUserNameChanged: (String) -> Unit = {},
    onCheckUsernameClick:()->Unit = {},
    onOtpBack: () -> Unit,
    onOtpResend: () -> Unit = {},
    onOtpComplete: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onResetPasswordClick: () -> Unit = {},
    onSignInUserNameChanged: (String) -> Unit,
    onSignInPasswordChanged: (String) -> Unit,
    onSignInClickEvent: () -> Unit = {},
) {
    AnimatedContent(
        targetState = screen,
        label = "cardScreen",
        transitionSpec = {
            slideInHorizontally { it } + fadeIn() togetherWith
                    slideOutHorizontally { -it } + fadeOut()
        }
    ) { target ->
        val context = LocalContext.current
        when (target) {

            is AuthCardScreen.ProviderScreen -> {
                ProviderCardScreen(
                    authScreenState = authScreenState,
                    modifier =modifier ,
                    onProviderSelected = onProviderSelected
                )
            }

            is AuthCardScreen.CheckUserNameScreen -> {
                CheckUserNameCardScreen(
                    authScreenState = authScreenState,
                    modifier =modifier ,
                    onCheckUserNameChanged = onCheckUserNameChanged,
                    onCheckUsernameClick = onCheckUsernameClick
                )
            }

            is AuthCardScreen.OtpScreen -> {
                OtpCardScreen(
                    authScreenState = authScreenState,
                    modifier =modifier ,
                    onBack = onOtpBack,
                    onOtpResend = onOtpResend,
                    onOtpComplete = onOtpComplete
                )
            }
            is AuthCardScreen.ResetPasswordScreen -> {
                ResetPasswordCardScreen(
                    authScreenState = authScreenState,
                    modifier =modifier ,
                    onPasswordChanged = onPasswordChanged,
                    onConfirmPasswordChanged = onConfirmPasswordChanged,
                    onResetPasswordClick = onResetPasswordClick
                )
            }


            is AuthCardScreen.SignInScreen -> {
                SignInCardScreen(
                    authScreenState = authScreenState,
                    onSignInUserNameChanged = onSignInUserNameChanged,
                    onSignInPasswordChanged = onSignInPasswordChanged,
                    onSignInClickEvent = onSignInClickEvent ,
                    modifier =modifier ,
                )
            }
            is AuthCardScreen.HomeScreen -> {
//                val intent = Intent(context, MainActivity::class.java)
//                context.startActivity(intent)
                navController.safeNavigate(NavDestination.HOME_SCREEN)
            }

        }
    }

}