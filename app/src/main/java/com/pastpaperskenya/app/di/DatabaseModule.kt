package com.pastpaperskenya.app.di

import android.content.Context
import com.pastpaperskenya.app.business.cache.AppDatabase
import com.pastpaperskenya.app.business.services.main.CategoryRemoteDataSource
import com.pastpaperskenya.app.business.services.main.CategoryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase = AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun providesCategoryDao(db: AppDatabase) = db.categoryDao()

    @Provides
    @Singleton
    fun providesCategoryRemoteDataSource(categoryService: CategoryService) = CategoryRemoteDataSource(categoryService)
}