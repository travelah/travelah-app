package com.travelah.travelahapp.view.post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.travelah.travelahapp.data.remote.PostRepository
import com.travelah.travelahapp.data.remote.models.Comment
import com.travelah.travelahapp.ui.common.UiState
import com.travelah.travelahapp.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _postComments: MutableStateFlow<UiState<List<Comment>>> = MutableStateFlow(UiState.Loading)
    val postComments: StateFlow<UiState<List<Comment>>>
        get() = _postComments

    fun getMostLikedPost(token: String) = postRepository.getMostLikedPost(token)
    fun getAllPost(token: String, isMyPost: Boolean) =
        postRepository.getAllPost(token, isMyPost).cachedIn(viewModelScope)

    fun getPostDetail(token: String, id: Int) {
        viewModelScope.launch {
            postRepository.getPostDetail(token, id)
        }
    }

    fun getLatestDetail() = postRepository.getLiveDataPostDetail()
    fun getAllPostComment(token: String, id: Int) {
        viewModelScope.launch {
            postRepository.getAllCommentPost(token, id).catch {
                _postComments.value = UiState.Error(it.message.toString())
            }.collect { result ->
                when (result) {
                    is Result.Success -> {
                        _postComments.value = UiState.Success(result.data)
                    }
                    is Result.Loading -> {
                        _postComments.value = UiState.Loading
                    }
                    is Result.Error -> {
                        _postComments.value = UiState.Error(result.error)
                    }
                }
            }
        }
    }

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
}