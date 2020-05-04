package com.example.newsapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.viewmodels.NewsListViewModel
import com.example.newsapp.viewmodels.NewsListViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class NewsList : Fragment() {

    private lateinit var viewModel: NewsListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, NewsListViewModelFactory(requireActivity().application)).get(
            NewsListViewModel::class.java
        )
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.news.observe(viewLifecycleOwner, Observer {
            Log.i("NewsList", it.totalResults.toString())
        })
    }

}
