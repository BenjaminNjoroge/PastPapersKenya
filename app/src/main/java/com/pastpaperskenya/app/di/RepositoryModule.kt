package com.pastpaperskenya.app.di

import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepositoryImpl
import com.pastpaperskenya.app.business.repository.main.home.HomeRepository
import com.pastpaperskenya.app.business.repository.main.home.PaymentsRepository
import com.pastpaperskenya.app.business.repository.main.home.PaymentsRepositoryImpl
import com.pastpaperskenya.app.business.repository.main.profile.*
import com.pastpaperskenya.app.business.services.auth.BaseAuthenticator
import com.pastpaperskenya.app.business.services.auth.UserService
import com.pastpaperskenya.app.business.services.main.CategoryService
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
    @Singleton
    fun providesProfileRepository(userService: UserService): ProfileRepository =
         ProfileRepositoryImpl(userService)


    fun providesEditProfileRepository(userService: UserService): EditProfileRepository =
        EditProfileRepositoryImpl(userService)


    @Provides
    fun providesPaymentRepository(paymentsService: PaymentsService): PaymentsRepository =
        PaymentsRepositoryImpl(paymentsService)

    @Provides
    fun providesHomeCategoryRepository(categoryService: CategoryService): HomeRepository =
         HomeRepository(categoryService)


}