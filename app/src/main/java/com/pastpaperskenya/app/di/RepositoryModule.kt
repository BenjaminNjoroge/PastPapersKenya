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
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitService
import com.pastpaperskenya.app.business.repository.auth.ServerCrudRepository
import com.pastpaperskenya.app.business.repository.auth.ServerCrudRepositoryImpl
import com.pastpaperskenya.app.business.repository.main.cart.CartRepository
import com.pastpaperskenya.app.business.repository.main.home.*
import com.pastpaperskenya.app.business.usecases.CartService
import com.pastpaperskenya.app.business.usecases.LocalUserService
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
    fun providesAuthRepository(authenticator: BaseAuthenticator): FirebaseRepository{
        return FirebaseRepositoryImpl(authenticator)
    }

    @Provides
    @Singleton
    fun providesServerCrud(retrofitService: RetrofitService): ServerCrudRepository{
        return ServerCrudRepositoryImpl(retrofitService)
    }

    @Provides
    @Singleton
    fun providesUserDetailsRepository(firestoreUserService: FirestoreUserService): UserDetailsRepository {
        return UserDetailsRepositoryImpl(firestoreUserService)
    }

    @Provides
    @Singleton
    fun providesProfileRepository(localUserService: LocalUserService): ProfileRepository =
         ProfileRepositoryImpl(localUserService)


    @Provides
    fun providesEditProfileRepository(localUserService: LocalUserService, firestoreUserService: FirestoreUserService, remoteDataSource: RemoteDataSource): EditProfileRepository =
        EditProfileRepositoryImpl(localUserService, firestoreUserService, remoteDataSource)


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
    fun providesDownloadsRepository(retrofitService: RetrofitService): DownloadsRepository =
        DownloadsRepository(retrofitService)

    @Provides
    fun providesCartRepository(cartService: CartService): CartRepository=
        CartRepository(cartService)

    @Provides
    fun providesProductsRepository(cartService: CartService, retrofitService: RetrofitService): ProductsRepository=
        ProductsRepository(cartService, retrofitService)

    @Provides
    fun providesMyOrdersRepository(remoteDataSource: RemoteDataSource, database: AppDatabase): MyOrdersRepository=
        MyOrdersRepository(remoteDataSource, database)

    @Provides
    fun providesMyOrderDetailsRepository(database: AppDatabase, remoteDatasource: RemoteDataSource): MyOrdersDetailRepository=
        MyOrdersDetailRepository(database, remoteDatasource)
}