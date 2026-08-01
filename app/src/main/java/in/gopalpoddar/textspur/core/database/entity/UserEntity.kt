package `in`.gopalpoddar.textspur.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val username: String,
    val isOnline: Boolean,
    val isVerified: Boolean
)
