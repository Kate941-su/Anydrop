package com.kaitokitaya.anydrop

import android.content.Context
import com.kaitokitaya.anydrop.network.socket.ClientSocketManager
import com.kaitokitaya.anydrop.network.NetworkService
import com.kaitokitaya.anydrop.network.socket.ServerSocketManager
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

    @Provides
    @Singleton
    fun provideServerSocketManager(): ServerSocketManager {
        return ServerSocketManager()
    }

    @Provides
    @Singleton
    fun provideClientSocketManager(): ClientSocketManager {
        return ClientSocketManager()
    }

}