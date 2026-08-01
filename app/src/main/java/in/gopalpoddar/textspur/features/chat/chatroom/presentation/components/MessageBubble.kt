package `in`.gopalpoddar.textspur.features.chat.chatroom.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageBubble(
    message: Message,
    isCurrentUser: Boolean
) {
    val backgroundColor = if (isCurrentUser) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val textColor = if (isCurrentUser) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (isCurrentUser) {
        RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
    }

    val timeString = if (message.timestamp > 0) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.format(Date(message.timestamp))
    } else {
        ""
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(shape)
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Text(
                text = message.message,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = timeString,
                color = textColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            )
        }
    }
}
