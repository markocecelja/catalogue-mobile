package com.mcecelja.catalogue.utils

import android.app.Activity
import android.app.AlertDialog
import com.mcecelja.catalogue.enums.ErrorEnum

class ErrorUtil {

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

        fun handleError(errorCode: String, activity: Activity) {
            val error = ErrorEnum.valueOf(errorCode)

            if(error.errorHandler != null) {
                error.errorHandler.handleError(activity)
            }
        }
    }
}