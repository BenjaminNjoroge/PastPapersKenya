package com.pastpaperskenya.papers.di

import android.app.Application
import androidx.room.Room
import com.pastpaperskenya.papers.business.datasources.cache.AppDatabase
import com.pastpaperskenya.papers.business.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(app: Application): AppDatabase =
         Room.databaseBuilder(app, AppDatabase::class.java, "pastpapers_db")
             .fallbackToDestructiveMigration()
             .allowMainThreadQueries().build()


    @Provides
    @Singleton
    fun providesLocalUserService(database: AppDatabase): LocalUserService= LocalUserServiceImpl(database)

    @Provides
    @Singleton
    fun providesFirestoreUserService(): FirestoreUserService= FirestoreUserServiceImpl()

    @Provides
    fun providesCartService(database: AppDatabase): CartService= CartServiceImpl(database)

    @Provides
    fun providesWishlistService(database: AppDatabase): WishlistService= WishlistServiceImpl(database)

}