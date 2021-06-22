package com.mcecelja.catalogue.adapters.recension

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.listener.OrganizationItemClickListener

class RecensionAdapter(
    organizations: List<OrganizationDTO>,
    private val organizationItemClickListener: OrganizationItemClickListener
) : RecyclerView.Adapter<RecensionViewHolder>() {

    private val organizations: MutableList<OrganizationDTO> = mutableListOf()

    init {
        refreshData(organizations)
    }

    fun refreshData(organizations: List<OrganizationDTO>) {
        this.organizations.clear()
        this.organizations.addAll(organizations)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecensionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recension, parent, false)

        return RecensionViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecensionViewHolder, position: Int) {
        val organization = organizations[position]
        holder.bind(organization, organizationItemClickListener)
    }

    override fun getItemCount(): Int {
        return organizations.size
    }
}