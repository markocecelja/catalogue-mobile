package com.mcecelja.catalogue.ui.organization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.adapters.organization.OrganizationAdapter
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.databinding.FragmentOrganizationsListBinding
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.listener.OrganizationItemClickListener
import com.mcecelja.catalogue.services.ProductService
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.ui.catalogue.MainActivity
import com.mcecelja.catalogue.ui.organization.details.OrganizationDetailsFragment
import com.mcecelja.catalogue.ui.product.ProductViewModel
import com.mcecelja.catalogue.utils.AlertUtil
import com.mcecelja.catalogue.utils.RestUtil
import com.mcecelja.catalogue.utils.getFavouriteResourceForStatus
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrganizationsListFragment : Fragment(), OrganizationItemClickListener {

    private lateinit var binding: FragmentOrganizationsListBinding

    private val organizationViewModel by viewModel<OrganizationViewModel>()

    private val loadingViewModel by viewModel<LoadingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrganizationsListBinding.inflate(inflater, container, false)

        arguments?.let {
            val product = it.getSerializable(PRODUCT) as ProductDTO
            setupRecyclerView(product)
            organizationViewModel.setProduct(product)
        }

        binding.ivFavourite.setOnClickListener { changeFavouriteStatus(organizationViewModel.product.value!!) }

        loadingViewModel.loadingVisibility.observe(
            viewLifecycleOwner,
            { (activity as MainActivity).setupLoadingScreen(it) })

        organizationViewModel.product.observe(
            viewLifecycleOwner,
            { setupProduct(it) })

        return binding.root
    }

    private fun setupProduct(product: ProductDTO) {
        binding.tvProductName.text = product.name
        binding.ivFavourite.setImageResource(getFavouriteResourceForStatus(product.currentUserFavourite))
    }

    private fun setupRecyclerView(product: ProductDTO) {
        binding.rvOrganizations.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvOrganizations.adapter = OrganizationAdapter(product.organizations, this)
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
                        response.body()?.payload?.let {
                            organizationViewModel.setProduct(it)
                            (requireActivity() as MainActivity).productViewModel.updateProduct(it)
                        }
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