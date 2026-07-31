package `in`.gopalpoddar.textspur.features.chat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.gopalpoddar.textspur.features.chat.home.data.datasource.ChatRemoteDataSource
import `in`.gopalpoddar.textspur.features.chat.home.data.repository.ChatRepositoryImpl
import `in`.gopalpoddar.textspur.features.chat.home.domain.repository.ChatRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        remoteDataSource: ChatRemoteDataSource
    ): ChatRepository {
        return ChatRepositoryImpl(remoteDataSource)
    }
}
