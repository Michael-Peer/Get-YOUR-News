package com.example.newsapp.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import com.example.newsapp.R

/**
 * A simple [Fragment] subclass.
 */
class Favorites : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dots_menu, menu)
    }

}
