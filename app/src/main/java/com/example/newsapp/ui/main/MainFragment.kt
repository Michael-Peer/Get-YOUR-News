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
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, MainViewModel.Factory(activity!!.application)).get(MainViewModel::class.java)

        network_button.setOnClickListener {
            viewModel.news.observe( viewLifecycleOwner, Observer {
                viewModel.extractData(it)
            }

            )
        }

        viewModel.totalResults.observe(viewLifecycleOwner, Observer {
            Log.i("MainFragment", "Observe total results: $it")
            Log.i("MainFragment", "----------------------------")
        })

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            Log.i("MainFragment", "Observe articles: $it")
            Log.i("MainFragment", "----------------------------")
        })
    }

}
