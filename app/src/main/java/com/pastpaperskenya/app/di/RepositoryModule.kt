package com.pastpaperskenya.app.di

import android.app.Application
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.app.business.repository.auth.FirebaseAuthRepository
import com.pastpaperskenya.app.business.repository.auth.FirebaseAuthRepositoryImpl
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepositoryImpl
import com.pastpaperskenya.app.business.repository.main.downloads.DownloadsRepository
import com.pastpaperskenya.app.business.repository.main.profile.*
import com.pastpaperskenya.app.business.datasources.remote.services.auth.BaseAuthenticator
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitApiService
import com.pastpaperskenya.app.business.repository.main.user.ServerCrudRepository
import com.pastpaperskenya.app.business.repository.main.user.ServerCrudRepositoryImpl
import com.pastpaperskenya.app.business.repository.main.cart.CartRepository
import com.pastpaperskenya.app.business.repository.main.home.*
import com.pastpaperskenya.app.business.repository.main.payment.PaymentRepository
import com.pastpaperskenya.app.business.repository.main.wishlist.WishlistRepository
import com.pastpaperskenya.app.business.usecases.CartService
import com.pastpaperskenya.app.business.usecases.LocalUserService
import com.pastpaperskenya.app.business.usecases.WishlistService
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
    fun providesPaymentRepository(retrofitApiService: RetrofitApiService, firestoreUserService: FirestoreUserService):PaymentRepository{
        return PaymentRepository(retrofitApiService, firestoreUserService)
    }

    @Provides
    @Singleton
    fun providesDataStoreRepository(application: Application): DataStoreRepository{
        return DataStoreRepositoryImpl(application)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(authenticator: BaseAuthenticator): FirebaseAuthRepository{
        return FirebaseAuthRepositoryImpl(authenticator)
    }

    @Provides
    @Singleton
    fun providesServerCrud(retrofitApiService: RetrofitApiService): ServerCrudRepository {
        return ServerCrudRepositoryImpl(retrofitApiService)
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
    fun providesEditProfileRepository(localUserService: LocalUserService,
                                      firestoreUserService: FirestoreUserService,
                                      retrofitApiService: RetrofitApiService,
                                      storage: FirebaseStorage,
                                      database: FirebaseFirestore
    ): EditProfileRepository =
        EditProfileRepositoryImpl(localUserService, firestoreUserService, retrofitApiService, storage, database)


    @Provides
    fun providesCategoryRemoteDataSource(retrofitApiService: RetrofitApiService): RemoteDataSource =
        RemoteDataSource(retrofitApiService)

    @Provides
    fun providesHomeCategoryRepository(remoteDataSource: RemoteDataSource, database: AppDatabase): HomeRepository =
         HomeRepository(remoteDataSource, database)

    @Provides
    fun providesSubCategoryRepository(remoteDataSource: RemoteDataSource, database: AppDatabase): SubCategoryRepository =
        SubCategoryRepository(remoteDataSource, database)

    @Provides
    fun providesDownloadsRepository(retrofitApiService: RetrofitApiService): DownloadsRepository =
        DownloadsRepository(retrofitApiService)

    @Provides
    fun providesCartRepository(cartService: CartService): CartRepository=
        CartRepository(cartService)

    @Provides
    fun providesWishlistRepository(wishlistService: WishlistService): WishlistRepository=
        WishlistRepository(wishlistService)

    @Provides
    fun providesProductsRepository(cartService: CartService, retrofitApiService: RetrofitApiService): ProductsRepository=
        ProductsRepository(cartService, retrofitApiService)

    @Provides
    fun providesMyOrdersRepository(remoteDataSource: RemoteDataSource, database: AppDatabase, retrofitApiService: RetrofitApiService): MyOrdersRepository=
        MyOrdersRepository(remoteDataSource, database, retrofitApiService)

    @Provides
    fun providesMyOrderDetailsRepository(database: AppDatabase, remoteDatasource: RemoteDataSource): MyOrdersDetailRepository=
        MyOrdersDetailRepository(database, remoteDatasource)
}