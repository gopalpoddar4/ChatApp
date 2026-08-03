package `in`.gopalpoddar.textspur.features.chat.chatroom.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import `in`.gopalpoddar.textspur.R
import `in`.gopalpoddar.textspur.features.chat.common.presentation.components.UserAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    userId: String,
    name: String,
    username: String,
    isVerified: Boolean,
    isOnline: Boolean,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserAvatar(userId = userId, name = name, isOnline = isOnline)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (isVerified) {
                            Image(
                                painter = painterResource(id = R.drawable.verified_badge),
                                contentDescription = "Verified Badge",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    if (username.isNotEmpty()) {
                        Text(
                            text = "@$username",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}
