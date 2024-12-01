package com.kaitokitaya.anydrop

import android.content.Context
import com.kaitokitaya.anydrop.network.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkService(@ApplicationContext appContext: Context): NetworkService {
        return NetworkService(context = appContext)
    }
}