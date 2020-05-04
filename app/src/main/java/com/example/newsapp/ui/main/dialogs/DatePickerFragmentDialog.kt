package com.example.newsapp.ui.main.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.newsapp.ui.main.MainFragment
import java.util.*

class DatePickerFragmentDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(requireActivity(), parentFragment as MainFragment, year, month, day)
        val datePicker = datePickerDialog.datePicker

        val oneMonthAgo = calendar.clone() as Calendar
        oneMonthAgo.add(Calendar.DATE, -29)

        datePicker.maxDate = calendar.timeInMillis


        datePicker.minDate = oneMonthAgo.timeInMillis

        // Create a new instance of DatePickerDialog and return it
        return datePickerDialog
    }


}