package com.example.kotlintest.features_autentication.presentation.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlintest.ui.theme.KotlinTestTheme
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.SpaceCadet
import com.example.kotlintest.ui.theme.YankeesBlue
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.util.scalePxToDp
import androidx.compose.material3.TextField
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlintest.features_autentication.data.model.ClinicItemResponse
import com.example.kotlintest.features_autentication.presentation.states.AuthScreenState
import com.example.kotlintest.features_autentication.utils.data.model.RequestStates
import com.example.kotlintest.ui.theme.LavenderGray
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.Platinum
import com.example.kotlintest.features_autentication.presentation.viewmodel.AuthViewModel


@Composable
fun ProviderCardScreen(
    authScreenState : AuthScreenState,
    modifier: Modifier = Modifier,
    onProviderSelected: (ClinicItemResponse) -> Unit,
) {




    CustomCard(
        modifier = modifier,
        header = { ProviderCardHeader("Choose your provider") },
        content = {
            ProviderCardContent(
                authScreenState = authScreenState,
                onProviderSelected = onProviderSelected
            )
        },
    )
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun ProviderCardHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.rhDisplayBold.copy(
            color = SpaceCadet,
            fontSize = 30.sp
        )
    )
}

@Composable
fun ProviderCardContent(
    authScreenState : AuthScreenState,
    onProviderSelected:  (ClinicItemResponse) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val  providers = authScreenState.providerList.data as ArrayList<ClinicItemResponse>

    // Filter providers by search
    val filteredProviders = providers.filter { provider ->
        provider.name.contains(searchQuery, ignoreCase = true)
    }
    Column(
        modifier = Modifier
            .padding( 0.dp)
    ) {
        /** SEARCH BAR **/
        TextField(

            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by provider name",
                style = MaterialTheme.typography.rhDisplayBlack.copy(
                    color = LavenderGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    tint = Color.Unspecified,
                    contentDescription = "Search",
                    modifier = Modifier.size(scalePxToDp(56f))
                )
            },
            modifier = Modifier.fillMaxWidth(),

            // Underline-only style (Material 3)
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xFF1A2E57),
                unfocusedIndicatorColor = Color(0xFFBBBBBB),
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),

            shape = RectangleShape,
            singleLine = true
        )
        when {
            authScreenState.providerList.isLoading -> {
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
            authScreenState.providerList .error != "" -> {
                Text("Error: ${authScreenState.checkUserNameObj.error}")
            }
            authScreenState.providerList .isSuccess  -> {
                Spacer(modifier = Modifier.height(15.dp))

                /** LIST OF PROVIDERS **/
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding( top = scalePxToDp(12f), start = scalePxToDp(12f),end = scalePxToDp(12f), bottom = scalePxToDp(5f))
                ) {
                    items(filteredProviders) { provider ->

                        //val isSelected = provider == selectedProvider

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width =  1.dp,
                                    color =  Lotion,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .background( Color.White)
                                .clickable { onProviderSelected(provider) }
                                .padding(scalePxToDp(10f))


                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Provider Icon
                                Box(
                                    modifier = Modifier
                                        .size( scalePxToDp(120f))                              // circle size
                                        .background(Platinum, CircleShape)  // circle fill color
                                        .clip(CircleShape)                        // clip image inside circle
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.logo_fuad),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .padding(5.dp),

                                        contentScale = ContentScale.Inside
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = provider.name .toString(),
                                    style = MaterialTheme.typography.rhDisplayBold.copy(
                                        color = YankeesBlue,
                                        fontSize = 20.sp
                                    )

                                )
                            }
                        }
                    }
                }
            }

        }


        Spacer(modifier = Modifier.height(25.dp))
    }
}



//@Preview(
//    showBackground = true,
//    device = "spec:width=1280dp,height=800dp"
//)
//@Composable
//fun ProviderCardScreenPreview() {
//    KotlinTestTheme {
//        ProviderCardScreen()
//    }
//}


