package com.example.kotlintest.screens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kotlintest.core.EventsEffect
import com.example.kotlintest.data.source.network.User
import com.example.kotlintest.util.Logger

val images = listOf(
    "Res.drawable.group_724", // Replace with your image resource IDs
    "Res.drawable.group_725",
   " Res.drawable.group_727",
    "Res.drawable.group_726"
)
val texts = listOf(
    "Mobile based Secure Sign In" to "Login and sign up to many digital services with one account",
    "Digital Signature" to "Sign and verify documents digitally",
    "Documents Sharing" to "Request and share official documents",
    "Unlimited Access" to "Goverment. Society and Entertainment"
)

@Composable
fun UserScreen(viewModel: TestViewModel=hiltViewModel()) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    LaunchedEffect (Unit){
        viewModel.trySendAction(TestAction.Test)
    }
    EventsEffect(viewModel) { event ->
        when (event) {
            is TestEvents.Send_Events -> {
                Logger.i("TAG", "UserScreen: ")

            }
        }
    }
//    if(result is ApiResult.Success)
//        (result as ApiResult.Success).data?.let { UserDetailsScreen(users = it) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(users: List<User>) {
    // Scaffold with top bar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Details") },
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(users) { user ->
                UserDetailsCard(user)
            }
        }
    }
}

@Composable
fun UserDetailsCard(user: User) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // User Name
            Text(
                text = "${user.FirstName} ${user.LastName}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date of Birth
            Text(
                text = "Date of Birth: ${user.DateOfBirth}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Address Header
            Text(
                text = "Address:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Address Details
            Text(
                text = "${user.Address?.HouseNumber}, ${user.Address?.Street}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${user.Address?.State}, ${user.Address?.ZipCode}",
                style = MaterialTheme.typography.bodyMedium
            )
            user.Address?.Country?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp)) // Space at the top

        // Centered Content
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = false,
                    onCheckedChange = { /* Handle Remember Me logic */ }
                )
                Text(text = "Remember me")
            }
        }

        // Footer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign in with",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { /* Handle Google Login */ }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_google),
//                        contentDescription = "Google",
//                        modifier = Modifier.height(48.dp).width(48.dp)
//
//                    )
                }
                IconButton(onClick = { /* Handle Facebook Login */ }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_facebook),
//                        contentDescription = "Facebook",
//                        modifier = Modifier.height(48.dp).width(48.dp)
//                    )
                }
                IconButton(onClick = { /* Handle Apple Login */ }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_apple),
//                        contentDescription = "Apple",
//                        modifier = Modifier.height(48.dp).width(48.dp)
//                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreen_() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF1C1C1E)), // Background color similar to the design
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top section (spacing)
        Spacer(modifier = Modifier.height(24.dp))

        // Main Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Login",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PhoneNumberInputField()

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = false,
                    onCheckedChange = { /* Handle Remember Me logic */ },
                    colors = CheckboxDefaults.colors(checkmarkColor = Color.White)
                )
                Text(
                    text = "Remember me",
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Footer Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { /* Handle Sign In */ },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34C759)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "SIGN IN", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Or sign in with",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            ) {
                IconButton(onClick = { /* Google Login */ }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_google),
//                        contentDescription = "Google",
//                        modifier = Modifier
//                            .widthIn(36.dp)
//                            .heightIn(36.dp),
//                        tint = Color.Unspecified
//                    )
                }
                IconButton(onClick = { /* Facebook Login */ }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_facebook),
//                        contentDescription = "Facebook",
//                        modifier = Modifier
//                            .widthIn(36.dp)
//                            .heightIn(36.dp),
//                        tint = Color.Unspecified
//                    )
                }
                IconButton(onClick = { /* Apple Login */ }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_apple),
//                        contentDescription = "Apple",
//                        modifier = Modifier
//                            .widthIn(36.dp)
//                            .heightIn(36.dp),
//                        tint = Color.Unspecified
//                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = "Don’t have an account? ",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Sign Up",
                    color = Color(0xFF007AFF),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { /* Handle Sign Up */ }
                )
            }
        }
    }
}
@Preview
@Composable
fun PhoneNumberInputField() {
    var phoneNumber by remember { mutableStateOf("") }
    val countryCode = "+66"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .background(Color(0xFF2C2C2E), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Text(
            text = countryCode,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(end = 8.dp)
        )
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = { Text("00 000 0000", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(

            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )
    }
}
@Preview
@Composable
fun PagerSection() {
    val pageCount = 4
    val pagerState = rememberPagerState(
        pageCount = { pageCount },
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ,
        verticalAlignment = Alignment.Top
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            // Display Image
//            Image(
//                painter = painterResource(0),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .width(223.dp)
//                    .height(150.dp)
//            )
            MiddleSection(it)

            Row(
                Modifier
                    .height(26.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pageCount) { iteration ->
                    Logger.i("TAG", "PagerSection: ${pagerState.currentPage} ")
                    val color = if (pagerState.currentPage == iteration) Color.Black else Color.Black
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .padding(16.dp)
                            .background(color, CircleShape)

                    )
                }
            }

        }

    }
}
@Composable
fun MiddleSection(page:Int) {
    Spacer(modifier = Modifier.height(160.dp))
    // Display Title
    Text(
        text = texts[page].first,
        style = TextStyle(
            fontSize = 22.sp,
            fontWeight = W700,
            color = Color.Black,
            lineHeight = 28.sp,
        )
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Display Description
    Text(
        textAlign = TextAlign.Center,
        text = texts[page].second,
        style = TextStyle(fontSize = 17.sp, fontWeight = W400, color = Color.Gray)
        , lineHeight = 22.sp
    )
}
@Preview
@Composable
fun PagerFromDocumentation(){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val pageCount = 10
        val pagerState = rememberPagerState(
            pageCount = { pageCount },
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Page $it")
            }
        }
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(color, CircleShape)
                        .size(10.dp)
                )
            }
        }
    }
}
