package com.example.novella.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.novella.components.EmailInput
import com.example.novella.components.NovellaLogo
import com.example.novella.components.PasswordInput
import com.example.novella.components.safeClickable
import com.example.novella.navigation.NovellaScreens

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = viewModel()
) {
    val showLoginForm = rememberSaveable { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NovellaLogo()

            if (showLoginForm.value) {
                UserForm(
                    loading = false,
                    isRegistered = false
                ) { email, password ->
                    viewModel.signInWithEmailAndPassword(email, password) {
                        navController.navigate(NovellaScreens.HomeScreen.name) {
                            popUpTo(NovellaScreens.LoginScreen.name) { inclusive = true }
                        }
                    }
                }
            } else {
                UserForm(
                    loading = false,
                    isRegistered = true
                ) { email, password ->
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(NovellaScreens.HomeScreen.name) {
                            popUpTo(NovellaScreens.LoginScreen.name) { inclusive = true }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val actionText = if (showLoginForm.value) "Sign up" else "Login"

                Text(text = "New user?")

                Text(
                    text = actionText,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .safeClickable {
                            showLoginForm.value = !showLoginForm.value
                        },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview
@Composable
fun UserForm(
    loading: Boolean = false,
    isRegistered: Boolean = false,
    onDone: (String, String) -> Unit = { _, _ -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }

    val passwordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val validInputs = remember(email.value, password.value) {
        email.value.isNotBlank() && password.value.isNotBlank()
    }

    Column(
        modifier = Modifier
            .height(260.dp)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isRegistered) {
            Text(
                text = "Register",
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }

        EmailInput(
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions {
                passwordFocusRequester.requestFocus()
            }
        )

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequester),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!validInputs) return@KeyboardActions
                keyboardController?.hide()
                onDone(email.value.trim(), password.value.trim())
            }
        )

        SubmitButton(
            text = if (isRegistered) "Register" else "Login",
            loading = loading,
            enabled = validInputs
        ) {
            keyboardController?.hide()
            onDone(email.value.trim(), password.value.trim())
        }
    }
}

@Composable
fun SubmitButton(
    text: String,
    loading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(),
        enabled = !loading && enabled,
        shape = CircleShape
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(text = text, modifier = Modifier.padding(6.dp))
        }
    }
}
