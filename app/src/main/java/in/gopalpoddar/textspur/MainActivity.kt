package `in`.gopalpoddar.textspur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import `in`.gopalpoddar.textspur.navigation.AppNavHost
import `in`.gopalpoddar.textspur.navigation.Screen
import `in`.gopalpoddar.textspur.ui.theme.TextSpurTheme
import `in`.gopalpoddar.textspur.features.auth.login.domain.usecase.CheckAuthStatusUseCase
import `in`.gopalpoddar.textspur.features.profile.domain.manager.PresenceManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var checkAuthStatusUseCase: CheckAuthStatusUseCase

    @Inject
    lateinit var presenceManager: PresenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextSpurTheme {
                val isAuthenticated = checkAuthStatusUseCase()
                if (isAuthenticated) {
                    ProcessLifecycleOwner.get().lifecycle.addObserver(presenceManager)
                }
                
                val startDestination = if (isAuthenticated) {
                    Screen.Home.route
                } else {
                    Screen.Login.route
                }
                
                AppNavHost(
                    modifier = Modifier.fillMaxSize(),
                    startDestination = startDestination
                )
            }
        }
    }
}