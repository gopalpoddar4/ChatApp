package `in`.gopalpoddar.textspur.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.gopalpoddar.textspur.features.auth.login.presentation.LoginScreen
import `in`.gopalpoddar.textspur.features.auth.signup.presentation.SignUpScreen
import `in`.gopalpoddar.textspur.features.chat.chatroom.presentation.ChatRoomScreen
import `in`.gopalpoddar.textspur.features.chat.home.presentation.HomeScreen
import `in`.gopalpoddar.textspur.features.profile.presentation.ProfileScreen

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
            LoginScreen(
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
            SignUpScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToChatRoom = {
                    navController.navigate(Screen.ChatRoom.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
        
        composable(route = Screen.ChatRoom.route) {
            ChatRoomScreen(
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
    }
}
