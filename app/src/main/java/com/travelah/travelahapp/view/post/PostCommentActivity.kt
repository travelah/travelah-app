package com.travelah.travelahapp.view.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import com.travelah.travelahapp.ui.screens.PostCommentScreen

class PostCommentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PostCommentScreen()
            }
        }
    }
}