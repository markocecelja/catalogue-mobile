package com.mcecelja.catalogue.adapters.organization

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.databinding.ItemOrganizationBinding
import com.mcecelja.catalogue.listener.OrganizationItemClickListener
import com.mcecelja.catalogue.utils.getStarForOrganizationPosition

class OrganizationViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(
        organization: OrganizationDTO,
        position: Int,
        organizationItemClickListener: OrganizationItemClickListener
    ) {
        val itemBinding = ItemOrganizationBinding.bind(itemView)
        itemBinding.tvStore.text = organization.name
        itemBinding.tvPrice.text = String.format("%.2f HRK", organization.price)

        itemBinding.ivStar.setImageResource(getStarForOrganizationPosition(position))
        itemBinding.rb.rating = organization.averageRating

        itemView.setOnClickListener { organizationItemClickListener.onOrganizationClicked(organization) }
    }
}