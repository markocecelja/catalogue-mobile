package com.mcecelja.catalogue.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        viewPager = mainBinding.viewPager
        viewPager.adapter = PageAdapter(this)

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


}