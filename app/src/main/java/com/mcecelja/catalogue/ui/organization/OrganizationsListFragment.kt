package com.mcecelja.catalogue.ui.organization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.adapters.organization.OrganizationAdapter
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.databinding.FragmentOrganizationsListBinding
import com.mcecelja.catalogue.listener.OrganizationItemClickListener
import com.mcecelja.catalogue.ui.catalogue.CatalogueViewModel
import com.mcecelja.catalogue.ui.catalogue.CatalogueActivity
import com.mcecelja.catalogue.ui.organization.details.OrganizationDetailsFragment
import com.mcecelja.catalogue.utils.getFavouriteResourceForStatus

class OrganizationsListFragment : Fragment(), OrganizationItemClickListener {

    private lateinit var binding: FragmentOrganizationsListBinding

    private lateinit var catalogueViewModel: CatalogueViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrganizationsListBinding.inflate(inflater, container, false)

        ViewModelProvider(requireActivity()).get(CatalogueViewModel::class.java).also {
            catalogueViewModel = it
        }

        setupRecyclerView()

        catalogueViewModel.selectedProduct.observe(
            viewLifecycleOwner,
            { setupProduct(it) })

        binding.ivFavourite.setOnClickListener {
            catalogueViewModel.changeFavouriteStatusForProduct(requireActivity(), catalogueViewModel.selectedProduct.value!!)
        }

        catalogueViewModel.selectedProductOrganizations.observe(
            viewLifecycleOwner,
            { (binding.rvOrganizations.adapter as OrganizationAdapter).refreshData(it) })

        catalogueViewModel.loadingVisibility.observe(
            viewLifecycleOwner,
            { (activity as CatalogueActivity).setupLoadingScreen(it) })

        return binding.root
    }

    private fun setupProduct(product: ProductDTO) {
        binding.tvProductName.text = product.name
        binding.ivFavourite.setImageResource(getFavouriteResourceForStatus(product.currentUserFavourite))
    }

    private fun setupRecyclerView() {
        binding.rvOrganizations.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvOrganizations.adapter = OrganizationAdapter(
            catalogueViewModel.selectedProductOrganizations.value ?: mutableListOf(), this
        )
    }

    override fun onOrganizationClicked(organization: OrganizationDTO) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_fragmentContainer,
                OrganizationDetailsFragment.create(organization),
                OrganizationDetailsFragment.TAG
            )
            .addToBackStack(TAG)
            .commit()
    }

    companion object {
        const val TAG = "Organizations list"
        fun create(): OrganizationsListFragment {
            return OrganizationsListFragment()
        }
    }
}