package com.travelah.travelahapp.view.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.travelah.travelahapp.data.remote.PostRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class PostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {
    fun getMostLikedPost(token: String) = postRepository.getMostLikedPost(token)
    fun getAllPost(token: String, isMyPost: Boolean) =
        postRepository.getAllPost(token, isMyPost).cachedIn(viewModelScope)
    fun likeDislikePost(token: String, id: Int, isLiked: Boolean) =
        postRepository.likeDislikePost(token, id, isLiked)
}