package com.mcecelja.catalogue.ui.organization.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.mcecelja.catalogue.location.LocationLiveData

class MapsViewModel(private val locationLiveData: LocationLiveData) : ViewModel() {

    private val _mMap: MutableLiveData<GoogleMap> = MutableLiveData<GoogleMap>()
    val mMap: LiveData<GoogleMap> = _mMap

    fun getLocation() = locationLiveData

    fun setMap(map: GoogleMap) {
        _mMap.value = map
    }
}