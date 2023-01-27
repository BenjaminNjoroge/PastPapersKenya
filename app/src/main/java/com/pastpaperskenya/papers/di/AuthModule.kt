package com.pastpaperskenya.papers.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pastpaperskenya.papers.business.datasources.remote.services.auth.AuthenticatorImpl
import com.pastpaperskenya.papers.business.datasources.remote.services.auth.BaseAuthenticator
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