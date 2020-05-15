package com.example.newsapp.ui.main.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.newsapp.ui.main.MainFragment
import kotlinx.android.synthetic.main.main_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DatePickerFragmentDialog : DialogFragment() {

    private var isStarted: Boolean = true
    private var selectedStartDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isStarted = it.getBoolean(IS_STARTED)
            selectedStartDate = it.getString(SELECTED_START_DATE)
        }
    }

    companion object {
        const val TAG = "datePicker"
        private const val IS_STARTED = "isStarted"
        private const val SELECTED_START_DATE = "selectedStartDate"

        fun newInstance(isStarted: Boolean, selectedStartDate: String?) =
            DatePickerFragmentDialog().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_STARTED, isStarted)
                    putString(SELECTED_START_DATE, selectedStartDate)
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.i("DatePickerFragmentD", isStarted.toString())
        Log.i("DatePickerFragmentD", selectedStartDate.toString())

        // Use the current date as the default date in the picker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireActivity(), parentFragment as MainFragment, year, month, day)
        val datePicker = datePickerDialog.datePicker
        val oneMonthAgo = calendar.clone() as Calendar

        if (isStarted) {
            //starter date
            oneMonthAgo.add(Calendar.DATE, -29)
        } else {
            //end date
            Log.i("DatePickerFragmentDayOf", "selectedStartDate $selectedStartDate") //2020-05-05

            val dateList: ArrayList<Int> = extractDate(selectedStartDate)

            Log.i("DatePickerFragmentDayOf", "extractDate inside else ${dateList[0]}---${dateList[1]}---${dateList[2]}")

            oneMonthAgo.set(Calendar.DAY_OF_MONTH, dateList[0])
            oneMonthAgo.set(Calendar.MONTH, dateList[1] - 1)
            oneMonthAgo.set(Calendar.YEAR, dateList[2])
        }
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS")
        Log.i("DatePickerFragmentDayOf", "is started $isStarted") //2020-05-05

        Log.i("DatePickerFragmentDayOf", "max form ${formatter.format(calendar.timeInMillis)}") //2020-05-05
        Log.i("DatePickerFragmentDayOf", "min form ${formatter.format(oneMonthAgo.timeInMillis)}") //2020-05-05

        datePicker.maxDate = calendar.timeInMillis


        datePicker.minDate = oneMonthAgo.timeInMillis

        // Create a new instance of DatePickerDialog and return it
        return datePickerDialog
    }


    private fun extractDate(selectedStartDate: String?): ArrayList<Int> {
        val year = selectedStartDate?.substring(0,4)?.toInt()
        val month = selectedStartDate?.substring(5,7)?.toInt()
        val day = selectedStartDate?.substring(selectedStartDate.length - 2)?.toInt()
        val dateList = ArrayList<Int>()
        dateList.add(day!!)
        dateList.add(month!!)
        dateList.add(year!!)
        Log.i("DatePickerFragmentDayOf", "extractDate ${dateList[0]}---${dateList[1]}---${dateList[2]}")
        return dateList

    }


}