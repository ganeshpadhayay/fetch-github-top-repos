package com.example.ganesh.network

import com.example.ganesh.network.api.GithubRepoApi

object ApiFactory {

    val githubRepoApi: GithubRepoApi =
        RetrofitFactory.retrofit("https://github-trending-api.now.sh/")
            .create(GithubRepoApi::class.java)
}