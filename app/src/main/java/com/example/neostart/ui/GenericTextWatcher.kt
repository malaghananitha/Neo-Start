package com.example.neostart.ui

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData

class GenericTextWatcher(private val liveData: MutableLiveData<String>?) : TextWatcher {

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
        // Optionally handle text before it changes
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
        // Optionally handle text as it changes
    }

    override fun afterTextChanged(editable: Editable?) {
        // Update LiveData with the new text value
        liveData?.value = editable.toString()
    }
}
