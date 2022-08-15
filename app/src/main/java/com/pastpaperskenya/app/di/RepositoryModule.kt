package com.pastpaperskenya.app.di

import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepositoryImpl
import com.pastpaperskenya.app.business.repository.main.PaymentsRepository
import com.pastpaperskenya.app.business.repository.main.PaymentsRepositoryImpl
import com.pastpaperskenya.app.business.repository.main.UserDetailsRepository
import com.pastpaperskenya.app.business.repository.main.UserDetailsRepositoryImpl
import com.pastpaperskenya.app.business.services.auth.BaseAuthenticator
import com.pastpaperskenya.app.business.services.auth.UserService
import com.pastpaperskenya.app.business.services.payment.PaymentsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesAuthRepository(authenticator: BaseAuthenticator): FirebaseRepository{
        return FirebaseRepositoryImpl(authenticator)
    }

    @Provides
    @Singleton
    fun providesUserDetailsRepository(userService: UserService): UserDetailsRepository {
        return UserDetailsRepositoryImpl(userService)
    }

    @Provides
    fun providesPaymentRepository(paymentsService: PaymentsService): PaymentsRepository = PaymentsRepositoryImpl(paymentsService)
}