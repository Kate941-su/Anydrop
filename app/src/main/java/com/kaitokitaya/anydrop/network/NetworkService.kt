package com.kaitokitaya.anydrop.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Enumeration

class NetworkService(private val context: Context) {
    fun getIPAddress(): String? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        if (networkCapabilities != null) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    // Device is connected to Wi-Fi
                    return getLocalIPAddress()
                }
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    // Device is connected to Ethernet
                    return getLocalIPAddress()
                }
            }
        }
        return null
    }

    private fun getLocalIPAddress(): String? {
        try {
            val interfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            for (networkInterface in interfaces) {
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress: InetAddress = addresses.nextElement()
                    // Filter out loopback addresses and IPv6 addresses
                    if (!inetAddress.isLoopbackAddress && inetAddress.address.size == 4) {
                        return inetAddress.hostAddress
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }
}