package com.dicoding.aristiyo.githubusers.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aristiyo.githubusers.models.SearchUsersResponse
import com.dicoding.aristiyo.githubusers.models.UserDetails
import com.dicoding.aristiyo.githubusers.models.UserFollowersList
import com.dicoding.aristiyo.githubusers.models.UserFollowingsList
import com.dicoding.aristiyo.githubusers.repository.remote.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Response

class ViewModelsWithKTX : ViewModel() {

    private val _dataSearchUserResponse = MutableLiveData<SearchUsersResponse>()
    val dataSearchUsersResponse: LiveData<SearchUsersResponse> = _dataSearchUserResponse

    private val _dataDetailUserResponse = MutableLiveData<UserDetails>()
    val dataDetailUserResponse: LiveData<UserDetails> = _dataDetailUserResponse

    private val _dataFollowersDetailUserResponse = MutableLiveData<UserFollowersList>()
    val dataFollowersDetailUsersUserResponse: LiveData<UserFollowersList> =
        _dataFollowersDetailUserResponse

    private val _dataFollowingsDetailUserResponse = MutableLiveData<UserFollowingsList>()
    val dataFollowingsDetailUsersUserResponse: LiveData<UserFollowingsList> =
        _dataFollowingsDetailUserResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun doGetSearchUser(hint: String) = viewModelScope.launch {
        _isLoading.value = true
        val searchUser = ApiConfig.getApiService().getUsersSearch(hint)
        searchUser.run {
            _isLoading.value = false
            if (this.isSuccessful) {
                _dataSearchUserResponse.value = this.body()
            } else {
                Log.e(javaClass.simpleName, "onFailure: ${this.message()}")
            }
        }
    }

    fun doGetUserDetail(username: String) = viewModelScope.launch {
        _isLoading.value = true
        val userDetail: Response<UserDetails> = ApiConfig.getApiService().getUserDetails(username)
        userDetail.run {
            _isLoading.value = false
            if (this.isSuccessful) {
                _dataDetailUserResponse.value = this.body()
            } else {
                Log.e(javaClass.simpleName, "onFailure: ${this.message()}")
            }
        }
    }

    fun doGetUserFollowers(username: String) = viewModelScope.launch {
        _isLoading.value = true
        val userFollowers: Response<UserFollowersList> =
            ApiConfig.getApiService().getUserFollowersList(username)
        userFollowers.run {
            _isLoading.value = false
            if (this.isSuccessful) {
                _dataFollowersDetailUserResponse.value = this.body()
            } else {
                Log.e(javaClass.simpleName, "onFailure: ${this.message()}")
            }
        }
    }

    fun doGetUserFollowings(username: String) = viewModelScope.launch {
        _isLoading.value = true
        val userFollowings: Response<UserFollowingsList> =
            ApiConfig.getApiService().getUserFollowingsList(username)
        userFollowings.run {
            _isLoading.value = false
            if (this.isSuccessful) {
                _dataFollowingsDetailUserResponse.value = this.body()
            } else {
                Log.e(javaClass.simpleName, "onFailure: ${this.message()}")
            }
        }
    }
}