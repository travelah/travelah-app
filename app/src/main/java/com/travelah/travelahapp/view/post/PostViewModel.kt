package com.travelah.travelahapp.view.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.travelah.travelahapp.data.remote.PostRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {
    fun getMostLikedPost(token: String) = postRepository.getMostLikedPost(token)
    fun getAllPost(token: String, isMyPost: Boolean) =
        postRepository.getAllPost(token, isMyPost).map {
            val postMap = mutableSetOf<Int>()
            it.filter { post ->
                if (postMap.contains(post.id)) {
                    false
                } else {
                    postMap.add(post.id)
                }
            }
        }.cachedIn(viewModelScope)

    fun getPostDetail(token: String, id: Int) {
        viewModelScope.launch {
            postRepository.getPostDetail(token, id)
        }
    }

    fun getLatestDetail() = postRepository.getLiveDataPostDetail()
    fun getAllPostComment(token: String, id: Int) =
        postRepository.getAllCommentPost(token, id).map {
            val commentMap = mutableSetOf<Int>()
            it.filter { comment ->
                if (commentMap.contains(comment.id)) {
                    false
                } else {
                    commentMap.add(comment.id)
                }
            }
        }.cachedIn(viewModelScope)

    fun likeDislikePost(token: String, id: Int, isLiked: Boolean) =
        postRepository.likeDislikePost(token, id, isLiked)

    fun createPost(
        photo: MultipartBody.Part,
        title: RequestBody,
        description: RequestBody,
        token: String,
        long: RequestBody,
        lat: RequestBody
    ) =
        postRepository.createPost(photo, title, description, token, long, lat)

    fun createPostComment(
        description: String,
        id: Int,
        token: String,
    ) = postRepository.createPostComment(description, id, token)
}