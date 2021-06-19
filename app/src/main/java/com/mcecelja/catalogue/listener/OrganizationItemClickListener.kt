package com.mcecelja.catalogue.listener

import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO

interface OrganizationItemClickListener {

    fun onOrganizationClicked(organization: OrganizationDTO)
}