package com.example.newsapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsListAdapter
import com.example.newsapp.adapters.OnArticleListener
import com.example.newsapp.viewmodels.Factory
import com.example.newsapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_news_list.*


class NewsList : Fragment(), OnArticleListener {

    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val storeOwner = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            .getViewModelStoreOwner(R.id.nested_navigation)


        viewModel = ViewModelProvider(storeOwner, Factory(requireActivity().application)).get(
            MainViewModel::class.java
        )
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NewsListAdapter(this)
        news_list_recycler_view.adapter = adapter
        news_list_recycler_view.layoutManager = LinearLayoutManager(requireContext())
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            //            Log.i("NewsList", it.totalResults.toString())
            it?.let {
                //TODO: inflate all items
                adapter.submitList(it) //ListAdapter function
            }
        })
    }

    override fun onArticleClick(position: Int) {
        Log.i("click","$position clicked")
        requireView().findNavController().navigate(
            NewsListDirections.actionNewsToSingleNews(
                viewModel.articles.value!![position]
            )
        )



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dots_menu, menu)
    }

}
