package com.mcecelja.catalogue.ui.catalogue

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.databinding.ActivityCatalogueBinding
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatalogueActivity : AppCompatActivity() {

    private lateinit var activityCatalogueBinding: ActivityCatalogueBinding

    private val catalogueViewModel by viewModel<CatalogueViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityCatalogueBinding = ActivityCatalogueBinding.inflate(layoutInflater)

        setContentView(activityCatalogueBinding.root)

        catalogueViewModel.setProducts(this, null)
        catalogueViewModel.setUserRatedOrganizations()
        catalogueViewModel.setUser()

        catalogueViewModel.loadingVisibility.observe(
            this,
            { setupLoadingScreen(it) })

        val token = PreferenceManager.getPreference(PreferenceEnum.TOKEN)

        if (token.isEmpty()) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        } else {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fl_fragmentContainer, TabFragment.create(), TabFragment.TAG)
                    .commit()
            }
        }
    }

    override fun onBackPressed() {
        if (catalogueViewModel.allowBack.value!!) {
            super.onBackPressed()
        }
    }

    fun setupLoadingScreen(visibility: Int) {
        activityCatalogueBinding.rlMain.visibility = visibility

        if (visibility == View.VISIBLE) {
            this.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            catalogueViewModel.setAllowBack(false)
        } else {
            this.window?.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            catalogueViewModel.setAllowBack(true)
        }
    }
}