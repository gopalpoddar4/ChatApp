package `in`.gopalpoddar.textspur.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Search : Screen("search")
    object ChatRoom : Screen("chatroom/{chatId}") {
        fun createRoute(chatId: String) = "chatroom/$chatId"
    }
    object Profile : Screen("profile")
    object SaveProfile : Screen("saveprofile")
}
