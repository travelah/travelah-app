package com.travelah.travelahapp.view.post

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import com.travelah.travelahapp.data.remote.models.Post
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainViewModel
import com.travelah.travelahapp.ui.screens.PostDetailScreen
import com.travelah.travelahapp.view.main.MainActivity
import com.travelah.travelahapp.data.Result

class PostDetailActivity : AppCompatActivity() {
    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }
    private val postViewModel: PostViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = ViewModelFactory.getInstance(this)

        supportActionBar?.hide()

        val id = intent.getIntExtra(EXTRA_ID, 0)
        val post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_POST, Post::class.java)
        } else {
            @Suppress("deprecation")
            intent.getParcelableExtra(EXTRA_POST)
        }

        mainViewModel.getToken().observe(this) { token ->
            mainViewModel.getProfile().observe(this) {profile ->
                if (token !== "" && profile != null) {
                    postViewModel.getPostDetail(token, id)
                    postViewModel.getLatestDetail().observe(this) { result ->
                        setContent {
                            MaterialTheme {
                                PostDetailScreen(token, profile, result, post, { onBackClick() }, postViewModel)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onBackClick() {
        finish()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_POST = "extra_post"
    }
}