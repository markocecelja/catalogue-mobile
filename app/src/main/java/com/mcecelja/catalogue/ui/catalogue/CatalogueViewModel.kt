package com.mcecelja.catalogue.ui.catalogue

import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.data.dto.organization.RatingDTO
import com.mcecelja.catalogue.data.dto.places.CoordinatesDTO
import com.mcecelja.catalogue.data.dto.places.PlaceDTO
import com.mcecelja.catalogue.data.dto.places.PlacesResponseDTO
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.data.dto.users.UserDTO
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.model.LocationModel
import com.mcecelja.catalogue.services.OrganizationService
import com.mcecelja.catalogue.services.PlacesService
import com.mcecelja.catalogue.services.ProductService
import com.mcecelja.catalogue.services.UserService
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.utils.AlertUtil
import com.mcecelja.catalogue.utils.PlacesUtil
import com.mcecelja.catalogue.utils.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogueViewModel : ViewModel() {

    private val _products: MutableLiveData<List<ProductDTO>> = MutableLiveData<List<ProductDTO>>()
    val products: LiveData<List<ProductDTO>> = _products

    private val _user: MutableLiveData<UserDTO> = MutableLiveData<UserDTO>()
    val user: LiveData<UserDTO> = _user

    private val _currentProductOrganizations: MutableLiveData<List<OrganizationDTO>> =
        MutableLiveData<List<OrganizationDTO>>()
    val currentProductOrganizations: LiveData<List<OrganizationDTO>> = _currentProductOrganizations

    private val _userRatedOrganizations: MutableLiveData<List<OrganizationDTO>> =
        MutableLiveData<List<OrganizationDTO>>()
    val userRatedOrganizations: LiveData<List<OrganizationDTO>> = _userRatedOrganizations

    private val _places: MutableLiveData<List<PlaceDTO>> = MutableLiveData<List<PlaceDTO>>()
    val places: LiveData<List<PlaceDTO>> = _places

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

    fun setOrganizationsByProductId(productId: Long, loadingViewModel: LoadingViewModel) {
        val apiCall =
            RestUtil.createService(
                OrganizationService::class.java,
                PreferenceManager.getPreference(PreferenceEnum.TOKEN)
            ).getOrganizations(productId)

        loadingViewModel.changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<List<OrganizationDTO>>> {
            override fun onResponse(
                call: Call<ResponseMessage<List<OrganizationDTO>>>,
                response: Response<ResponseMessage<List<OrganizationDTO>>>
            ) {
                loadingViewModel.changeVisibility(View.INVISIBLE)

                if (response.isSuccessful) {
                    _currentProductOrganizations.value = response.body()?.payload
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<List<OrganizationDTO>>>,
                t: Throwable
            ) {
                loadingViewModel.changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "Get favourites failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun setUserRatedOrganizations() {
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

    fun leaveRecension(organization: OrganizationDTO, grade: Int, activity: FragmentActivity) {

        val apiCall =
            RestUtil.createService(
                OrganizationService::class.java, PreferenceManager.getPreference(
                    PreferenceEnum.TOKEN
                )
            ).leaveRecension(organization.id, RatingDTO(grade))

        apiCall.enqueue(object : Callback<ResponseMessage<OrganizationDTO>> {
            override fun onResponse(
                call: Call<ResponseMessage<OrganizationDTO>>,
                response: Response<ResponseMessage<OrganizationDTO>>
            ) {
                if (response.isSuccessful) {

                    if (response.body()?.errorCode != null) {
                        AlertUtil.showAlertMessageForErrorCode(
                            response.body()!!.errorCode,
                            activity
                        )
                    } else {
                        response.body()?.payload?.let {

                            setUserRatedOrganizations()

                            if (_currentProductOrganizations.value != null) {

                                val organizations = mutableListOf<OrganizationDTO>()
                                organizations.addAll(_currentProductOrganizations.value!!)

                                val iterator = organizations.iterator()
                                while (iterator.hasNext()) {
                                    val o = iterator.next()
                                    if (o.id == it.id) {
                                        organizations[organizations.indexOf(o)] = it
                                    }
                                }

                                _currentProductOrganizations.value = organizations
                            }
                        }
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<OrganizationDTO>>,
                t: Throwable
            ) {

                Toast.makeText(
                    Catalogue.application,
                    "Leave recension failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun setCurrentOrganizationPlaces(
        organization: OrganizationDTO,
        locationModel: LocationModel,
        apiKey: String
    ) {
        val apiCall =
            PlacesUtil.createService(PlacesService::class.java).getNearbyPlaces(
                CoordinatesDTO(locationModel.latitude, locationModel.longitude),
                5000,
                Catalogue.application.getString(R.string.places_type_filter),
                organization.name,
                apiKey
            )

        apiCall.enqueue(object : Callback<PlacesResponseDTO> {
            override fun onResponse(
                call: Call<PlacesResponseDTO>,
                response: Response<PlacesResponseDTO>
            ) {
                if (response.isSuccessful) {

                    val responsePlaces = response.body()!!.results.toMutableList()

                    val iterator: MutableIterator<PlaceDTO> = responsePlaces.iterator()
                    while (iterator.hasNext()) {
                        val responsePlace = iterator.next()
                        if (!responsePlace.name.contains(organization.name, true)) {
                            iterator.remove()
                        }
                    }

                    _places.value = responsePlaces
                }
            }

            override fun onFailure(
                call: Call<PlacesResponseDTO>,
                t: Throwable
            ) {

                Toast.makeText(
                    Catalogue.application,
                    "Get places failed!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun getProductById(id: Long): ProductDTO? {
        for (product in _products.value!!) {
            if (product.id == id) {
                return product
            }
        }

        return null
    }
}