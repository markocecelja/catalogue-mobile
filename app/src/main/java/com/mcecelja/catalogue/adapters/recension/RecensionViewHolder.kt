package com.mcecelja.catalogue.adapters.recension

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.databinding.ItemRecensionBinding
import com.mcecelja.catalogue.listener.OrganizationItemClickListener

class RecensionViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(
        organization: OrganizationDTO,
        organizationItemClickListener: OrganizationItemClickListener
    ) {
        val itemBinding = ItemRecensionBinding.bind(itemView)
        itemBinding.tvStore.text = organization.name
        itemBinding.rb.rating = organization.currentUserRating!!.grade.toFloat()

        itemView.setOnClickListener { organizationItemClickListener.onOrganizationClicked(organization) }
    }
}