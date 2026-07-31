package `in`.gopalpoddar.textspur.features.search.domain.usecase

import javax.inject.Inject

class CreateChatIdUseCase @Inject constructor() {
    operator fun invoke(currentUserUid: String, selectedUserUid: String): String {
        val sortedUids = listOf(currentUserUid, selectedUserUid).sorted()
        return "${sortedUids[0]}_${sortedUids[1]}"
    }
}
