package com.example.neostart.util


import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import com.example.neostart.R

object DropdownData {

    fun getEducationArray(context: Context): Array<String> {
        return arrayOf("Post Graduate", "Graduate", "HSC/Diploma", "SSC")
    }

    fun getYearOfPassingArray(context: Context): Array<String> {
        // Generate years dynamically if needed
        return (1963..2023).map { it.toString() }.toTypedArray()
    }

    fun getDesignationArray(context: Context): Array<String> {
        return arrayOf("Software Engineer", "Manager", "Analyst", "Developer")
    }

    fun getDomainArray(context: Context): Array<String> {
        return arrayOf("IT", "Finance", "Healthcare", "Education")
    }

    fun getStateArray(context: Context): Array<String> {
        return arrayOf("Maharashtra", "Gujarat", "Karnataka", "Madhya Pradesh", "Delhi", "Others")
    }

    fun setupAutoCompleteTextView(
        context: Context,
        autoCompleteTextView: AutoCompleteTextView,
        array: Array<String>,
        backgroundResId: Int
    ) {
        val adapter = ArrayAdapter(context, R.layout.spinner_item, array)
        autoCompleteTextView.setAdapter(adapter)

        val dropDownBackground = ContextCompat.getDrawable(context, backgroundResId)
        autoCompleteTextView.setDropDownBackgroundDrawable(dropDownBackground)

        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }
    }
}
