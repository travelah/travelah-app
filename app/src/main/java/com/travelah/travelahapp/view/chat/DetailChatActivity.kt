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
import com.travelah.travelahapp.view.post.PostDetailActivity
import org.json.JSONObject

class DetailChatActivity : AppCompatActivity() {
    private lateinit var factory: ViewModelFactory

    private val mainViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = ViewModelFactory.getInstance(this)

        supportActionBar?.hide()

        val id = intent.getIntExtra(EXTRA_ID, 0)

        mainViewModel.getToken().observe(this) {token ->
            if (token != "" && id != 0) {
                Log.d("abcde", token)
                SocketHandler.setSocket(token)
                SocketHandler.establishConnection()

                val payload = JSONObject()
                payload.put("groupId", id)
                payload.put("token", token)

                SocketHandler.getSocket().emit("getAllChatFromGroupChat", payload)

                // Listen for the response from the server
                SocketHandler.getSocket().on("chatRetrieved"
                ) { args ->
                    val response = args[0] as JSONObject
                    val chats = ChatDetailResponse.fromJson(response)

                    runOnUiThread {
                        if (chats != null) {
                            setContent {
                                MaterialTheme {
                                    DetailChatScreen(token, id, ChatDetailResponse.fromJson(response))
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

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}