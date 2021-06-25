package com.mcecelja.catalogue.ui.catalogue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.databinding.ActivityMainBinding
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private val catalogueViewModel by viewModel<CatalogueViewModel>()

    private val loadingViewModel by viewModel<LoadingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mainBinding.root)

        catalogueViewModel.setProducts(
            PreferenceManager.getPreference(PreferenceEnum.TOKEN),
            null,
            this,
            loadingViewModel
        )
        catalogueViewModel.setUserRatedOrganizations()
        catalogueViewModel.setUser()

        loadingViewModel.loadingVisibility.observe(
            this,
            { setupLoadingScreen(it) })

        val token = PreferenceManager.getPreference(PreferenceEnum.TOKEN)

        if (token.isEmpty()) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        } else {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fl_fragmentContainer, TabFragment.create(), TabFragment.TAG)
                    .commit()
            }
        }
    }

    fun setupLoadingScreen(visibility: Int) {
        mainBinding.rlMain.visibility = visibility

        if (visibility == View.VISIBLE) {
            this.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            this.window?.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }
}