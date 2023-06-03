package com.travelah.travelahapp.view.post

import androidx.lifecycle.ViewModel
import com.travelah.travelahapp.data.remote.PostRepository

class PostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {
    fun getMostLikedPost(token: String) = postRepository.getMostLikedPost(token)
}