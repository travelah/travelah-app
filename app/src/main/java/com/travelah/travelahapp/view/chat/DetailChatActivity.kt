package com.travelah.travelahapp.view.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import com.travelah.travelahapp.ui.screens.DetailChatScreen

class DetailChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContent {
            MaterialTheme {
                DetailChatScreen()
            }
        }
    }
}