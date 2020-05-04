package com.example.newsapp.ui.main

import android.app.Activity
import android.content.Intent
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
import com.example.newsapp.Utils.ResultState
import com.example.newsapp.viewmodels.Factory
import com.example.newsapp.viewmodels.MainViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        const val SIGN_IN_CODE = 1000
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this, Factory(requireActivity().application)).get(
            MainViewModel::class.java
        )



        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //        network_button.setOnClickListener {


//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //login flow
        login_button.setOnClickListener { launchLoginFlow() }

        observeAuthState()


        viewModel.news.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.extractData(it)
            }
        })

        search_button.setOnClickListener {
            Log.i("MainFragment", "Search button clicked")
            if (search_input.text.isNullOrEmpty())
                Toast.makeText(activity, "Input Error", Toast.LENGTH_LONG).show()
            else
                viewModel.onSearchButtonClicked(search_input.text.toString())
        }

        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultState.Success -> { /* show success in UI */
                }
                is ResultState.Failure -> {
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })



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
            if (it) Toast.makeText(
                activity,
                "Network Error",
                Toast.LENGTH_LONG
            ).show() //TODO: notify user

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SIGN_IN_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK){
                //success
                Log.i("MainFragment", "user success ${FirebaseAuth.getInstance().currentUser?.displayName}")
            } else {
                //failed
                Log.i("MainFragment", "user failed ${response?.error?.errorCode}")
            }
        }
    }

    private fun launchLoginFlow() {
        //sign in methods
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        //firebaseUI launch sign in flow
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            MainFragment.SIGN_IN_CODE
        )
    }


//      Method for displaying a Toast error message for network errors.

    private fun onNetworkError() {
        if (viewModel.isNetworkError.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
//            viewModel.onNetworkError()
        }
    }

    private fun observeAuthState() {
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            when(it) {
                MainViewModel.AuthState.AUTHENTICATED -> {
                    login_button.text = "Logout"

                    login_button.setOnClickListener {
                        //TODO: implement logging out
                        AuthUI.getInstance().signOut(requireContext())
                    }

                    //TODO: specific user staff
                }
                else -> {
                    //if no logged in user
                    login_button.text = "Login"
                    login_button.setOnClickListener { launchLoginFlow() }
                }
            }
        })
    }

}
