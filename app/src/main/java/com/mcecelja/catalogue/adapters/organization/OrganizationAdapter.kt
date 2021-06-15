package com.mcecelja.catalogue.adapters.organization

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.dto.organization.OrganizationWithPriceDTO

class OrganizationAdapter (organizations: List<OrganizationWithPriceDTO>) : RecyclerView.Adapter<OrganizationViewHolder>() {

    private val organizations: MutableList<OrganizationWithPriceDTO> = mutableListOf()
    init {
        refreshData(organizations)
    }

    fun refreshData(organizations: List<OrganizationWithPriceDTO>) {
        this.organizations.clear()
        this.organizations.addAll(organizations)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_organization, parent, false)

        return OrganizationViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val organization = organizations[position]
        holder.bind(organization, position)

    }

    override fun getItemCount(): Int {
        return organizations.size
    }
}