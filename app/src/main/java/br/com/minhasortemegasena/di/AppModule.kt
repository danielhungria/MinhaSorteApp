package br.com.minhasortemegasena.di

import br.com.minhasortemegasena.retrofit.BASE_URL
import br.com.minhasortemegasena.retrofit.RetrofitService
import br.com.minhasortemegasena.retrofit.UnsafeOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton
import javax.net.ssl.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHTTPClient(): OkHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()

    @Provides
    @Singleton
    fun provideRetrofitService(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): RetrofitService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build().create(RetrofitService::class.java)

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

}