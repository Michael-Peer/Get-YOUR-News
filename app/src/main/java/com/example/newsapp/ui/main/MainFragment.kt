package com.example.newsapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.viewmodels.Factory
import com.example.newsapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        viewModel = ViewModelProvider(this, Factory(activity!!.application)).get(
            MainViewModel::class.java)


        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //        network_button.setOnClickListener {
        viewModel.news.observe( viewLifecycleOwner, Observer {
            it?.let {
                viewModel.extractData(it)
            }

        }

        )
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_button.setOnClickListener {
            Log.i("MainFragment", "Search button clicked")
            if(search_input.text.isNullOrEmpty())
                Toast.makeText(activity, "Input Error", Toast.LENGTH_LONG).show()
            else
                viewModel.onSearchButtonClicked(search_input.text.toString())
        }



        viewModel.totalResults.observe(viewLifecycleOwner, Observer {
            Log.i("MainFragment", "Observe total results: $it")
            Log.i("MainFragment", "----------------------------")
        })

        viewModel.articles.observe(viewLifecycleOwner, Observer {
            Log.i("MainFragment", "Observe articles: $it")
            Log.i("MainFragment", "----------------------------")
        })

//        viewModel.networkError.observe(viewLifecycleOwner, Observer {
//            if (it) onNetworkError()
//        })

        viewModel.isNetworkError.observe(viewLifecycleOwner, Observer {
            if(it)  Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show() //TODO: notify user

        })
    }


//      Method for displaying a Toast error message for network errors.

    private fun onNetworkError() {
        if(viewModel.isNetworkError.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
//            viewModel.onNetworkError()
        }
    }

}
