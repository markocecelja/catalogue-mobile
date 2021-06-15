package com.mcecelja.catalogue.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mcecelja.catalogue.adapters.PageAdapter
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.databinding.ActivityMainBinding
import com.mcecelja.catalogue.enums.TabFragmentEnum
import com.mcecelja.catalogue.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        val pageAdapter = PageAdapter(this)
        pageAdapter.addFragment(TabFragmentEnum.PRODUCT)
        pageAdapter.addFragment(TabFragmentEnum.PROFILE)

        viewPager = mainBinding.viewPager
        viewPager.adapter = pageAdapter

        val tabLayout = mainBinding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            TabFragmentEnum.getByPosition(position)?.let {
                tab.text = it.title
                tab.setIcon(it.icon)
            }
        }.attach()

        setContentView(mainBinding.root)

        val token = PreferenceManager.getPreference("Token")

        if (token.isEmpty()) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun setupLoadingScreen(visibility: Int) {
        mainBinding.rlMain.visibility = visibility

        if(visibility == View.VISIBLE) {
            this.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            this.window?.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

}