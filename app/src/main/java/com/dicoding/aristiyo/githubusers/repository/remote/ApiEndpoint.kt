package com.dicoding.aristiyo.githubusers.repository.remote

import com.dicoding.aristiyo.githubusers.BuildConfig
import com.dicoding.aristiyo.githubusers.BuildConfig.GITHUB_PERSONAL_TOKEN_ACCESS
import com.dicoding.aristiyo.githubusers.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndpoint {

    @Headers("Authorization: token ${BuildConfig.GITHUB_PERSONAL_TOKEN_ACCESS}")
    @GET("/search/users")
    suspend fun getUsersSearch(
        @Query("q") hint: String
    ): Response<SearchUsersResponse>

    @GET("/users/{username}")
    suspend fun getUserDetails(
        @Path("username") username: String
    ): Response<UserDetails>

    @GET("/users/{username}/followers")
    suspend fun getUserFollowersList(
        @Path("username") username: String
    ): Response<UserFollowersList>

    @GET("/users/{username}/following")
    suspend fun getUserFollowingsList(
        @Path("username") username: String
    ): Response<UserFollowingsList>
}