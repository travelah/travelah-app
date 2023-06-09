package com.travelah.travelahapp.utils

import android.util.Log
import com.travelah.travelahapp.data.remote.retrofit.RetrofitConfig
import io.socket.client.IO
import io.socket.client.Socket

// reference: https://medium.com/@thushenarriyam/socket-io-connection-on-android-kotlin-to-node-js-server-71b218c160c9
object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(token: String) {
        try {
// "http://10.0.2.2:3000" is the network your Android emulator must use to join the localhost network on your computer
// "http://localhost:3000/" will not work
// If you want to use your physical phone you could use your ip address plus :3000
// This will allow your Android Emulator and physical device at your home to connect to the server
            mSocket = IO.socket(RetrofitConfig.BASE_URL)
        } catch (e: Exception) {
            Log.d("Socket: ", e.message.toString())
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