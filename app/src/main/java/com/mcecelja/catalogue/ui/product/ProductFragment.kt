package com.mcecelja.catalogue.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.adapters.product.ProductAdapter
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.databinding.FragmentProductsBinding
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.listener.ProductItemClickListener
import com.mcecelja.catalogue.services.ProductService
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.ui.catalogue.MainActivity
import com.mcecelja.catalogue.ui.organization.OrganizationFragment
import com.mcecelja.catalogue.utils.AlertUtil
import com.mcecelja.catalogue.utils.RestUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductFragment : Fragment(), ProductItemClickListener {

    private lateinit var productFragmentBinding: FragmentProductsBinding

    private val productViewModel by viewModel<ProductViewModel>()

    private val loadingViewModel by viewModel<LoadingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().supportFragmentManager.addOnBackStackChangedListener {
            fetchProducts(
                PreferenceManager.getPreference(PreferenceEnum.TOKEN),
                productFragmentBinding.etSearch.text.toString()
            )
        }

        productFragmentBinding = FragmentProductsBinding.inflate(inflater, container, false)

        setupRecyclerView()

        fetchProducts(PreferenceManager.getPreference(PreferenceEnum.TOKEN), null)

        productViewModel.products.observe(
            viewLifecycleOwner,
            { (productFragmentBinding.rvProducts.adapter as ProductAdapter).refreshData(it) })

        loadingViewModel.loadingVisibility.observe(
            viewLifecycleOwner,
            { (activity as MainActivity).setupLoadingScreen(it) })

        productFragmentBinding.etSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fetchProducts(
                    PreferenceManager.getPreference(PreferenceEnum.TOKEN),
                    productFragmentBinding.etSearch.text.toString()
                )
                return@OnEditorActionListener true
            }
            false
        })

        return productFragmentBinding.root
    }

    private fun setupRecyclerView() {
        productFragmentBinding.rvProducts.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        productFragmentBinding.rvProducts.adapter = ProductAdapter(mutableListOf(), this)
    }

    private fun fetchProducts(token: String, name: String?) {

        val apiCall =
            RestUtil.createService(ProductService::class.java, token).getProducts(name)

        loadingViewModel.changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<List<ProductDTO>>> {
            override fun onResponse(
                call: Call<ResponseMessage<List<ProductDTO>>>,
                response: Response<ResponseMessage<List<ProductDTO>>>
            ) {

                loadingViewModel.changeVisibility(View.INVISIBLE)

                if (response.isSuccessful) {

                    if (response.body()?.errorCode != null) {
                        AlertUtil.showAlertMessageForErrorCode(
                            response.body()!!.errorCode,
                            activity
                        )
                    } else {
                        productViewModel.setProducts(response.body()?.payload)
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<List<ProductDTO>>>,
                t: Throwable
            ) {

                loadingViewModel.changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "Get products failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    override fun onFavouriteClicked(position: Int) {
        productViewModel.changeFavouriteStatusForPosition(position, activity, loadingViewModel)
    }

    override fun onProductClicked(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_fragmentContainer,
                OrganizationFragment.create(productViewModel.products.value!![position]),
                OrganizationFragment.TAG
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