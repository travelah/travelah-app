package com.travelah.travelahapp.view.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.travelah.travelahapp.data.remote.PostRepository

class PostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {
    fun getMostLikedPost(token: String) = postRepository.getMostLikedPost(token)
    fun getAllPost(token: String, isMyPost: Boolean) =
        postRepository.getAllPost(token, isMyPost).cachedIn(viewModelScope)
}