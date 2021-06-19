package com.mcecelja.catalogue.ui.organization.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.mcecelja.catalogue.location.LocationLiveData

class MapsViewModel(private val locationLiveData: LocationLiveData) : ViewModel() {

    private val _marker: MutableLiveData<Marker> = MutableLiveData<Marker>()
    val marker: LiveData<Marker> = _marker

    fun getLocation() = locationLiveData

    fun setMarker(marker: Marker) {
        _marker.postValue(marker)
    }

    fun updatePosition(latLng: LatLng) {
        _marker.value?.position = latLng
    }
}