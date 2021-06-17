package com.mcecelja.catalogue.utils

import com.mcecelja.catalogue.R

fun getStarForOrganizationPosition(position: Int): Int {
    return when (position) {
        0 -> R.drawable.star_gold
        1 -> R.drawable.star_silver
        2 -> R.drawable.star_bronze
        else -> 0
    }
}

fun getFavouriteResourceForStatus(status: Boolean): Int {
    return when (status) {
        true -> R.drawable.favourite_added
        else -> R.drawable.favourite
    }
}