package com.mcecelja.catalogue.data

import android.content.Context
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.enums.PreferenceEnum

class PreferenceManager {

    companion object {
        private const val PREFS_FILE = "preferences"

        fun getPreference(preference: PreferenceEnum): String {
            return Catalogue.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
                .getString(preference.key, "") ?: ""
        }

        fun savePreference(preference: PreferenceEnum, value: String?) {
            Catalogue.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
                .edit()
                .putString(preference.key, value)
                .apply()
        }

        fun removePreference(preference: PreferenceEnum) {
            Catalogue.application.getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
            )
                .edit()
                .remove(preference.key)
                .apply()
        }
    }
}