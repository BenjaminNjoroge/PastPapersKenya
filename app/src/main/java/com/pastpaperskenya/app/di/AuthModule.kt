package com.pastpaperskenya.app.di

import com.google.firebase.firestore.FirebaseFirestore
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
    fun providesFirestore()= FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthenticator(): BaseAuthenticator {
        return AuthenticatorImpl()
    }

}