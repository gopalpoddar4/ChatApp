package `in`.gopalpoddar.textspur.features.chat.common.presentation.components

import androidx.compose.ui.graphics.Color

data class AvatarColorPair(
    val lightForeground: Color,
    val lightBackground: Color,
    val darkForeground: Color,
    val darkBackground: Color
)

object AvatarColorPalette {
    val colors = listOf(
        // 1. Purple
        AvatarColorPair(Color(0xFF6D28D9), Color(0xFFEDE9FE), Color(0xFFC4B5FD), Color(0xFF35205A)),
        // 2. Violet
        AvatarColorPair(Color(0xFF5B21B6), Color(0xFFDDD6FE), Color(0xFFDDD6FE), Color(0xFF3B2566)),
        // 3. Indigo
        AvatarColorPair(Color(0xFF3730A3), Color(0xFFE0E7FF), Color(0xFFC7D2FE), Color(0xFF252A59)),
        // 4. Blue
        AvatarColorPair(Color(0xFF1D4ED8), Color(0xFFDBEAFE), Color(0xFFBFDBFE), Color(0xFF1E3A5F)),
        // 5. Sky
        AvatarColorPair(Color(0xFF0369A1), Color(0xFFE0F2FE), Color(0xFFBAE6FD), Color(0xFF164E63)),
        // 6. Cyan
        AvatarColorPair(Color(0xFF0E7490), Color(0xFFCFFAFE), Color(0xFFA5F3FC), Color(0xFF164E63)),
        // 7. Teal
        AvatarColorPair(Color(0xFF0F766E), Color(0xFFCCFBF1), Color(0xFF99F6E4), Color(0xFF134E4A)),
        // 8. Emerald
        AvatarColorPair(Color(0xFF047857), Color(0xFFD1FAE5), Color(0xFFA7F3D0), Color(0xFF064E3B)),
        // 9. Green
        AvatarColorPair(Color(0xFF15803D), Color(0xFFDCFCE7), Color(0xFFBBF7D0), Color(0xFF14532D)),
        // 10. Lime
        AvatarColorPair(Color(0xFF4D7C0F), Color(0xFFECFCCB), Color(0xFFD9F99D), Color(0xFF365314)),
        // 11. Yellow
        AvatarColorPair(Color(0xFFA16207), Color(0xFFFEF9C3), Color(0xFFFEF08A), Color(0xFF713F12)),
        // 12. Amber
        AvatarColorPair(Color(0xFFB45309), Color(0xFFFEF3C7), Color(0xFFFDE68A), Color(0xFF78350F)),
        // 13. Orange
        AvatarColorPair(Color(0xFFC2410C), Color(0xFFFFEDD5), Color(0xFFFED7AA), Color(0xFF7C2D12)),
        // 14. Deep Orange
        AvatarColorPair(Color(0xFFC2410C), Color(0xFFFFEDD5), Color(0xFFFDBA74), Color(0xFF7C2D12)),
        // 15. Red
        AvatarColorPair(Color(0xFFB91C1C), Color(0xFFFEE2E2), Color(0xFFFCA5A5), Color(0xFF7F1D1D)),
        // 16. Rose
        AvatarColorPair(Color(0xFFBE123C), Color(0xFFFFE4E6), Color(0xFFFDA4AF), Color(0xFF881337)),
        // 17. Pink
        AvatarColorPair(Color(0xFFBE185D), Color(0xFFFCE7F3), Color(0xFFF9A8D4), Color(0xFF831843)),
        // 18. Fuchsia
        AvatarColorPair(Color(0xFFA21CAF), Color(0xFFFAE8FF), Color(0xFFF5D0FE), Color(0xFF701A75)),
        // 19. Magenta
        AvatarColorPair(Color(0xFF9D174D), Color(0xFFFCE7F3), Color(0xFFF9A8D4), Color(0xFF831843)),
        // 20. Plum
        AvatarColorPair(Color(0xFF86198F), Color(0xFFF5D0FE), Color(0xFFE9D5FF), Color(0xFF581C87)),
        // 21. Brown
        AvatarColorPair(Color(0xFF78350F), Color(0xFFFEF3C7), Color(0xFFFED7AA), Color(0xFF78350F)),
        // 22. Stone
        AvatarColorPair(Color(0xFF44403C), Color(0xFFE7E5E4), Color(0xFFD6D3D1), Color(0xFF44403C)),
        // 23. Slate
        AvatarColorPair(Color(0xFF334155), Color(0xFFE2E8F0), Color(0xFFCBD5E1), Color(0xFF334155)),
        // 24. Steel Blue
        AvatarColorPair(Color(0xFF1E40AF), Color(0xFFDBEAFE), Color(0xFFBFDBFE), Color(0xFF1E3A5F)),
        // 25. Deep Green
        AvatarColorPair(Color(0xFF166534), Color(0xFFDCFCE7), Color(0xFFBBF7D0), Color(0xFF14532D)),
        // 26. Deep Purple/Pink
        AvatarColorPair(Color(0xFF86198F), Color(0xFFF5D0FE), Color(0xFFE9D5FF), Color(0xFF581C87))
    )
}

fun getAvatarColor(userId: String, isDarkTheme: Boolean): Pair<Color, Color> {
    if (userId.isEmpty()) {
        val defaultPair = AvatarColorPalette.colors[0]
        return if (isDarkTheme) {
            Pair(defaultPair.darkForeground, defaultPair.darkBackground)
        } else {
            Pair(defaultPair.lightForeground, defaultPair.lightBackground)
        }
    }
    
    val index = (userId.hashCode() and 0x7FFFFFFF) % AvatarColorPalette.colors.size
    val colorPair = AvatarColorPalette.colors[index]
    
    return if (isDarkTheme) {
        Pair(colorPair.darkForeground, colorPair.darkBackground)
    } else {
        Pair(colorPair.lightForeground, colorPair.lightBackground)
    }
}
