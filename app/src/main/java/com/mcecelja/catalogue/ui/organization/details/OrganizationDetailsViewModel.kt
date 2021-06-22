package com.mcecelja.catalogue.ui.organization.details

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
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.model.LocationModel
import com.mcecelja.catalogue.services.OrganizationService
import com.mcecelja.catalogue.services.PlacesService
import com.mcecelja.catalogue.ui.catalogue.MainActivity
import com.mcecelja.catalogue.utils.AlertUtil
import com.mcecelja.catalogue.utils.PlacesUtil
import com.mcecelja.catalogue.utils.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrganizationDetailsViewModel : ViewModel() {

    private val _organization: MutableLiveData<OrganizationDTO> = MutableLiveData<OrganizationDTO>()
    val organization: LiveData<OrganizationDTO> = _organization

    private val _places: MutableLiveData<List<PlaceDTO>> = MutableLiveData<List<PlaceDTO>>()
    val places: LiveData<List<PlaceDTO>> = _places

    fun setOrganization(organization: OrganizationDTO) {
        _organization.value = organization
    }

    fun leaveRecension(grade: Int, activity: FragmentActivity) {

        val apiCall =
            RestUtil.createService(
                OrganizationService::class.java, PreferenceManager.getPreference(
                    PreferenceEnum.TOKEN
                )
            ).leaveRecension(_organization.value!!.id, RatingDTO(grade))

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
                            _organization.value = it
                            (activity as MainActivity).userProfileViewModel.updateOrganization(it)
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

    fun setCurrentOrganizationPlaces(locationModel: LocationModel, apiKey: String) {
        val apiCall =
            PlacesUtil.createService(PlacesService::class.java).getNearbyPlaces(
                CoordinatesDTO(locationModel.latitude, locationModel.longitude),
                5000,
                Catalogue.application.getString(R.string.places_type_filter),
                _organization.value!!.name,
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
                        if (!responsePlace.name.contains(_organization.value!!.name, true)) {
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
}