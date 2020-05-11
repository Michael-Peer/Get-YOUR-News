package com.example.newsapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.newsapp.R
import kotlinx.android.synthetic.main.fragment_single_article.*


/**
 * A simple [Fragment] subclass.
 */
class SingleArticleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = SingleArticleFragmentArgs.fromBundle(
            requireArguments()
        )

        title_text.text = args.Article.title
        content_text.text = args.Article.content.toString()
    }

}
