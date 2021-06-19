package com.mcecelja.catalogue.ui.organization.details

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.data.dto.places.CoordinatesDTO
import com.mcecelja.catalogue.data.dto.places.PlaceDTO
import com.mcecelja.catalogue.data.dto.places.PlacesResponseDTO
import com.mcecelja.catalogue.model.LocationModel
import com.mcecelja.catalogue.services.PlacesService
import com.mcecelja.catalogue.utils.PlacesUtil
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

    fun setCurrentOrganizationPlaces(locationModel: LocationModel, apiKey: String) {
        val apiCall =
            PlacesUtil.createService(PlacesService::class.java).getNearbyPlaces(
                CoordinatesDTO(locationModel.latitude, locationModel.longitude),
                5000,
                "supermarket",
                _organization.value!!.name,
                apiKey
            )

        apiCall.enqueue(object : Callback<PlacesResponseDTO> {
            override fun onResponse(
                call: Call<PlacesResponseDTO>,
                response: Response<PlacesResponseDTO>
            ) {
                if (response.isSuccessful) {
                    _places.value = response.body()!!.results
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