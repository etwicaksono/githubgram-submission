package com.etwicaksono.submission2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersListViewModel(username: String) : ViewModel() {

    class Factory(private val username: String) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UsersListViewModel(username) as T
        }
    }

    private val _listUsers = MutableLiveData<List<ResponseUserItem>>()
    val userData: LiveData<List<ResponseUserItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private val TAG = UsersListViewModel::class.java.simpleName
        private val api = ApiConfig.getApiService()
    }

    init {
        getFollowersData(username)
    }

    private fun getFollowersData(username:String){
        _isLoading.value=true
        val client=api.getUserFollowers(username)
        client.enqueue(object:Callback<List<ResponseUserItem>>{
            override fun onResponse(
                call: Call<List<ResponseUserItem>>,
                response: Response<List<ResponseUserItem>>
            ) {
                _isLoading.value=false
                if(response.isSuccessful){
                    _listUsers.postValue(response.body())
                }else{
                    Log.e(TAG,"onFailure: ${response.message().toString()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseUserItem>>, t: Throwable) {
                _isLoading.value=false
                Log.e(TAG,"onFailure: ${t.message.toString()}")
            }

        })
    }
}