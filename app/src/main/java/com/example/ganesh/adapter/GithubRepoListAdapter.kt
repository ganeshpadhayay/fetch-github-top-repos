package com.example.ganesh.adapter

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ganesh.R
import com.example.ganesh.data.GithubRepo

class GithubRepoListAdapter : RecyclerView.Adapter<GithubRepoListAdapter.GithubRepoViewHolder>() {

    lateinit var context: Activity
    var githubRepoList: List<GithubRepo> = ArrayList()
    lateinit var callBackHandler: CallBackHandler
    private var lastSelectedItem: Int = -1

    fun setData(
        context: Activity,
        githubRepoList: List<GithubRepo>,
        callBackHandler: CallBackHandler
    ) {
        this.context = context
        this.githubRepoList = githubRepoList
        this.callBackHandler = callBackHandler
    }

    override fun onBindViewHolder(holder: GithubRepoViewHolder, position: Int) {
        holder.bind(context, githubRepoList[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubRepoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GithubRepoViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = githubRepoList.size

    inner class GithubRepoViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.github_repo_item, parent, false)) {
        private var tvAuthor: TextView? = null
        private var tvRepo: TextView? = null
        private var tvDescription: TextView? = null
        private var tvLanguage: TextView? = null
        private var tvStars: TextView? = null
        private var tvForks: TextView? = null
        private var ivAuthorIcon: ImageView? = null
        private var lytExpandedState: LinearLayout? = null
        private var lytContainer: RelativeLayout? = null
        private var ivLanguageColor: ImageView? = null

        init {
            tvAuthor = itemView.findViewById(R.id.tv_author)
            tvRepo = itemView.findViewById(R.id.tv_repo)
            tvDescription = itemView.findViewById(R.id.tv_desc)
            tvLanguage = itemView.findViewById(R.id.tv_language)
            tvStars = itemView.findViewById(R.id.tv_stars)
            tvForks = itemView.findViewById(R.id.tv_forks)
            ivAuthorIcon = itemView.findViewById(R.id.iv_image)
            lytExpandedState = itemView.findViewById(R.id.lyt_expanded_state)
            lytContainer = itemView.findViewById(R.id.lyt_container)
            ivLanguageColor = itemView.findViewById(R.id.iv_language_color)
        }

        fun bind(
            context: Activity,
            githubRepo: GithubRepo,
            position: Int
        ) {
            tvAuthor?.text = githubRepo.author
            tvRepo?.text = githubRepo.name
            tvDescription?.text = githubRepo.description
            tvStars?.text = githubRepo.stars
            tvForks?.text = githubRepo.forks
            if (!TextUtils.isEmpty(githubRepo.language) && !TextUtils.isEmpty(githubRepo.languageColor)) {
                tvLanguage?.text = githubRepo.language
                try {
                    val gradientDrawable = ivLanguageColor?.background?.current as GradientDrawable
                    gradientDrawable.setColor(Color.parseColor(githubRepo.languageColor))
                } catch (e: Exception) {
                    //to handle the malformed color coming from the back end
                }
            } else {
                tvLanguage?.visibility = View.GONE
                ivLanguageColor?.visibility = View.GONE
            }

            ivAuthorIcon?.let {
                Glide.with(context)
                    .load(githubRepo.avatar)
                    .apply(RequestOptions.circleCropTransform())
                    .centerCrop()
                    .into(it)
            }

            if (githubRepo.isExpanded) {
                lytExpandedState?.visibility = View.VISIBLE
            } else {
                lytExpandedState?.visibility = View.GONE
            }

            lytContainer?.setOnClickListener {

                githubRepo.isExpanded = !githubRepo.isExpanded

                if (lastSelectedItem != -1 && lastSelectedItem != position) {
                    githubRepoList[lastSelectedItem].isExpanded = false
                }

                lastSelectedItem = position

                callBackHandler.onItemClick(position, githubRepoList)
            }
        }
    }
}

interface CallBackHandler {
    fun onItemClick(position: Int, updatedList: List<GithubRepo>)
}
