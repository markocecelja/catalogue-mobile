package com.mcecelja.catalogue.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mcecelja.catalogue.enums.TabFragmentEnum
import com.mcecelja.catalogue.ui.userprofile.UserProfileFragment

class PageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        TabFragmentEnum.getByPosition(position)?.let {
            return it.fragment
        }

        return UserProfileFragment()
    }
}