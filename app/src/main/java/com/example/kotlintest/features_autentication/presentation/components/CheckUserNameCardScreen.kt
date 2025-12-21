package com.example.kotlintest.features_autentication.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlintest.ui.theme.KotlinTestTheme
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.SpaceCadet
import com.example.kotlintest.ui.theme.YankeesBlue
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.util.scalePxToDp
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.Platinum
import com.example.kotlintest.ui.theme.rhDisplayMedium
import androidx.compose.ui.text.input.VisualTransformation
import com.example.kotlintest.features_autentication.data.model.ClinicItemResponse
import com.example.kotlintest.features_autentication.presentation.events.AuthScreenEvent
import com.example.kotlintest.features_autentication.presentation.states.AuthScreenState
import com.example.kotlintest.features_autentication.presentation.viewmodel.AuthViewModel
import com.example.kotlintest.ui.theme.FrenchWine
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.ui.theme.deepDarkBlue

@Composable
fun CheckUserNameCardScreen(
    authScreenState : AuthScreenState,
    modifier: Modifier = Modifier,
    onCheckUserNameChanged: (String) -> Unit,
    onCheckUsernameClick: () -> Unit = {},
) {


    CustomCard(
        modifier = modifier,
        header = { CheckUserNameCardHeader("Check User Name") },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {

            }
            CheckUserNameCardContent(
                authScreenState = authScreenState,
                onCheckUserNameChanged = onCheckUserNameChanged
            )
            if (authScreenState.checkUserNameObj.isLoading ) {
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
        bottom = {CheckUserNameCardBottom (onCheckUsernameClick)}
    )

}


@Composable
fun CheckUserNameCardHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.rhDisplayBold.copy(
            color = SpaceCadet,
            fontSize = 30.sp
        )
    )
}

@Composable
fun CheckUserNameCardBottom(
    onCheckUsernameClick: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedButton(
                onClick =  onCheckUsernameClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryMidLinkColor,
                    contentColor = Lotion,
                    disabledContainerColor = Color.White,
                    disabledContentColor = PrimaryMidLinkColor
                ),
                shape = RoundedCornerShape(40.dp),
                border = BorderStroke(1.dp, deepDarkBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(scalePxToDp(124f))


            ) {
                Text(
                    text = "Next",
                    style = MaterialTheme.typography.rhDisplayBlack.copy(
                        fontSize = 20.sp
                    )

                )
            }
        }

    }
}


@Composable
fun CheckUserNameCardContent(
    authScreenState : AuthScreenState,
    onCheckUserNameChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(Modifier.height(scalePxToDp(60f)))

        Text(
            text = "Use your registered username as a nurse or a doctor",
            style = MaterialTheme.typography.rhDisplayMedium.copy(
                color = SpaceCadet,
                fontSize = 20.sp
            )

        )


        Row(
            modifier = Modifier.fillMaxSize()
                .weight(1f)
                .padding(bottom = scalePxToDp(100f)),
            verticalAlignment = Alignment.CenterVertically,


        ) {
            FloatingLabelUnderlineTextField(
                value = authScreenState.checkUserName,
                onValueChange = { onCheckUserNameChanged(it) },
                label = "Username",
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (authScreenState.checkUserNameObj .error != ""){
            Text(
                color = FrenchWine,
                text = "Error: ${authScreenState.checkUserNameObj.error}")
        }




    }
}
@Composable
fun CheckUserNameCardScreenPrev()
{
    CheckUserNameCardScreen(
        authScreenState = AuthScreenState(),
        modifier = Modifier,
        onCheckUserNameChanged = {},
    )
}



@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp"
)
@Composable
fun CheckUserNameCardScreenPreview() {
    KotlinTestTheme {
        CheckUserNameCardScreenPrev()
    }
}
