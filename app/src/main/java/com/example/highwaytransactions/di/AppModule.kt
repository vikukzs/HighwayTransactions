package com.example.highwaytransactions.di

import android.content.Context
import androidx.room.Room
import com.example.highwaytransactions.data.local.HighwayDao
import com.example.highwaytransactions.data.local.HighwayDatabase
import com.example.highwaytransactions.data.remote.HighwayApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideYettelApi(): HighwayApi {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/v1/highway/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient().newBuilder().addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
            )
            .build()
            .create(HighwayApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HighwayDatabase {
        return Room.databaseBuilder(
            context,
            HighwayDatabase::class.java,
            name = "highway_db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideHighwayDao(db: HighwayDatabase): HighwayDao = db.highwayDao()

}