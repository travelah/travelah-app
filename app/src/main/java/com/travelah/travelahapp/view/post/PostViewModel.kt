package com.travelah.travelahapp.view.post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.remote.PostRepository
import com.travelah.travelahapp.data.remote.models.Post
import kotlinx.coroutines.launch

class PostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _mostLikedPost = MutableLiveData<Result<List<Post>>>()
    val mostLikedPost: LiveData<Result<List<Post>>>
        get() = _mostLikedPost

    fun getMostLikedPost(token: String) {
        viewModelScope.launch {
            postRepository.getMostLikedPost(token).observeForever { result ->
                when (result) {
                    is Result.Success -> {
                        _mostLikedPost.value = result
                    }
                    else -> {}
                }
            }
        }
    }
}