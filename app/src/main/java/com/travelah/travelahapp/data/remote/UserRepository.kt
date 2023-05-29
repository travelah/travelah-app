package com.travelah.travelahapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.travelah.travelahapp.data.remote.retrofit.ApiService
import retrofit2.HttpException
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.remote.models.ErrorResponse
import com.travelah.travelahapp.data.remote.models.body.LoginBody
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

                if (response.accessToken != null) {

                    pref.saveTokenSetting(response.accessToken)

                    val profile = apiService.profile("Bearer ${response.accessToken}")
                    pref.saveProfile(profile)
                    // TO DO: change if response from server is already consistent
                    emit(Result.Success("Success"))
                } else {
                    // TO DO: change if response from server is already consistent
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