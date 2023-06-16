package com.travelah.travelahapp.view.chat

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import com.travelah.travelahapp.data.remote.models.response.ChatDetailResponse
import com.travelah.travelahapp.ui.screens.DetailChatScreen
import com.travelah.travelahapp.utils.SocketHandler
import com.travelah.travelahapp.utils.SocketHandler.mSocket
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainViewModel
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.Transport
import org.json.JSONObject


class DetailChatActivity : AppCompatActivity() {
    private lateinit var factory: ViewModelFactory

    private val mainViewModel: MainViewModel by viewModels { factory }
    private val chatViewModel: ChatViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = ViewModelFactory.getInstance(this)

        supportActionBar?.hide()
        chatViewModel.changeId(intent.getIntExtra(EXTRA_ID, 0))

        mainViewModel.getToken().observe(this) { token ->

            if (token != "") {
                chatViewModel.idChat.observe(this) { id ->
                    SocketHandler.setSocket(token)
                    SocketHandler.establishConnection()

                    val onConnect: Emitter.Listener = Emitter.Listener { args ->
                        Log.d(TAG, "Socket Connected!")
                    }

                    val onConnectError: Emitter.Listener = Emitter.Listener { args ->
                        Log.d(TAG, "onConnectError")
                    }

                    val onDisconnect: Emitter.Listener = Emitter.Listener { args ->
                        Log.d(TAG, "onDisconnect")
                    }

                    try {
                        mSocket.on(Socket.EVENT_CONNECT, onConnect);
                        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
                    } catch (e: Exception) {
                        Log.d(TAG, "Error")
                    }


                    val payload = JSONObject()
                    payload.put("groupId", id)
                    payload.put("token", token)


                    mSocket.io().on(
                        Manager.EVENT_TRANSPORT
                    ) { args ->
                        val transport: Transport = args[0] as Transport
                        transport.on(Transport.EVENT_ERROR) { args ->
                            val e = args[0] as Exception
                            e.printStackTrace()
                            e.cause!!.printStackTrace()
                        }
                    }

                    SocketHandler.getSocket().emit("getAllChatFromGroupChat", payload)

                    // Listen for the response from the server
                    SocketHandler.getSocket().on(
                        "chatRetrieved"
                    ) { args ->
                        val response = args[0] as JSONObject
                        val chats = ChatDetailResponse.fromJson(response)

                        runOnUiThread {
                            if (chats != null) {
                                setContent {
                                    MaterialTheme {
                                        DetailChatScreen(
                                            token,
                                            id,
                                            ChatDetailResponse.fromJson(response),
                                            { onBackClick() }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    SocketHandler.getSocket().on(
                        "groupChatCreationError"
                    ) { args ->
                        val response = args[0] as JSONObject
                        Log.e("error:", response.toString())
                        // Handle the response received from the server
                        // ...
                    }
                }
            }
        }
    }

    private fun onBackClick() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.closeConnection()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val TAG = "Socket IO: "
    }
}