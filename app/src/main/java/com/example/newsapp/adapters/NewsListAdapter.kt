package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.models.Article
import kotlinx.android.synthetic.main.news_list_item.view.*

//class NewsListAdapter(private val articleList: ArrayList<Article>) :
//    RecyclerView.Adapter<NewsListAdapter.ArticleViewHolder>() {

//class NewsListAdapter(private val articleList:ArrayList<Article>): androidx.recyclerview.widget.ListAdapter<Article, NewsListAdapter.ArticleViewHolder>(ArticleDiffCallback()){
class NewsListAdapter(): androidx.recyclerview.widget.ListAdapter<Article, NewsListAdapter.ArticleViewHolder>(ArticleDiffCallback()){



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsListAdapter.ArticleViewHolder {
        return ArticleViewHolder.from(parent)
    }

    /**
     * we don't need getItemCount when using ListAdapter instead of RecyclerView.Adapter
     */
//    override fun getItemCount(): Int {
//        return articleList.size
//    }

    override fun onBindViewHolder(holder: NewsListAdapter.ArticleViewHolder, position: Int) {
//        val article = articleList[position]
        val article = getItem(position) //List Adapater functionallty
        holder.bind(article)
    }


    class ArticleViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemText: TextView = itemView.text_item

        fun bind(article: Article) {
            itemText.text = article.title
        }

        companion object {
            fun from(parent: ViewGroup): ArticleViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.news_list_item, parent, false)
                return ArticleViewHolder(view)
            }
        }
    }
}

class ArticleDiffCallback: DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        //TODO: CHANGE TO MORE UNIQUE PARAMETER
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

}