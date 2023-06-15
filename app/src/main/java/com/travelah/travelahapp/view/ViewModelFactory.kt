package com.travelah.travelahapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.travelah.travelahapp.data.remote.ChatRepository
import com.travelah.travelahapp.data.remote.PostRepository
import com.travelah.travelahapp.data.remote.UserRepository
import com.travelah.travelahapp.di.Injection
import com.travelah.travelahapp.view.chat.ChatViewModel
import com.travelah.travelahapp.view.editprofile.EditProfileViewModel
import com.travelah.travelahapp.view.login.LoginViewModel
import com.travelah.travelahapp.view.main.MainViewModel
import com.travelah.travelahapp.view.post.PostViewModel
import com.travelah.travelahapp.view.profile.ProfileViewModel
import com.travelah.travelahapp.view.register.RegisterViewModel
import com.travelah.travelahapp.view.splashscreen.SplashScreenActivity
import com.travelah.travelahapp.view.splashscreen.SplashScreenViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val chatRepository: ChatRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepository) as T
        }

        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        }

        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            return PostViewModel(postRepository) as T
        }

        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(chatRepository) as T
        }

        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            return SplashScreenViewModel(userRepository) as T
        }

        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository) as T
        }

        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(userRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.providePostRepository(context),
                    Injection.provideChatRepository(context)
                )
            }.also { instance = it }
    }
}