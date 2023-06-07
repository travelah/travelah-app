package com.travelah.travelahapp.view.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.travelah.travelahapp.data.remote.PostRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {
    fun getMostLikedPost(token: String) = postRepository.getMostLikedPost(token)
    fun getAllPost(token: String, isMyPost: Boolean) =
        postRepository.getAllPost(token, isMyPost).cachedIn(viewModelScope)
    fun likeDislikePost(token: String, id: Int, isLiked: Boolean) =
        postRepository.likeDislikePost(token, id, isLiked)
    fun createPost(
        photo: MultipartBody.Part,
        description: RequestBody,
        token: String,
        long: RequestBody,
        lat: RequestBody
    ) =
        postRepository.createPost(photo, description, token, long, lat)
}