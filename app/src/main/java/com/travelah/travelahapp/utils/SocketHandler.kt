package com.travelah.travelahapp.utils

import com.travelah.travelahapp.data.remote.retrofit.RetrofitConfig
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(token: String) {
        try {
// "http://10.0.2.2:3000" is the network your Android emulator must use to join the localhost network on your computer
// "http://localhost:3000/" will not work
// If you want to use your physical phone you could use your ip address plus :3000
// This will allow your Android Emulator and physical device at your home to connect to the server
            val options = IO.Options()
            options.callFactory = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .addHeader("authorization", "Bearer $token")
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()

            mSocket = IO.socket("${RetrofitConfig.BASE_URL}:3000", options)
        } catch (_: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}