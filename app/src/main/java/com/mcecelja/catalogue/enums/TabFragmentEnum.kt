package com.mcecelja.catalogue.enums

import androidx.fragment.app.Fragment
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.ui.product.ProductFragment
import com.mcecelja.catalogue.ui.userprofile.UserProfileFragment

enum class TabFragmentEnum(
    val position: Int,
    val fragment: Fragment,
    val title: String,
    val icon: Int
) {

    PRODUCT(
        0,
        ProductFragment.create(PreferenceManager.getPreference("Token")),
        "Proizvodi",
        R.drawable.products
    ),
    PROFILE(
        1,
        UserProfileFragment.create(PreferenceManager.getPreference("Token")),
        "Profil",
        R.drawable.person
    );

    companion object {
        fun getByPosition(position: Int): TabFragmentEnum? {
            for (tabFragmentEnum in values()) {
                if (tabFragmentEnum.position == position) {
                    return tabFragmentEnum
                }
            }

            return null
        }
    }
}