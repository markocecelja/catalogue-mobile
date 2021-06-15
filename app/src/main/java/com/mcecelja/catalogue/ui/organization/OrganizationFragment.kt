package com.mcecelja.catalogue.ui.organization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.adapters.organization.OrganizationAdapter
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.databinding.FragmentOrganizationsBinding

class OrganizationFragment : Fragment() {

    private lateinit var organizationsBinding: FragmentOrganizationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        organizationsBinding = FragmentOrganizationsBinding.inflate(inflater, container, false)

        arguments?.let {
            val product = it.getSerializable(PRODUCT) as ProductDTO
            setupRecyclerView(product)

            organizationsBinding.tvProductName.text = product.name

            if(product.currentUserFavourite) {
                organizationsBinding.ivFavourite.setImageResource(R.drawable.favourite_added)
            } else {
                organizationsBinding.ivFavourite.setImageResource(R.drawable.favourite)
            }
        }

        return organizationsBinding.root
    }

    private fun setupRecyclerView(product: ProductDTO) {
        organizationsBinding.rvOrganizations.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        organizationsBinding.rvOrganizations.adapter = OrganizationAdapter(product.organizations)
    }

    companion object {
        const val TAG = "Organization"
        private const val PRODUCT = "Product"
        fun create(product: ProductDTO): OrganizationFragment {
            val args = Bundle()
            args.putSerializable(PRODUCT, product)
            val fragment = OrganizationFragment()
            fragment.arguments = args
            return fragment
        }
    }
}