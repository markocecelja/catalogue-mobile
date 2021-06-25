package com.mcecelja.catalogue.ui.catalogue

import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.data.dto.users.UserDTO
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.services.OrganizationService
import com.mcecelja.catalogue.services.ProductService
import com.mcecelja.catalogue.services.UserService
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.utils.AlertUtil
import com.mcecelja.catalogue.utils.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogueViewModel : ViewModel() {

    private val _products: MutableLiveData<List<ProductDTO>> = MutableLiveData<List<ProductDTO>>()
    val products: LiveData<List<ProductDTO>> = _products

    private val _user: MutableLiveData<UserDTO> = MutableLiveData<UserDTO>()
    val user: LiveData<UserDTO> = _user

    private val _userRatedOrganizations: MutableLiveData<List<OrganizationDTO>> =
        MutableLiveData<List<OrganizationDTO>>()
    val userRatedOrganizations: LiveData<List<OrganizationDTO>> = _userRatedOrganizations

    fun setProducts(
        token: String,
        name: String?,
        activity: FragmentActivity,
        loadingViewModel: LoadingViewModel
    ) {

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
                        _products.value = response.body()?.payload
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

    fun setOrganizations() {
        val apiCall =
            RestUtil.createService(
                OrganizationService::class.java,
                PreferenceManager.getPreference(PreferenceEnum.TOKEN)
            ).getCurrentUserRatedOrganizations()

        apiCall.enqueue(object : Callback<ResponseMessage<List<OrganizationDTO>>> {
            override fun onResponse(
                call: Call<ResponseMessage<List<OrganizationDTO>>>,
                response: Response<ResponseMessage<List<OrganizationDTO>>>
            ) {
                if (response.isSuccessful) {
                    _userRatedOrganizations.value = response.body()?.payload
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<List<OrganizationDTO>>>,
                t: Throwable
            ) {
                Toast.makeText(
                    Catalogue.application,
                    "Get favourites failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun setUser() {
        val apiCall =
            RestUtil.createService(
                UserService::class.java,
                PreferenceManager.getPreference(PreferenceEnum.TOKEN)
            ).getCurrentUserInfo()

        apiCall.enqueue(object : Callback<ResponseMessage<UserDTO>> {
            override fun onResponse(
                call: Call<ResponseMessage<UserDTO>>,
                response: Response<ResponseMessage<UserDTO>>
            ) {
                if (response.isSuccessful) {
                    _user.value = response.body()?.payload
                }
            }

            override fun onFailure(call: Call<ResponseMessage<UserDTO>>, t: Throwable) {
                Toast.makeText(
                    Catalogue.application,
                    "Get current user info failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun updateOrganization(organization: OrganizationDTO) {
        val organizations = mutableListOf<OrganizationDTO>()
        _userRatedOrganizations.value?.let { organizations.addAll(it) }

        val iterator: MutableIterator<OrganizationDTO> = organizations.iterator()
        while (iterator.hasNext()) {
            val o = iterator.next()
            if (o.id == organization.id) {
                if (organization.currentUserRating != null) {
                    organizations[organizations.indexOf(o)] = organization
                } else {
                    organizations.remove(o)
                }
            }
        }

        _userRatedOrganizations.value = organizations
    }

    fun changeFavouriteStatusForProduct(
        product: ProductDTO,
        activity: FragmentActivity?,
        loadingViewModel: LoadingViewModel
    ) {
        val products = mutableListOf<ProductDTO>()
        _products.value?.let { products.addAll(it) }

        val apiCall =
            RestUtil.createService(
                ProductService::class.java,
                PreferenceManager.getPreference(PreferenceEnum.TOKEN)
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

                            val iterator: MutableIterator<ProductDTO> = products.iterator()

                            while (iterator.hasNext()) {
                                val p = iterator.next()
                                if (p.id == product.id) {
                                    products[products.indexOf(p)] = it
                                }
                            }
                            _products.value = products
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

    fun getProductById(id: Long): ProductDTO? {
        for (product in _products.value!!) {
            if (product.id == id) {
                return product;
            }
        }

        return null;
    }
}