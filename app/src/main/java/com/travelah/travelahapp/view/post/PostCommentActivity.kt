package com.travelah.travelahapp.view.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import com.travelah.travelahapp.ui.screens.PostCommentScreen
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainViewModel

class PostCommentActivity : AppCompatActivity() {
    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = ViewModelFactory.getInstance(this)

        val id = intent.getIntExtra(PostDetailActivity.EXTRA_ID, 0)

        mainViewModel.getToken().observe(this) { token ->
            setContent {
                MaterialTheme {
                    PostCommentScreen(id, token)
                }
            }
        }
    }
}