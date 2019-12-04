package com.example.ganesh.network.api

import com.example.ganesh.data.GithubRepo
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface GithubRepoApi {

    @GET("repositories?language=&since=daily")
    fun getTrendingGithubRepos(): Deferred<Response<List<GithubRepo>>>
}