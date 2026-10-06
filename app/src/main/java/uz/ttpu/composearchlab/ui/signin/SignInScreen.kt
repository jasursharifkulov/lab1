package uz.ttpu.composearchlab.ui.signin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.ttpu.composearchlab.ui.theme.ComposeArchLabTheme

@Composable
fun SignInRoute(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = viewModel()
) {
    val uiState = viewModel.uiState.value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is SignInUiState.Error) {
            snackbarHostState.showSnackbar(uiState.message)
            viewModel.onErrorShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        SignInScreen(
            uiState = uiState,
            onSignIn = viewModel::onSignIn,
            modifier = Modifier.padding(innerPadding) // Bu yerda innerPadding to'g'rilandi
        )
    }
}

@Composable
fun SignInScreen(
    uiState: SignInUiState,
    onSignIn: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is SignInUiState.SignedIn -> {
                Text(
                    text = "Welcome, ${uiState.email}!",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (uiState is SignInUiState.InProgress) {
                        CircularProgressIndicator()
                    }

                    Button(
                        onClick = { onSignIn(email, password) },
                        enabled = uiState !is SignInUiState.InProgress,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign in")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Signed Out")
@Composable
fun SignedOutPreview() {
    ComposeArchLabTheme { SignInScreen(SignInUiState.SignedOut, { _, _ -> }) }
}

@Preview(showBackground = true, name = "In Progress")
@Composable
fun InProgressPreview() {
    ComposeArchLabTheme { SignInScreen(SignInUiState.InProgress, { _, _ -> }) }
}
