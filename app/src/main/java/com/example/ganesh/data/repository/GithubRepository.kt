package com.example.ganesh.data.repository

import com.example.ganesh.data.GithubRepo
import com.example.ganesh.network.api.GithubRepoApi

class GithubRepository(private val api: GithubRepoApi) : BaseRepository() {

    suspend fun getTrendingRepos(): MutableList<GithubRepo>? {

        val githubRepoResponse = safeApiCall(
            call = { api.getTrendingGithubRepos().await() },
            errorMessage = "Error Fetching Trending Repository"
        )

        return githubRepoResponse?.toMutableList();
    }

}
