package com.mcecelja.catalogue.data

import android.content.Context
import com.mcecelja.catalogue.Catalogue

class PreferenceManager {

    companion object {
        private const val PREFS_FILE = "preferences"

        fun savePreference(key: String, value: String?) {
            Catalogue.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
                .edit()
                .putString(key, value)
                .apply()
        }
    }
}