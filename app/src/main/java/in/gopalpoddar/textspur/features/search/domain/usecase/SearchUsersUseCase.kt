package `in`.gopalpoddar.textspur.features.search.domain.usecase

import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import `in`.gopalpoddar.textspur.features.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(query: String, currentUserId: String?): Flow<List<UserProfile>> {
        return searchRepository.searchUsersByUsername(query).map { users ->
            users.filter { it.uid != currentUserId }
        }
    }
}
