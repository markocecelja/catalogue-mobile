package com.mcecelja.catalogue.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.databinding.ActivityMainBinding
import com.mcecelja.catalogue.ui.login.LoginActivity
import com.mcecelja.catalogue.ui.userprofile.UserProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val token = PreferenceManager.getPreference("Token")

        if (token.isEmpty()) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        } else {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.fl_fragmentContainer,
                        UserProfileFragment.create(token),
                        UserProfileFragment.TAG
                    )
                    .commit()
            }
        }
    }
}