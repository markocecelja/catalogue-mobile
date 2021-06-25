package com.mcecelja.catalogue.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.adapters.product.ProductAdapter
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.databinding.FragmentProductsBinding
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.listener.ProductItemClickListener
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.ui.organization.OrganizationsListFragment
import com.mcecelja.catalogue.ui.catalogue.CatalogueViewModel
import com.mcecelja.catalogue.ui.catalogue.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductFragment : Fragment(), ProductItemClickListener {

    private lateinit var productFragmentBinding: FragmentProductsBinding

    private lateinit var catalogueViewModel: CatalogueViewModel

    private val loadingViewModel by viewModel<LoadingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productFragmentBinding = FragmentProductsBinding.inflate(inflater, container, false)

        ViewModelProvider(requireActivity()).get(CatalogueViewModel::class.java).also {
            catalogueViewModel = it
        }

        setupRecyclerView()

        catalogueViewModel.products.observe(
            viewLifecycleOwner,
            { (productFragmentBinding.rvProducts.adapter as ProductAdapter).refreshData(it) })

        productFragmentBinding.etSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                catalogueViewModel.setProducts(
                    PreferenceManager.getPreference(PreferenceEnum.TOKEN),
                    productFragmentBinding.etSearch.text.toString(),
                    requireActivity(),
                    loadingViewModel
                )
                return@OnEditorActionListener true
            }
            false
        })

        loadingViewModel.loadingVisibility.observe(
            viewLifecycleOwner,
            { (activity as MainActivity).setupLoadingScreen(it) })

        return productFragmentBinding.root
    }

    private fun setupRecyclerView() {
        productFragmentBinding.rvProducts.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        productFragmentBinding.rvProducts.adapter = ProductAdapter(catalogueViewModel.products.value ?: mutableListOf(), this)
    }

    override fun onFavouriteClicked(product: ProductDTO) {
        catalogueViewModel.changeFavouriteStatusForProduct(
            product,
            activity,
            loadingViewModel
        )
    }

    override fun onProductClicked(product: ProductDTO) {
        catalogueViewModel.setOrganizationsByProductId(product.id, loadingViewModel)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_fragmentContainer,
                OrganizationsListFragment.create(product),
                OrganizationsListFragment.TAG
            )
            .addToBackStack(TAG)
            .commit()
    }

    companion object {
        const val TAG = "Products list"
        fun create(): ProductFragment {
            return ProductFragment()
        }
    }
}