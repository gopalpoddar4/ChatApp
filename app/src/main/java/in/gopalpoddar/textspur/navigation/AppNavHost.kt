package `in`.gopalpoddar.textspur.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.gopalpoddar.textspur.features.auth.login.presentation.LoginScreen
import `in`.gopalpoddar.textspur.features.auth.login.presentation.LoginViewModel
import `in`.gopalpoddar.textspur.features.auth.signup.presentation.SignUpScreen
import `in`.gopalpoddar.textspur.features.chat.chatroom.presentation.ChatRoomScreen
import `in`.gopalpoddar.textspur.features.chat.home.presentation.HomeScreen
import `in`.gopalpoddar.textspur.features.search.presentation.SearchScreen
import `in`.gopalpoddar.textspur.features.profile.presentation.ProfileScreen
import `in`.gopalpoddar.textspur.features.profile.saveprofile.presentation.SaveProfileScreen
import `in`.gopalpoddar.textspur.features.profile.saveprofile.presentation.SaveProfileViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }
        
        composable(route = Screen.SignUp.route) {
            val signUpViewModel: `in`.gopalpoddar.textspur.features.auth.signup.presentation.SignUpViewModel = hiltViewModel()
            SignUpScreen(
                viewModel = signUpViewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack(Screen.Login.route, inclusive = false)
                },
                onNavigateToSaveProfile = {
                    navController.navigate(Screen.SaveProfile.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToChatRoom = { chatId ->
                    navController.navigate(Screen.ChatRoom.createRoute(chatId))
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToSearch = {
                    navController.navigate(Screen.Search.route)
                }
            )
        }
        
        composable(route = Screen.Search.route) {
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToChatRoom = { chatId ->
                    navController.navigate(Screen.ChatRoom.createRoute(chatId)) {
                        popUpTo(Screen.Search.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.ChatRoom.route) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatRoomScreen(
                chatId = chatId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(route = Screen.SaveProfile.route) {
            val saveProfileViewModel: SaveProfileViewModel = hiltViewModel()
            SaveProfileScreen(
                viewModel = saveProfileViewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SaveProfile.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
