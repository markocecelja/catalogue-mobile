package com.mcecelja.catalogue.ui.catalogue

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.data.dto.organization.RatingDTO
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.data.dto.users.UserDTO
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.services.OrganizationService
import com.mcecelja.catalogue.services.ProductService
import com.mcecelja.catalogue.services.UserService
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.utils.ErrorUtil
import com.mcecelja.catalogue.utils.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogueViewModel : LoadingViewModel() {

    private val _user: MutableLiveData<UserDTO> = MutableLiveData<UserDTO>()
    val user: LiveData<UserDTO> = _user

    private val _products: MutableLiveData<List<ProductDTO>> = MutableLiveData<List<ProductDTO>>()
    val products: LiveData<List<ProductDTO>> = _products

    private val _currentUserFavourites: MutableLiveData<List<ProductDTO>> =
        MutableLiveData<List<ProductDTO>>()
    val currentUserFavourites: LiveData<List<ProductDTO>> = _currentUserFavourites

    private val _selectedProduct: MutableLiveData<ProductDTO> = MutableLiveData<ProductDTO>()
    val selectedProduct: LiveData<ProductDTO> = _selectedProduct

    private val _selectedProductOrganizations: MutableLiveData<List<OrganizationDTO>> =
        MutableLiveData<List<OrganizationDTO>>()
    val selectedProductOrganizations: LiveData<List<OrganizationDTO>> =
        _selectedProductOrganizations

    private val _userRatedOrganizations: MutableLiveData<List<OrganizationDTO>> =
        MutableLiveData<List<OrganizationDTO>>()
    val userRatedOrganizations: LiveData<List<OrganizationDTO>> = _userRatedOrganizations

    fun setProducts(activity: Activity, name: String?) {

        val apiCall =
            RestUtil.createService(
                ProductService::class.java,
                PreferenceManager.getPreference(PreferenceEnum.TOKEN)
            ).getProducts(name)

        changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<List<ProductDTO>>> {
            override fun onResponse(
                call: Call<ResponseMessage<List<ProductDTO>>>,
                response: Response<ResponseMessage<List<ProductDTO>>>
            ) {

                changeVisibility(View.INVISIBLE)

                if (response.isSuccessful) {

                    if (response.body()?.errorCode != null) {
                        ErrorUtil.showAlertMessageForErrorCode(
                            response.body()!!.errorCode,
                            activity
                        )

                        ErrorUtil.handleError(response.body()!!.errorCode, activity)
                    } else {
                        _products.value = response.body()?.payload
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<List<ProductDTO>>>,
                t: Throwable
            ) {

                changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "Get products failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun setCurrentUserFavourites(activity: Activity) {

        val apiCall =
            RestUtil.createService(
                ProductService::class.java,
                PreferenceManager.getPreference(PreferenceEnum.TOKEN)
            ).getCurrentUserFavourites()

        changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<List<ProductDTO>>> {
            override fun onResponse(
                call: Call<ResponseMessage<List<ProductDTO>>>,
                response: Response<ResponseMessage<List<ProductDTO>>>
            ) {

                changeVisibility(View.INVISIBLE)

                if (response.isSuccessful) {

                    if (response.body()?.errorCode != null) {
                        ErrorUtil.showAlertMessageForErrorCode(
                            response.body()!!.errorCode,
                            activity
                        )

                        ErrorUtil.handleError(response.body()!!.errorCode, activity)
                    } else {
                        _currentUserFavourites.value = response.body()?.payload
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<List<ProductDTO>>>,
                t: Throwable
            ) {

                changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "Get favourites failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun setSelectedProduct(product: ProductDTO) {
        _selectedProduct.value = product
    }

    fun setOrganizationsByProductId(activity: Activity, productId: Long) {
        val apiCall =
            RestUtil.createService(
                OrganizationService::class.java,
                PreferenceManager.getPreference(PreferenceEnum.TOKEN)
            ).getOrganizations(productId)

        changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<List<OrganizationDTO>>> {
            override fun onResponse(
                call: Call<ResponseMessage<List<OrganizationDTO>>>,
                response: Response<ResponseMessage<List<OrganizationDTO>>>
            ) {
                changeVisibility(View.INVISIBLE)

                if (response.body()?.errorCode != null) {
                    ErrorUtil.showAlertMessageForErrorCode(
                        response.body()!!.errorCode,
                        activity
                    )

                    ErrorUtil.handleError(response.body()!!.errorCode, activity)
                } else if (response.isSuccessful) {
                    _selectedProductOrganizations.value = response.body()?.payload
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<List<OrganizationDTO>>>,
                t: Throwable
            ) {
                changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "Get favourites failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun setUserRatedOrganizations(activity: Activity) {

        val apiCall =
            RestUtil.createService(
                OrganizationService::class.java,
                PreferenceManager.getPreference(PreferenceEnum.TOKEN)
            ).getCurrentUserRatedOrganizations()

        changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<List<OrganizationDTO>>> {
            override fun onResponse(
                call: Call<ResponseMessage<List<OrganizationDTO>>>,
                response: Response<ResponseMessage<List<OrganizationDTO>>>
            ) {
                changeVisibility(View.INVISIBLE)

                if (response.body()?.errorCode != null) {
                    ErrorUtil.showAlertMessageForErrorCode(
                        response.body()!!.errorCode,
                        activity
                    )

                    ErrorUtil.handleError(response.body()!!.errorCode, activity)
                } else if (response.isSuccessful) {
                    _userRatedOrganizations.value = response.body()?.payload
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<List<OrganizationDTO>>>,
                t: Throwable
            ) {
                changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "Get favourites failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun setUser(activity: Activity) {

        val apiCall =
            RestUtil.createService(
                UserService::class.java,
                PreferenceManager.getPreference(PreferenceEnum.TOKEN)
            ).getCurrentUserInfo()

        changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<UserDTO>> {
            override fun onResponse(
                call: Call<ResponseMessage<UserDTO>>,
                response: Response<ResponseMessage<UserDTO>>
            ) {
                changeVisibility(View.INVISIBLE)

                if (response.body()?.errorCode != null) {
                    ErrorUtil.showAlertMessageForErrorCode(
                        response.body()!!.errorCode,
                        activity
                    )

                    ErrorUtil.handleError(response.body()!!.errorCode, activity)
                } else if (response.isSuccessful) {
                    _user.value = response.body()?.payload
                }
            }

            override fun onFailure(call: Call<ResponseMessage<UserDTO>>, t: Throwable) {
                changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "Get current user info failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun changeFavouriteStatusForProduct(activity: Activity, product: ProductDTO) {
        val apiCall =
            RestUtil.createService(
                ProductService::class.java,
                PreferenceManager.getPreference(PreferenceEnum.TOKEN)
            ).changeFavouriteStatus(product.id)

        changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<ProductDTO>> {
            override fun onResponse(
                call: Call<ResponseMessage<ProductDTO>>,
                response: Response<ResponseMessage<ProductDTO>>
            ) {
                changeVisibility(View.INVISIBLE)

                if (response.isSuccessful) {

                    if (response.body()?.errorCode != null) {
                        ErrorUtil.showAlertMessageForErrorCode(
                            response.body()!!.errorCode,
                            activity
                        )

                        ErrorUtil.handleError(response.body()!!.errorCode, activity)
                    } else {
                        response.body()?.payload?.let {
                            _selectedProduct.value = it
                        }
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<ProductDTO>>,
                t: Throwable
            ) {

                changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "Change favourite status failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun rateOrganization(activity: Activity, organization: OrganizationDTO, grade: Int) {

        val apiCall =
            RestUtil.createService(
                OrganizationService::class.java, PreferenceManager.getPreference(
                    PreferenceEnum.TOKEN
                )
            ).rateOrganization(organization.id, RatingDTO(grade))

        changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<OrganizationDTO>> {
            override fun onResponse(
                call: Call<ResponseMessage<OrganizationDTO>>,
                response: Response<ResponseMessage<OrganizationDTO>>
            ) {
                changeVisibility(View.INVISIBLE)

                if (response.isSuccessful) {

                    if (response.body()?.errorCode != null) {
                        ErrorUtil.showAlertMessageForErrorCode(
                            response.body()!!.errorCode,
                            activity
                        )

                        ErrorUtil.handleError(response.body()!!.errorCode, activity)
                    } else {
                        response.body()?.payload?.let {

                            setUserRatedOrganizations(activity)
                            _selectedProduct.value?.let {
                                setOrganizationsByProductId(
                                    activity,
                                    it.id
                                )
                            }
                        }
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<OrganizationDTO>>,
                t: Throwable
            ) {
                changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "Leave recension failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }
}