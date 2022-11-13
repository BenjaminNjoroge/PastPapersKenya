package com.pastpaperskenya.app.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitService
import com.pastpaperskenya.app.business.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object NetworkModule {

    private const val PAYMENTS_URL= Constants.PAYMENTS_URL

    private const val BASE_URL= Constants.BASE_URL

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor()= HttpLoggingInterceptor()
        .apply {
            level= HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    @Named("payments")
    fun providePaymentsRetrofit(okHttpClient: OkHttpClient): Retrofit=
        Retrofit.Builder()
            .baseUrl(PAYMENTS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    @Named("base")
    fun provideBaseRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit=
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()


    @Singleton
    @Provides
    fun providesRetrofitService(@Named("base") retrofit: Retrofit): RetrofitService =
        retrofit.create(RetrofitService::class.java)



}