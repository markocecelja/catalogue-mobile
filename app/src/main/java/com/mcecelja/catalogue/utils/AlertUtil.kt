package com.mcecelja.catalogue.utils

import android.app.Activity
import android.app.AlertDialog
import com.mcecelja.catalogue.enums.ErrorEnum

class AlertUtil {

    companion object {
        fun showAlertMessageForErrorCode(errorCode: String, activity: Activity) {
            val message = ErrorEnum.valueOf(errorCode).message
            val alertDialog: AlertDialog = activity.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton("OK"
                    ) { _, _ ->
                    }
                }
                builder.setMessage(message).create()
            }

            alertDialog.show()
        }
    }
}