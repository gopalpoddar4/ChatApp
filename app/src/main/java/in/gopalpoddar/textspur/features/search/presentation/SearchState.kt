package `in`.gopalpoddar.textspur.features.search.presentation

import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile

data class SearchState(
    val query: String = "",
    val users: List<UserProfile> = emptyList(),
    val isLoading: Boolean = false,
    val isChatCreating: Boolean = false,
    val error: String? = null
)
