package com.example.kotlintest.features_autentication.presentation.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlintest.R
import com.example.kotlintest.features_autentication.presentation.components.AuthTopBar
import com.example.kotlintest.ui.theme.KotlinTestTheme
import com.example.kotlintest.util.scalePxToDp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kotlintest.features_autentication.presentation.components.AuthCardContainer
import com.example.kotlintest.features_autentication.presentation.viewmodel.AuthViewModel
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.ui.theme.rhDisplaySemiBold

@Composable
fun AuthScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.auth_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
                .padding(top = 45.dp),
            contentScale = ContentScale.FillBounds

        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                AuthTopBar()
            },
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(start = scalePxToDp(100f))   // <-- margin from bottom & left
                                .align(Alignment.Bottom)
                        ) {
                            Text(
                                text = "Welcome to MedLink.",
                                style = MaterialTheme.typography.rhDisplayMedium.copy(
                                    fontSize = 24.sp,
                                    color =Lotion,
                                ),
                            )
                            Text(
                                text = "The power of connectivity in telehealth.",
                                style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                                    fontSize = 30.sp,
                                    color =Lotion,
                                ),
                            )
                        }

                        Box(modifier = Modifier
                            .fillMaxWidth(),
                            ){
                            AuthCardContainer(
                                navController = navController,
                                modifier = Modifier
                                    .align(Alignment.Center))
                        }


                    }


                }
            }
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape"
)
@Composable
fun AuthScreenPreview() {
    KotlinTestTheme {
        AuthScreen(
            navController = NavController(LocalContext.current)
        )
    }
}
