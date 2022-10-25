package com.pastpaperskenya.app.di

import android.app.Application
import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepositoryImpl
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepositoryImpl
import com.pastpaperskenya.app.business.repository.main.downloads.DownloadsRepository
import com.pastpaperskenya.app.business.repository.main.profile.*
import com.pastpaperskenya.app.business.datasources.remote.services.auth.BaseAuthenticator
import com.pastpaperskenya.app.business.datasources.remote.services.auth.UserService
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitService
import com.pastpaperskenya.app.business.datasources.remote.services.payment.PaymentsService
import com.pastpaperskenya.app.business.repository.main.cart.CartRepository
import com.pastpaperskenya.app.business.repository.main.home.*
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
    fun providesAuthRepository(authenticator: BaseAuthenticator, retrofitService: RetrofitService): FirebaseRepository{
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
    fun providesCategoryRemoteDataSource(retrofitService: RetrofitService): RemoteDataSource =
        RemoteDataSource(retrofitService)

    @Provides
    fun providesHomeCategoryRepository(remoteDataSource: RemoteDataSource, database: AppDatabase): HomeRepository =
         HomeRepository(remoteDataSource, database)

    @Provides
    fun providesSubCategoryRepository(remoteDataSource: RemoteDataSource, database: AppDatabase): SubCategoryRepository =
        SubCategoryRepository(remoteDataSource, database)

    @Provides
    fun providesDownloadsRepository(retrofitService: RetrofitService, userService: UserService): DownloadsRepository =
        DownloadsRepository(retrofitService, userService)

    @Provides
    fun providesCartRepository(database:AppDatabase): CartRepository=
        CartRepository(database)

    @Provides
    fun providesProductsRepository(database: AppDatabase, retrofitService: RetrofitService): ProductsRepository=
        ProductsRepository(database, retrofitService)

    @Provides
    fun providesMyOrdersRepository(retrofitService: RetrofitService, userService: UserService): MyOrdersRepository=
        MyOrdersRepository(retrofitService, userService)

    @Provides
    fun providesMyOrderDetailsRepository(database: AppDatabase, remoteDatasource: RemoteDataSource): MyOrdersDetailRepository=
        MyOrdersDetailRepository(database, remoteDatasource)
}