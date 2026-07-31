package `in`.gopalpoddar.textspur.features.search.domain.repository

import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchUsersByUsername(query: String): Flow<List<UserProfile>>
}
