package com.mcecelja.catalogue.utils

import android.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.mcecelja.catalogue.enums.ErrorEnum

class AlertUtil {

    companion object {
        fun showAlertMessageForErrorCode(errorCode: String, activity: FragmentActivity?) {
            val message = ErrorEnum.valueOf(errorCode).message
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton("OK"
                    ) { _, _ ->
                    }
                }
                builder.setMessage(message).create()
            }

            alertDialog?.show()
        }
    }
}