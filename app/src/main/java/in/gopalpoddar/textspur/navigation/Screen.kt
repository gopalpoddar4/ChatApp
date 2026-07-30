package `in`.gopalpoddar.textspur.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object ChatRoom : Screen("chatroom")
    object Profile : Screen("profile")
}
