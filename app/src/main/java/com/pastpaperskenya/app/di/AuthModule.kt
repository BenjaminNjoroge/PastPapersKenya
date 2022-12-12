package com.pastpaperskenya.app.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pastpaperskenya.app.business.datasources.remote.services.auth.AuthenticatorImpl
import com.pastpaperskenya.app.business.datasources.remote.services.auth.BaseAuthenticator
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import com.pastpaperskenya.app.business.usecases.FirestoreUserServiceImpl
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
    fun providesFirebaseFirestore()= Firebase.firestore

    @Provides
    @Singleton
    fun providesFirebaseStorage()= Firebase.storage

    @Provides
    @Singleton
    fun provideAuthenticator(): BaseAuthenticator {
        return AuthenticatorImpl()
    }

}