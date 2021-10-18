package com.etwicaksono.submission2

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersListViewModel(type: String, username: String? = null) : ViewModel() {

    class Factory(private val type: String, private val username: String? = null) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UsersListViewModel(type, username) as T
        }
    }

    private val _listUsers = MutableLiveData<List<ResponseUserItem>>()
    val listUser: LiveData<List<ResponseUserItem>> = _listUsers

    private val _followers = MutableLiveData<List<ResponseUserItem>>()
    val followers: LiveData<List<ResponseUserItem>> = _followers

    private val _following = MutableLiveData<List<ResponseUserItem>>()
    val following: LiveData<List<ResponseUserItem>> = _following

    private val _userData = MutableLiveData<ResponseUserDetail>()
    val userData: LiveData<ResponseUserDetail> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoading2 = MutableLiveData<Boolean>()
    val isLoading2: LiveData<Boolean> = _isLoading2

    private val _loadingSearch = MutableLiveData<Boolean>()
    val loadingSearch: LiveData<Boolean> = _loadingSearch

    companion object {
        private val TAG = UsersListViewModel::class.java.simpleName
        private val api = ApiConfig.getApiService()
    }

    init {
        when (type) {
            "all" -> getAllUsers()
            "detail user" -> getUserData(username!!)
            "followers" -> getFollowersData(username!!)
            "following" -> getFollowingData(username!!)
        }
    }

    fun searchUser(username: String) {
        _loadingSearch.value = true
        val client = api.searchUser(username)
        client.enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                _loadingSearch.value = false
                if (response.isSuccessful) {
                    response.body()?.items.let {
                        if (!it.isNullOrEmpty()) _listUsers.postValue(
                            it
                        )
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                _loadingSearch.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    private fun getFollowersData(username: String) {
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
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseUserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun getFollowingData(username: String) {
        _isLoading2.value = true
        val client = api.getUserFollowing(username)
        client.enqueue(object : Callback<List<ResponseUserItem>> {
            override fun onResponse(
                call: Call<List<ResponseUserItem>>,
                response: Response<List<ResponseUserItem>>
            ) {
                _isLoading2.value = false
                if (response.isSuccessful) {
                    _following.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Log.e(TAG, "errorBody: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseUserItem>>, t: Throwable) {
                _isLoading2.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Log.e(TAG, "cause: ${t.cause.toString()}")
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
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseUserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
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
                    Log.e(TAG, "onFailue: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseUserDetail>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
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
            result.value= networkInfo.isConnected
            return result
        }
    }
}