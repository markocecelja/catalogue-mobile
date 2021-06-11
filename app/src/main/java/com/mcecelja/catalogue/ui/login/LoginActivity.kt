package com.mcecelja.catalogue.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.fl_fragmentContainer, LoginFragment.create(), LoginFragment.TAG)
                .commit()
        }
    }
}