package com.pastpaperskenya.app.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepositoryImpl
import com.pastpaperskenya.app.business.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthenticator():BaseAuthenticator{
        return Authenticator()
    }

    @Provides
    @Singleton
    fun providesAuthRepository(authenticator: BaseAuthenticator): FirebaseRepository{
        return FirebaseRepositoryImpl(authenticator)
    }
}