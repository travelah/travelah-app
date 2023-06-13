package com.travelah.travelahapp.view.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import com.travelah.travelahapp.data.remote.models.response.ChatDetailResponse
import com.travelah.travelahapp.ui.screens.DetailChatScreen
import com.travelah.travelahapp.utils.SocketHandler
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainViewModel
import io.socket.emitter.Emitter
import org.json.JSONObject

class DetailChatActivity : AppCompatActivity() {
    private lateinit var factory: ViewModelFactory

    private val mainViewModel: MainViewModel by viewModels { factory }
    private val chatViewModel: ChatViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = ViewModelFactory.getInstance(this)

        supportActionBar?.hide()

        mainViewModel.getToken().observe(this) {token ->
            if (token != "") {
                SocketHandler.setSocket(token)
                SocketHandler.establishConnection()

                val payload = JSONObject()
                payload.put("groupId", 3)

                SocketHandler.getSocket().emit("getAllChatFromGroupChat", payload)

                // Listen for the response from the server
                SocketHandler.getSocket().on("chatRetrieved"
                ) { args ->
                    val response = args[0] as JSONObject
                    Log.d("response", response.toString())
                    val chats = ChatDetailResponse.fromJson(response)

                    runOnUiThread {
                        if (chats != null) {
                            setContent {
                                MaterialTheme {
                                    DetailChatScreen(ChatDetailResponse.fromJson(response))
                                }
                            }
                        }
                    }
                    // Handle the response received from the server
                    // ...
                }

                SocketHandler.getSocket().on("groupChatCreationError"
                ) { args ->
                    val response = args[0] as JSONObject
                    Log.e("error:", response.toString())
                    // Handle the response received from the server
                    // ...
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.closeConnection()
    }
}