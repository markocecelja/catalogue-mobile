package com.mcecelja.catalogue.data

import android.content.Context
import com.mcecelja.catalogue.Catalogue

class PreferenceManager {

    companion object {
        private const val PREFS_FILE = "preferences"

        fun getPreference(key: String): String {
            return Catalogue.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
                .getString(key, "") ?: ""
        }

        fun savePreference(key: String, value: String?) {
            Catalogue.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
                .edit()
                .putString(key, value)
                .apply()
        }

        fun removePreference(key: String) {
            Catalogue.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
                .edit()
                .remove(key)
                .apply()
        }
    }
}