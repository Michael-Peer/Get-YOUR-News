package com.example.newsapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.models.Article
import kotlinx.android.synthetic.main.news_list_item.view.*


//class NewsListAdapter(private val articleList: ArrayList<Article>) :
//    RecyclerView.Adapter<NewsListAdapter.ArticleViewHolder>() {

//class NewsListAdapter(private val articleList:ArrayList<Article>): androidx.recyclerview.widget.ListAdapter<Article, NewsListAdapter.ArticleViewHolder>(ArticleDiffCallback()){
class NewsListAdapter(private val articleListener: OnArticleListener) :
    androidx.recyclerview.widget.ListAdapter<Article, NewsListAdapter.ArticleViewHolder>(
        ArticleDiffCallback()
    ) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsListAdapter.ArticleViewHolder {
        return ArticleViewHolder.from(parent, articleListener)
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


    class ArticleViewHolder private constructor(
        itemView: View,
        private var onArticleListener: OnArticleListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
//        private val itemText: TextView = itemView.cell_content_view_text
//        private val titleText: TextView = itemView.title_text
//        private val authorText: TextView = itemView.author_text
//        private val foldingCell = itemView.folding_cell
        private val  imageTitle = itemView.title_image


        init {
            itemView.setOnClickListener(this)
        }


        fun bind(article: Article) {
//            itemText.text = article.content.toString()
//            titleText.text = article.title
//            authorText.text = article.author

//            Picasso.get()
//                .load(article.urlToImage)
//                .placeholder(R.drawable.ic_launcher_background)
//                .fit()
//                .centerInside()
//                .into(imageTitle)
        }

        companion object {
            fun from(parent: ViewGroup, onArticleListener: OnArticleListener): ArticleViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.news_list_item, parent, false)
                return ArticleViewHolder(view, onArticleListener)
            }
        }

        override fun onClick(v: View?) {
            Log.i("onClick", "onClick: $adapterPosition")
            onArticleListener.onArticleClick(adapterPosition)
//            foldingCell.toggle(false)
        }
    }
}


interface OnArticleListener {
    fun onArticleClick(position: Int)
}

class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        //TODO: CHANGE TO MORE UNIQUE PARAMETER
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

}



