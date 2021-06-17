package com.mcecelja.catalogue.ui.organization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.adapters.organization.OrganizationAdapter
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.databinding.FragmentOrganizationsBinding
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.services.ProductService
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.ui.catalogue.MainActivity
import com.mcecelja.catalogue.utils.AlertUtil
import com.mcecelja.catalogue.utils.RestUtil
import com.mcecelja.catalogue.utils.getFavouriteResourceForStatus
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrganizationFragment : Fragment() {

    private lateinit var organizationsBinding: FragmentOrganizationsBinding

    private val organizationViewModel by viewModel<OrganizationViewModel>()

    private val loadingViewModel by viewModel<LoadingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStackImmediate()
            }
        })

        organizationsBinding = FragmentOrganizationsBinding.inflate(inflater, container, false)

        arguments?.let {
            val product = it.getSerializable(PRODUCT) as ProductDTO
            setupRecyclerView(product)
            organizationViewModel.setProduct(product)
        }

        organizationsBinding.ivFavourite.setOnClickListener { changeFavouriteStatus(organizationViewModel.product.value!!) }

        loadingViewModel.loadingVisibility.observe(
            viewLifecycleOwner,
            { (activity as MainActivity).setupLoadingScreen(it) })

        organizationViewModel.product.observe(
            viewLifecycleOwner,
            { setupProduct(it) })

        return organizationsBinding.root
    }

    private fun setupProduct(product: ProductDTO) {
        organizationsBinding.tvProductName.text = product.name
        organizationsBinding.ivFavourite.setImageResource(getFavouriteResourceForStatus(product.currentUserFavourite))
    }

    private fun setupRecyclerView(product: ProductDTO) {
        organizationsBinding.rvOrganizations.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        organizationsBinding.rvOrganizations.adapter = OrganizationAdapter(product.organizations)
    }

    private fun changeFavouriteStatus(product: ProductDTO) {
        val apiCall =
            RestUtil.createService(
                ProductService::class.java, PreferenceManager.getPreference(
                    PreferenceEnum.TOKEN
                )
            ).changeFavouriteStatus(product.id)

        loadingViewModel.changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<ProductDTO>> {
            override fun onResponse(
                call: Call<ResponseMessage<ProductDTO>>,
                response: Response<ResponseMessage<ProductDTO>>
            ) {
                loadingViewModel.changeVisibility(View.INVISIBLE)

                if (response.isSuccessful) {

                    if (response.body()?.errorCode != null) {
                        AlertUtil.showAlertMessageForErrorCode(
                            response.body()!!.errorCode,
                            activity
                        )
                    } else {
                        response.body()?.payload?.let { organizationViewModel.setProduct(it) }
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<ProductDTO>>,
                t: Throwable
            ) {

                loadingViewModel.changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "Change favourite status failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
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