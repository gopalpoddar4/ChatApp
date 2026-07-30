package `in`.gopalpoddar.textspur.features.auth.signup.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.gopalpoddar.textspur.features.auth.signup.presentation.components.LoginNavigationText
import `in`.gopalpoddar.textspur.features.auth.signup.presentation.components.SignUpButton
import `in`.gopalpoddar.textspur.features.auth.signup.presentation.components.SignUpEmailField
import `in`.gopalpoddar.textspur.features.auth.signup.presentation.components.SignUpNameField
import `in`.gopalpoddar.textspur.features.auth.signup.presentation.components.SignUpPasswordField

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToHome()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { errorMessage ->
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.resetError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            SignUpNameField(
                value = name,
                onValueChange = { name = it },
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            SignUpEmailField(
                value = email,
                onValueChange = { email = it },
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            SignUpPasswordField(
                value = password,
                onValueChange = { password = it },
                onDone = { viewModel.signUp(name, email, password) },
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            SignUpButton(
                onClick = { viewModel.signUp(name, email, password) },
                isLoading = uiState.isLoading
            )

            Spacer(modifier = Modifier.weight(1f))

            LoginNavigationText(
                onNavigateToLogin = onNavigateToLogin,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
