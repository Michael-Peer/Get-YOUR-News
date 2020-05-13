package com.example.newsapp.ui.main

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.example.newsapp.R
import com.example.newsapp.Utils.ResultState
import com.example.newsapp.ui.main.dialogs.DatePickerFragmentDialog
import com.example.newsapp.viewmodels.Factory
import com.example.newsapp.viewmodels.MainViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_fragment.date_picker_start_button
import kotlinx.android.synthetic.main.main_fragment.folding_cell
import kotlinx.android.synthetic.main.main_fragment.free_query_edit_text
import kotlinx.android.synthetic.main.main_fragment.free_query_searh_button

import kotlinx.android.synthetic.main.main_fragment.search_button
import kotlinx.android.synthetic.main.main_fragment.search_input
import kotlinx.android.synthetic.main.main_fragment.sort_by_spinner
import kotlinx.android.synthetic.main.sample_layout.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class MainFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = MainFragment()
        const val SIGN_IN_CODE = 1000
    }

    private lateinit var viewModel: MainViewModel
    //can't be null
    private lateinit var selectedSortBy: String
    //could be null
    private var selectedStartDate: String? = null
    private var selectedEndDate: String? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        selectedSortBy =  resources.getStringArray(R.array.sort_by_array)[0]
        val storeOwner = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).getViewModelStoreOwner(R.id.nested_navigation)


        viewModel =  ViewModelProvider(storeOwner, Factory(requireActivity().application)).get(MainViewModel::class.java)


        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //        network_button.setOnClickListener {


//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




//        //sample
//        network_button.setOnClickListener {
//            it.findNavController().navigate(
//                R.id.sampleFragment
//            )
//        }

        folding_cell.setOnClickListener {
            folding_cell.toggle(false)
//            if (imageView3.visibility == View.VISIBLE)
//                imageView3.visibility = View.GONE
//            else
//                imageView3.visibility = View.VISIBLE

        }

        /**
         * Initialize spinner
         */
        val spinner = sort_by_spinner
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_by_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }


        /**
         * login flow
         */
//        login_button.setOnClickListener { launchLoginFlow() }
//        observeAuthState()

        /**
         * Set Click Listeners
         */
        search_button.setOnClickListener {
            //            Log.i("MainFragment", "Search button clicked")
            if (search_input.text.isNullOrEmpty())
                Toast.makeText(activity, "Input Error", Toast.LENGTH_LONG).show()
            else
                viewModel.onSearchButtonClicked(search_input.text.toString())
        }

        free_query_searh_button.setOnClickListener {
            //            Log.i("MainFragment", "query button clicked")
            if (free_query_edit_text.text.isNullOrEmpty() && selectedStartDate == null) {
                Toast.makeText(activity, "Insert search input and pick a date", Toast.LENGTH_LONG)
                    .show()
            } else if (free_query_edit_text.text.isNullOrEmpty())
                Toast.makeText(activity, "Input Error", Toast.LENGTH_LONG).show()
            else if (selectedStartDate == null) {
                Toast.makeText(activity, "Pick a Date", Toast.LENGTH_LONG).show()
            } else
                viewModel.onQueryButtonClicked(
                    free_query_edit_text.text.toString(),
                    selectedStartDate!!,
                    selectedSortBy
                )
        }

        //Date picker
        date_picker_start_button.setOnClickListener {
            openDatePickerDialog()
        }

        date_picker_end_button.setOnClickListener {
            openDatePickerDialog()
        }

        /**
         * Set  Observers
         */

        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultState.Success -> { /* show success in UI */
                }
                is ResultState.Failure -> {
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })


        viewModel.news.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.extractData(it)
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


    //launch dialog
    private fun openDatePickerDialog() {

        DatePickerFragmentDialog().show(childFragmentManager, "datePicker")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                //success
                Log.i(
                    "MainFragment",
                    "user success ${FirebaseAuth.getInstance().currentUser?.displayName}"
                )
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
            when (it) {
                MainViewModel.AuthState.AUTHENTICATED -> {
                    login_button.text = "Logout"

                    login_button.setOnClickListener {
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


    /**
     * Implement Interfaces
     */

    //date click
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.i("DatePickerFragmentYear", year.toString())
        Log.i("DatePickerFragmentMonth", month.toString())
        Log.i("DatePickerFragmentDay", dayOfMonth.toString())

        //TODO: REMOVE threeten imports WHEN UPDATING TO ANDROID STUDIO 4.0
        val date = LocalDate.of(year, month + 1, dayOfMonth)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = date.format(formatter)
        selectedStartDate = formattedDate.toString()

        Log.i("DatePickerFragment", formattedDate.toString())

    }


    //spinner click
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        Log.i("onItemSelected", "${parent!!.getItemAtPosition(position)}")
        selectedSortBy = parent!!.getItemAtPosition(position).toString()
        Log.i("onItemSelected", selectedSortBy)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dots_menu, menu)
    }
}
