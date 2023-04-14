package br.com.minhasortemegasena.di

import android.content.Context
import androidx.room.Room
import br.com.minhasortemegasena.database.PalpiteDatabase
import br.com.minhasortemegasena.retrofit.BASE_URL
import br.com.minhasortemegasena.retrofit.RetrofitService
import br.com.minhasortemegasena.retrofit.UnsafeOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, PalpiteDatabase::class.java, "item.database")
        .build()

    @Provides
    fun providesItemDao(
        db: PalpiteDatabase
    ) = db.getPalpiteDao()

}