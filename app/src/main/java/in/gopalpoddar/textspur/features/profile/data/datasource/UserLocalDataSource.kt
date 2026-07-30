package `in`.gopalpoddar.textspur.features.profile.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val KEY_UID = stringPreferencesKey("uid")
        val KEY_NAME = stringPreferencesKey("name")
        val KEY_EMAIL = stringPreferencesKey("email")
        val KEY_USERNAME = stringPreferencesKey("username")
    }

    suspend fun saveUserProfile(userProfile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[KEY_UID] = userProfile.uid
            preferences[KEY_NAME] = userProfile.name
            preferences[KEY_EMAIL] = userProfile.email
            preferences[KEY_USERNAME] = userProfile.username
        }
    }

    suspend fun getUserProfile(): UserProfile? {
        val preferences = dataStore.data.firstOrNull() ?: return null
        
        val uid = preferences[KEY_UID]
        val name = preferences[KEY_NAME]
        val email = preferences[KEY_EMAIL]
        val username = preferences[KEY_USERNAME]

        if (uid != null && name != null && email != null && username != null) {
            return UserProfile(uid = uid, name = name, email = email, username = username)
        }
        return null
    }

    suspend fun clearUserProfile() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_UID)
            preferences.remove(KEY_NAME)
            preferences.remove(KEY_EMAIL)
            preferences.remove(KEY_USERNAME)
        }
    }
}
