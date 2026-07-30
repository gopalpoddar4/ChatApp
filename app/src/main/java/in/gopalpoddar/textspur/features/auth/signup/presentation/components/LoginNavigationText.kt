package `in`.gopalpoddar.textspur.features.auth.signup.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun LoginNavigationText(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val annotatedString = buildAnnotatedString {
        append("Already have an account? ")
        withStyle(style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )) {
            append("Login")
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier
            .clickable(onClick = onNavigateToLogin)
            .padding(8.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}
