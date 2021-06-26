package com.mcecelja.catalogue.ui.organization.details

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.data.dto.places.CoordinatesDTO
import com.mcecelja.catalogue.data.dto.places.PlaceDTO
import com.mcecelja.catalogue.data.dto.places.PlacesResponseDTO
import com.mcecelja.catalogue.location.LocationLiveData
import com.mcecelja.catalogue.model.LocationModel
import com.mcecelja.catalogue.services.PlacesService
import com.mcecelja.catalogue.utils.PlacesUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val locationLiveData: LocationLiveData) : ViewModel() {

    private val _mMap: MutableLiveData<GoogleMap> = MutableLiveData<GoogleMap>()
    val mMap: LiveData<GoogleMap> = _mMap

    private val _places: MutableLiveData<List<PlaceDTO>> = MutableLiveData<List<PlaceDTO>>()
    val places: LiveData<List<PlaceDTO>> = _places

    fun getLocation() = locationLiveData

    fun setMap(map: GoogleMap) {
        _mMap.value = map
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
}