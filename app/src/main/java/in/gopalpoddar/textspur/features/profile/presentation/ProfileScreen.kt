package `in`.gopalpoddar.textspur.features.profile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.gopalpoddar.textspur.features.profile.presentation.components.DeleteWarningDialog
import `in`.gopalpoddar.textspur.features.profile.presentation.components.LogoutDialog
import `in`.gopalpoddar.textspur.features.profile.presentation.components.ProfileActionsSection
import `in`.gopalpoddar.textspur.features.profile.presentation.components.ProfileHeader
import `in`.gopalpoddar.textspur.features.profile.presentation.components.ProfileInfoSection
import `in`.gopalpoddar.textspur.features.profile.presentation.components.ReauthenticateDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSplash: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.isLogoutSuccess, state.isDeleteAccountSuccess) {
        if (state.isLogoutSuccess || state.isDeleteAccountSuccess) {
            onNavigateToSplash()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.dismissError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                state.userProfile?.let { profile ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileHeader(userProfile = profile)
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        ProfileInfoSection(userProfile = profile)
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        ProfileActionsSection(
                            onLogoutClick = { viewModel.setShowLogoutDialog(true) },
                            onDeleteAccountClick = { viewModel.setShowDeleteWarningDialog(true) }
                        )
                    }
                }
            }
        }
    }

    if (state.showLogoutDialog) {
        LogoutDialog(
            onDismissRequest = { viewModel.setShowLogoutDialog(false) },
            onConfirm = { viewModel.logout() }
        )
    }

    if (state.showDeleteWarningDialog) {
        DeleteWarningDialog(
            onDismissRequest = { viewModel.setShowDeleteWarningDialog(false) },
            onConfirm = { viewModel.setShowReauthenticateDialog(true) }
        )
    }

    if (state.showReauthenticateDialog) {
        ReauthenticateDialog(
            onDismissRequest = { viewModel.setShowReauthenticateDialog(false) },
            onConfirm = { password -> viewModel.reauthenticateAndDelete(password) }
        )
    }
}
