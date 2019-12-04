package com.example.ganesh.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ganesh.data.GithubRepo
import com.example.ganesh.data.repository.GithubRepository
import com.example.ganesh.network.ApiFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var lastSelectedPosition: Int = -1
    var isExpanded: Boolean = false

    private val repository: GithubRepository = GithubRepository(ApiFactory.githubRepoApi)

    val trendingGithubRepos = MutableLiveData<MutableList<GithubRepo>>()

    fun fetchTrendingGithubRepos() {
        uiScope.launch {
            val trendingRepos = repository.getTrendingRepos()
            trendingGithubRepos.postValue(trendingRepos)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
