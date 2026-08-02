package `in`.gopalpoddar.textspur.features.profile.domain.model

import com.google.firebase.database.PropertyName

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val username: String = "",
    @get:PropertyName("isOnline")
    @set:PropertyName("isOnline")
    var isOnline: Boolean = false,
    @get:PropertyName("verified")
    @set:PropertyName("verified")
    var isVerified: Boolean = false
)
