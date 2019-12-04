package com.example.ganesh.cache

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.ganesh.data.repository.GithubRepository
import com.example.ganesh.network.ApiFactory
import com.example.ganesh.utility.LocalPersistence
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/***
 * this class will update the cache after 2 hours of last update
 */
class UpdateCacheWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository: GithubRepository = GithubRepository(ApiFactory.githubRepoApi)

    override fun doWork(): Result {
        val appContext = applicationContext

        uiScope.launch {
            val trendingRepos = repository.getTrendingRepos()

            //update the local cache
            if (trendingRepos != null && trendingRepos.size > 0) {
                LocalPersistence.writeObjectToFile(appContext, trendingRepos, "github_data")
            }

        }
        return try {
            Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }

    }

}