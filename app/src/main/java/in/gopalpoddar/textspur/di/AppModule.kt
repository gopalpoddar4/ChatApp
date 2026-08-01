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

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.gopalpoddar.textspur.features.profile.data.datasource.UserLocalDataSource
import `in`.gopalpoddar.textspur.features.profile.data.datasource.UserRemoteDataSource
import `in`.gopalpoddar.textspur.features.profile.data.repository.UserRepositoryImpl
import `in`.gopalpoddar.textspur.features.profile.domain.repository.UserRepository

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

    @Provides
    @Singleton
    fun provideFirebaseDatabaseReference(): DatabaseReference {
        return Firebase.database.reference
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("user_prefs") }
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        localDataSource: UserLocalDataSource,
        remoteDataSource: UserRemoteDataSource,
        userDao: `in`.gopalpoddar.textspur.core.database.dao.UserDao
    ): UserRepository {
        return UserRepositoryImpl(localDataSource, remoteDataSource, userDao)
    }
}
