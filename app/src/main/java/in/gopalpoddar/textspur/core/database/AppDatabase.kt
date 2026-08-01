package `in`.gopalpoddar.textspur.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import `in`.gopalpoddar.textspur.core.database.dao.ChatDao
import `in`.gopalpoddar.textspur.core.database.dao.UserDao
import `in`.gopalpoddar.textspur.core.database.entity.ChatEntity
import `in`.gopalpoddar.textspur.core.database.entity.ChatParticipantCrossRef
import `in`.gopalpoddar.textspur.core.database.entity.UserEntity

@Database(
    entities = [UserEntity::class, ChatEntity::class, ChatParticipantCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
}
