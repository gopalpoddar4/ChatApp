package `in`.gopalpoddar.textspur.features.chat.common.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun UserAvatar(
    userId: String,
    name: String,
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    val initial = if (name.isNotBlank()) name.first().uppercase() else "?"
    
    val isDarkTheme = isSystemInDarkTheme()
    val (avatarForeground, avatarBackground) = getAvatarColor(userId = userId, isDarkTheme = isDarkTheme)
    
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(avatarBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                color = avatarForeground,
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        if (isOnline) {
            OnlineIndicator(
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
