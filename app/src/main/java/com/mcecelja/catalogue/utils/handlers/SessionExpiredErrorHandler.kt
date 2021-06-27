package com.mcecelja.catalogue.utils.handlers

import android.app.Activity
import android.content.Intent
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.ui.login.LoginActivity
import com.mcecelja.catalogue.utils.RestUtil

class SessionExpiredErrorHandler: ErrorHandler {

    override fun handleError(activity: Activity) {
        PreferenceManager.removePreference(PreferenceEnum.TOKEN)
        RestUtil.clearHeader()
        val loginIntent = Intent(Catalogue.application, LoginActivity::class.java)
        activity.startActivity(loginIntent)
        activity.finish()
    }
}