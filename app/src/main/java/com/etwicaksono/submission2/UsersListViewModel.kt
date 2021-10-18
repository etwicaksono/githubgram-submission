package com.etwicaksono.submission2

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersListViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<List<ResponseUserItem>>()
    val listUsers: LiveData<List<ResponseUserItem>> = _listUsers

    private val _followers = MutableLiveData<List<ResponseUserItem>>()
    val followers: LiveData<List<ResponseUserItem>> = _followers

    private val _followings = MutableLiveData<List<ResponseUserItem>>()
    val followings: LiveData<List<ResponseUserItem>> = _followings

    private val _userData = MutableLiveData<ResponseUserDetail>()
    val userData: LiveData<ResponseUserDetail> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object {
        private val TAG = UsersListViewModel::class.java.simpleName
        private val api = ApiConfig.getApiService()
    }

    init {
        getAllUsers()
    }

    fun searchUser(username: String) {
        _isLoading.value = true
        val client = api.searchUser(username)
        client.enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.items.let {
                        if (!it.isNullOrEmpty()) _listUsers.postValue(
                            it
                        )
                    }
                } else {
                    Log.e(TAG, "searchUser onResponse failure: ${response.message()}")
                    _errorMessage.value = "searchUser onResponse failure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "searchUser onFailure: ${t.message.toString()}")
                _errorMessage.value = "searchUser onFailure: ${t.message.toString()}"
            }

        })
    }

    fun getFollowersData(username: String) {
        _isLoading.value = true
        val client = api.getUserFollowers(username)
        client.enqueue(object : Callback<List<ResponseUserItem>> {
            override fun onResponse(
                call: Call<List<ResponseUserItem>>,
                response: Response<List<ResponseUserItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followers.postValue(response.body())
                } else {
                    Log.e(TAG, "getFollowersData onResponse failure: ${response.message()}")
                    _errorMessage.value =
                        "getFollowersData onResponse failure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<ResponseUserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "getFollowersData onFailure: ${t.message.toString()}")
                _errorMessage.value = "getFollowersData onFailure: ${t.message.toString()}"
            }
        })
    }

    fun getFollowingData(username: String) {
        _isLoading.value = true
        val client = api.getUserFollowing(username)
        client.enqueue(object : Callback<List<ResponseUserItem>> {
            override fun onResponse(
                call: Call<List<ResponseUserItem>>,
                response: Response<List<ResponseUserItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followings.postValue(response.body())
                } else {
                    Log.e(TAG, "getFollowingData onResponse failure: ${response.message()}")
                    Log.e(TAG, "getFollowingData errorBody: ${response.errorBody()}")
                    _errorMessage.value =
                        "getFollowingData onResponse failure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<ResponseUserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "getFollowingData onFailure: ${t.message.toString()}")
                Log.e(TAG, "getFollowingData cause: ${t.cause.toString()}")
                _errorMessage.value = "getFollowingData onFailure: ${t.message.toString()}"
            }

        })
    }

    fun getAllUsers() {
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
                } else {
                    Log.e(TAG, "getAllUsers onResponse failure: ${response.message()}")
                    Log.e(TAG, "getAllUsers onResponse failure: ${response.errorBody()}")
                    _errorMessage.value = "getAllUsers onResponse failure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<ResponseUserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "getAllUsers onFailure: ${t.message.toString()}")
                _errorMessage.value = "getAllUsers onFailure: ${t.message.toString()}"
            }

        })
    }

    fun getUserData(username: String) {
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
                    Log.e(TAG, "getUserData onResponse failure: ${response.message()}")
                    _errorMessage.value = "getUserData onResponse failure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ResponseUserDetail>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "getUserData onFailure: ${t.message.toString()}")
                _errorMessage.value = "getUserData onFailure: ${t.message.toString()}"
            }

        })
    }

    fun hasInternet(context: Context): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        result.value = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return result
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return result
            when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> result.value =
                    true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> result.value =
                    true
                else -> result.value = false
            }
            return result
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return result
            @Suppress("DEPRECATION")
            result.value = networkInfo.isConnected
            return result
        }
    }
}