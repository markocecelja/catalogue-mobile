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
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.ui.catalogue.CatalogueViewModel
import com.mcecelja.catalogue.ui.catalogue.MainActivity
import com.mcecelja.catalogue.ui.organization.details.OrganizationDetailsFragment
import com.mcecelja.catalogue.utils.getFavouriteResourceForStatus

class OrganizationsListFragment : Fragment(), OrganizationItemClickListener {

    private lateinit var binding: FragmentOrganizationsListBinding

    private lateinit var catalogueViewModel: CatalogueViewModel

    private lateinit var loadingViewModel: LoadingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrganizationsListBinding.inflate(inflater, container, false)

        ViewModelProvider(requireActivity()).get(CatalogueViewModel::class.java).also {
            catalogueViewModel = it
        }

        ViewModelProvider(requireActivity()).get(LoadingViewModel::class.java).also {
            loadingViewModel = it
        }

        setupRecyclerView()

        arguments?.let {
            val product = it.getSerializable(PRODUCT) as ProductDTO
            setupProduct(product)

            binding.ivFavourite.setOnClickListener {
                catalogueViewModel.changeFavouriteStatusForProduct(
                    product,
                    activity,
                    loadingViewModel
                )
            }

            catalogueViewModel.products.observe(
                viewLifecycleOwner,
                { setupProduct(catalogueViewModel.getProductById(product.id)!!) })
        }

        catalogueViewModel.currentProductOrganizations.observe(
            viewLifecycleOwner,
            {  (binding.rvOrganizations.adapter as OrganizationAdapter).refreshData(it) })

        loadingViewModel.loadingVisibility.observe(
            viewLifecycleOwner,
            { (activity as MainActivity).setupLoadingScreen(it) })

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
        binding.rvOrganizations.adapter = OrganizationAdapter(catalogueViewModel.currentProductOrganizations.value ?: mutableListOf(), this)
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
        private const val PRODUCT = "Product"
        fun create(product: ProductDTO): OrganizationsListFragment {
            val args = Bundle()
            args.putSerializable(PRODUCT, product)
            val fragment = OrganizationsListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}