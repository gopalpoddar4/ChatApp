package `in`.gopalpoddar.textspur.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.gopalpoddar.textspur.features.auth.login.data.datasource.FirebaseAuthDataSource
import `in`.gopalpoddar.textspur.features.auth.login.data.repository.FirebaseAuthRepository
import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideAuthDataSource(firebaseAuth: FirebaseAuth): FirebaseAuthDataSource {
        return FirebaseAuthDataSource(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(dataSource: FirebaseAuthDataSource): AuthRepository {
        return FirebaseAuthRepository(dataSource)
    }
}
