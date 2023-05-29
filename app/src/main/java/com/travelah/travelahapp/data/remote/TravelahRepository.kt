package com.travelah.travelahapp.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.travelah.travelahapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.HttpException
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.remote.models.ProfileResponse
import com.travelah.travelahapp.data.remote.models.body.LoginBody
import com.travelah.travelahapp.utils.wrapEspressoIdlingResource

class TravelahRepository private constructor(
    private val apiService: ApiService,
    private val pref: SettingPreferences
) {

//    private fun convertErrorResponse(stringRes: String?): CommonResponse {
//        return Gson().fromJson(stringRes, CommonResponse::class.java)
//    }

    @OptIn(DelicateCoroutinesApi::class)
    fun postLogin(
        email: String,
        password: String,
    ): LiveData<Result<String>> = liveData {
        emit(Result.Loading)

        Log.d("abc", email)
        Log.d("abc", password)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.login(LoginBody(email, password))

                if (response.accessToken != null) {

                    pref.saveTokenSetting(response.accessToken)

                    val profile = apiService.profile("Bearer ${response.accessToken}")
                    pref.saveProfile(profile)
                    emit(Result.Success("Success"))
                } else {
                    emit(Result.Error("Error"))
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
//                        val jsonRes = convertErrorResponse(e.response()?.errorBody()?.string())
//                        val msg = jsonRes.message
                        emit(Result.Error("Error"))
                    }
                    else -> {
                        emit(Result.Error(e.message.toString()))
                    }
                }
            }
        }
    }

    suspend fun logout() {
        pref.saveProfile(ProfileResponse("", 0, "", ""))
        return pref.saveTokenSetting("")
    }

    fun getToken(): LiveData<String> {
       return pref.getTokenSetting().asLiveData()
    }

    companion object {
        @Volatile
        private var instance: TravelahRepository? = null
        fun getInstance(
            apiService: ApiService,
            pref: SettingPreferences
        ): TravelahRepository =
            instance ?: synchronized(this) {
                instance ?: TravelahRepository(apiService, pref)
            }.also { instance = it }
    }
}