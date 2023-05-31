package com.travelah.travelahapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.travelah.travelahapp.data.remote.retrofit.ApiService
import retrofit2.HttpException
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.remote.models.ErrorResponse
import com.travelah.travelahapp.data.remote.models.Profile
import com.travelah.travelahapp.data.remote.models.body.LoginBody
import com.travelah.travelahapp.data.remote.models.body.RegisterBody
import com.travelah.travelahapp.utils.wrapEspressoIdlingResource

class UserRepository private constructor(
    private val apiService: ApiService,
    private val pref: SettingPreferences
) {

    private fun convertErrorResponse(stringRes: String?): ErrorResponse {
        return Gson().fromJson(stringRes, ErrorResponse::class.java)
    }

    fun postLogin(
        email: String,
        password: String,
    ): LiveData<Result<String>> = liveData {
        emit(Result.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.login(LoginBody(email, password))

                if (response.data != null) {

                    pref.saveTokenSetting(response.data.token)

                    val profile = apiService.profile("Bearer ${response.data.token}")
                    pref.saveProfile(profile)

                    emit(Result.Success(response.message))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val jsonRes = convertErrorResponse(e.response()?.errorBody()?.string())
                        val msg = jsonRes.message
                        emit(Result.Error(msg))
                    }
                    else -> {
                        emit(Result.Error(e.message.toString()))
                    }
                }
            }
        }
    }

    fun postRegister(
        email: String,
        password: String,
        fullName: String
    ) : LiveData<Result<String>> = liveData {
        emit(Result.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.register(RegisterBody(email, password, fullName))
                if (response.status == true) {
                    emit(Result.Success("Success"))
                } else {
                    emit(Result.Error("Error"))
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val jsonRes = convertErrorResponse(e.response()?.errorBody()?.string())
                        val msg = jsonRes.message
                        emit(Result.Error(msg))
                    }
                    else -> {
                        emit(Result.Error(e.message.toString()))
                    }
                }
            }
        }
    }

    suspend fun logout() {
        pref.clearProfile()
        return pref.saveTokenSetting("")
    }

    fun getToken(): LiveData<String> {
       return pref.getTokenSetting().asLiveData()
    }

    fun getProfile(): LiveData<Profile> {
        return pref.getProfileSetting().asLiveData()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            pref: SettingPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, pref)
            }.also { instance = it }
    }
}