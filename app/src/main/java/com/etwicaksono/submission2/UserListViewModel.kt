package com.etwicaksono.submission2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<List<ResponseUserItem>>()
    val listUser: LiveData<List<ResponseUserItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private val TAG = UserListViewModel::class.java.simpleName
    }

    init {
        getAllUsers()
    }

    private fun getAllUsers() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllUsers()
        client.enqueue(object : Callback<List<ResponseUserItem>> {
            override fun onResponse(
                call: Call<List<ResponseUserItem>>,
                response: Response<List<ResponseUserItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUsers.postValue(response.body())
                }else{
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseUserItem>>, t: Throwable) {
                _isLoading.value=false
                Log.e(TAG,"onFailure: ${t.message.toString()}")
            }

        })
    }
}