package com.pastpaperskenya.app.di

import android.app.Application
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepositoryImpl
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepositoryImpl
import com.pastpaperskenya.app.business.repository.main.downloads.DownloadsRepository
import com.pastpaperskenya.app.business.repository.main.home.HomeRepository
import com.pastpaperskenya.app.business.repository.main.home.PaymentsRepository
import com.pastpaperskenya.app.business.repository.main.home.PaymentsRepositoryImpl
import com.pastpaperskenya.app.business.repository.main.home.SubCategoryRepository
import com.pastpaperskenya.app.business.repository.main.profile.*
import com.pastpaperskenya.app.business.services.auth.BaseAuthenticator
import com.pastpaperskenya.app.business.services.auth.UserService
import com.pastpaperskenya.app.business.services.main.RetrofitService
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
    fun providesDataStoreRepository(application: Application): DataStoreRepository{
        return DataStoreRepositoryImpl(application)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(authenticator: BaseAuthenticator,retrofitService: RetrofitService): FirebaseRepository{
        return FirebaseRepositoryImpl(authenticator, retrofitService)
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
    fun providesHomeCategoryRepository(retrofitService: RetrofitService): HomeRepository =
         HomeRepository(retrofitService)

    @Provides
    fun providesSubCategoryRepository(retrofitService: RetrofitService): SubCategoryRepository =
        SubCategoryRepository(retrofitService)

    @Provides
    fun providesDownloadsRepository(retrofitService: RetrofitService, userService: UserService): DownloadsRepository =
        DownloadsRepository(retrofitService, userService)


}