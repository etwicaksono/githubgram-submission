package com.etwicaksono.submission2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(private val username: String) : ViewModel() {

    class Factory(private val username:String):ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserDetailViewModel(username) as T
        }
    }

    private val _userData = MutableLiveData<ResponseUserDetail>()
    val userData: LiveData<ResponseUserDetail> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private val TAG = UserDetailViewModel::class.java.simpleName
        private val api = ApiConfig.getApiService()
    }

    init {
        getUserData(username)
    }

    private fun getUserData(username: String) {
        _isLoading.value = true
        val client = api.getUserDetail(username)
        client.enqueue(object : Callback<ResponseUserDetail> {
            override fun onResponse(
                call: Call<ResponseUserDetail>,
                response: Response<ResponseUserDetail>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userData.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailue: ${response.message().toString()}")
                }
            }

            override fun onFailure(call: Call<ResponseUserDetail>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}