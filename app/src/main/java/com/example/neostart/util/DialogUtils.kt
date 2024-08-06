package com.example.neostart.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.neostart.viewmodel.RegisterViewModel


object DialogUtils {

    fun showAlert(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String = "OK",
        positiveButtonAction: (() -> Unit)? = null,
        negativeButtonText: String? = null,
        negativeButtonAction: (() -> Unit)? = null
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButtonText) { dialog, _ ->
            dialog.dismiss()
            positiveButtonAction?.invoke()
        }
        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText) { dialog, _ ->
                dialog.dismiss()
                negativeButtonAction?.invoke()
            }
        }
        val alert = builder.create()
        alert.show()
    }
}