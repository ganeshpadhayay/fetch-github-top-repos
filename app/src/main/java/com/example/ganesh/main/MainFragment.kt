package com.example.ganesh.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ganesh.R
import com.example.ganesh.adapter.CallBackHandler
import com.example.ganesh.adapter.GithubRepoListAdapter
import com.example.ganesh.cache.UpdateCacheWorker
import com.example.ganesh.data.GithubRepo
import com.example.ganesh.utility.LocalPersistence
import com.example.ganesh.utility.ShimmerFrameLayout
import com.example.ganesh.utility.Utils
import java.util.concurrent.TimeUnit


class MainFragment : Fragment(), CallBackHandler {

    private var shimmerLoader: ShimmerFrameLayout? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: GithubRepoListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var mContext: Context? = null
    private var pullToRefresh: SwipeRefreshLayout? = null
    private var lytNoInternet: RelativeLayout? = null
    private var retryButton: TextView? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.main_fragment, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {

        shimmerLoader = view.findViewById(R.id.shimmer_loader) as ShimmerFrameLayout
        shimmerLoader?.startShimmerAnimation()

        lytNoInternet = view.findViewById(R.id.lyt_no_internet)
        retryButton = view.findViewById(R.id.button_retry)

        recyclerView = view.findViewById(R.id.my_recycler_view) as RecyclerView

        layoutManager = LinearLayoutManager(activity)
        adapter = GithubRepoListAdapter()
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = layoutManager

        pullToRefresh = view.findViewById(R.id.pull_to_refresh)
        pullToRefresh?.setOnRefreshListener {
            makeNetworkCall()
        }

        retryButton?.setOnClickListener {
            shimmerLoader?.visibility = View.VISIBLE
            shimmerLoader?.startShimmerAnimation()
            makeNetworkCall()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        //check if the data is present in local cache, if not then make the network call and update the cache
        val obj: Any? = LocalPersistence.readObjectFromFile(mContext, "github_data")
        if (obj is MutableList<*>) {
            updateUI(obj as MutableList<GithubRepo>, false)
        } else {
            makeNetworkCall()
        }

        mainViewModel.trendingGithubRepos.observe(this, Observer {
            if (it != null && it.size >= 0) {
                updateUI(it, true)
            }
        })

        //this is to handle configuration change
        if (mainViewModel.lastSelectedPosition != -1) {
            recyclerView?.smoothScrollToPosition(mainViewModel.lastSelectedPosition)
            adapter?.githubRepoList = mainViewModel.trendingGithubRepos.value as List<GithubRepo>
            adapter?.notifyDataSetChanged()
        }

    }

    private fun makeNetworkCall() {
        if (Utils.isNetworkAvailable(mContext)) {
            mainViewModel.fetchTrendingGithubRepos()
        } else {
            //stop shimmer
            shimmerLoader?.stopShimmerAnimation()
            shimmerLoader?.visibility = View.GONE

            //stop refreshing
            if (pullToRefresh?.isRefreshing!!)
                pullToRefresh?.isRefreshing = false

            lytNoInternet?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE

            Toast.makeText(mContext, "no internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(it: MutableList<GithubRepo>, needToHandleWorkManager: Boolean) {
        shimmerLoader?.stopShimmerAnimation()
        shimmerLoader?.visibility = View.GONE
        lytNoInternet?.visibility = View.GONE

        recyclerView?.visibility = View.VISIBLE
        activity?.let { it1 -> adapter?.setData(it1, it, this) }
        adapter?.notifyDataSetChanged()

        //update the local cache as well
        LocalPersistence.writeObjectToFile(mContext, it, "github_data")

        //stop refreshing
        if (pullToRefresh?.isRefreshing!!)
            pullToRefresh?.isRefreshing = false

        if (needToHandleWorkManager)
            handleWorkManager()

    }

    private fun handleWorkManager() {
        //cancel if any existing work manager is scheduled, if not then schedule a new one for after 2 hours
        mContext?.let { it ->
            if (WorkManager.getInstance(it).getWorkInfosByTag("cache_update_work_manager").get().size > 0) {
                //cancel the existing one and set a new one
                WorkManager.getInstance(it).cancelAllWorkByTag("cache_update_work_manager")
            }
            startWorkManager()
        }
    }

    private fun startWorkManager() {
        val updateCacheWorkRequest =
            PeriodicWorkRequestBuilder<UpdateCacheWorker>(2, TimeUnit.HOURS)
                .setInitialDelay(2, TimeUnit.HOURS)
                .addTag("cache_update_work_manager")
                .build()
        mContext?.let { WorkManager.getInstance(it).enqueue(updateCacheWorkRequest) }
    }

    override fun onItemClick(position: Int, updatedList: List<GithubRepo>) {
        mainViewModel.lastSelectedPosition = position
        mainViewModel.trendingGithubRepos.value = updatedList as MutableList<GithubRepo>
        adapter?.notifyDataSetChanged()
    }

}
