package com.mcecelja.catalogue.ui.catalogue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mcecelja.catalogue.adapters.PageAdapter
import com.mcecelja.catalogue.databinding.TabFragmentBinding
import com.mcecelja.catalogue.enums.TabFragmentEnum

class TabFragment : Fragment() {

    private lateinit var tabFragmentBinding: TabFragmentBinding

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAffinity()
            }
        })

        tabFragmentBinding = TabFragmentBinding.inflate(inflater, container, false)

        return tabFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val pageAdapter = PageAdapter(requireActivity())
        pageAdapter.addFragment(TabFragmentEnum.PRODUCT)
        pageAdapter.addFragment(TabFragmentEnum.PROFILE)

        viewPager = tabFragmentBinding.viewPager
        viewPager.adapter = pageAdapter

        val tabLayout = tabFragmentBinding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            TabFragmentEnum.getByPosition(position)?.let {
                tab.text = it.title
                tab.setIcon(it.icon)
            }
        }.attach()
    }

    companion object {
        const val TAG = "Tab"
        fun create(): TabFragment {
            return TabFragment()
        }
    }
}