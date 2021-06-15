package com.mcecelja.catalogue.adapters.organization

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mcecelja.catalogue.data.dto.organization.OrganizationWithPriceDTO
import com.mcecelja.catalogue.databinding.ItemOrganizationBinding
import com.mcecelja.catalogue.utils.getStarForOrganizationPosition

class OrganizationViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(organization: OrganizationWithPriceDTO, position: Int) {
        val itemBinding = ItemOrganizationBinding.bind(itemView)
        itemBinding.tvStore.text = organization.name
        itemBinding.tvPrice.text = String.format("%s HRK", organization.price)

        itemBinding.ivStar.setImageResource(getStarForOrganizationPosition(position))
    }
}