package com.mcecelja.catalogue.ui.login

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding

    private val loginViewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fl_fragmentContainer, LoginFragment.create(), LoginFragment.TAG)
                .commit()
        }

        loginViewModel.loadingVisibility.observe(this, { setupLoadingScreen(it) })
    }

    override fun onBackPressed() {
        if (loginViewModel.allowBack.value!!) {
            super.onBackPressed()
        }
    }

    fun setupLoadingScreen(visibility: Int) {
        loginBinding.rlLogin.visibility = visibility

        if (visibility == View.VISIBLE) {
            this.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            loginViewModel.setAllowBack(false)
        } else {
            this.window?.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            loginViewModel.setAllowBack(true)
        }
    }
}