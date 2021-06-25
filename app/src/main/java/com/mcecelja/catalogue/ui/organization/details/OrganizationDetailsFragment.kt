package com.mcecelja.catalogue.ui.organization.details

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.data.dto.places.PlaceDTO
import com.mcecelja.catalogue.databinding.FragmentOrganizationDetailsBinding
import com.mcecelja.catalogue.enums.RequestEnum
import com.mcecelja.catalogue.model.LocationModel
import com.mcecelja.catalogue.ui.catalogue.CatalogueViewModel
import com.mcecelja.catalogue.utils.GpsUtils
import com.mcecelja.catalogue.utils.OnGpsListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrganizationDetailsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentOrganizationDetailsBinding

    private lateinit var catalogueViewModel: CatalogueViewModel

    private val mapsViewModel: MapsViewModel by viewModel()

    private var isGPSEnabled = false

    private lateinit var organization: OrganizationDTO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrganizationDetailsBinding.inflate(inflater, container, false)

        ViewModelProvider(requireActivity()).get(CatalogueViewModel::class.java).also {
            catalogueViewModel = it
        }

        arguments?.let {
            val organization = it.getSerializable(ORGANIZATION) as OrganizationDTO
            this.organization = organization
        }

        setOrganization(organization)
        binding.rb.setOnRatingBarChangeListener { _, rating, _ ->
            catalogueViewModel.leaveRecension(
                organization,
                rating.toInt(),
                requireActivity()
            )
        }

        catalogueViewModel.places.observe(viewLifecycleOwner, { updateMapPlaces(it) })

        GpsUtils(requireActivity()).turnGPSOn(object : OnGpsListener {

            override fun gpsStatus(isGPSEnable: Boolean) {
                this@OrganizationDetailsFragment.isGPSEnabled = isGPSEnable
            }
        })
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    private fun setOrganization(organization: OrganizationDTO) {
        binding.tvStore.text = organization.name

        if (organization.currentUserRating != null) {
            binding.rb.rating = organization.currentUserRating.grade.toFloat()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapsViewModel.setMap(googleMap)

        val currentLocation = LatLng(0.0, 0.0)
        mapsViewModel.mMap.value!!.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        invokeLocationAction()
    }

    private fun invokeLocationAction() {
        when (PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(
                Catalogue.application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                startLocationUpdate()
                mapsViewModel.mMap.value!!.isMyLocationEnabled = true
            }
            else -> ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                RequestEnum.LOCATION_REQUEST.code
            )
        }
    }

    private fun startLocationUpdate() {
        mapsViewModel.getLocation().observe(
            this, { updateMap(it) }
        )
    }

    private fun updateMap(locationModel: LocationModel) {
        val currentLocation = LatLng(locationModel.latitude, locationModel.longitude)
        mapsViewModel.mMap.value!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14f))
        catalogueViewModel.setCurrentOrganizationPlaces(
            organization,
            locationModel,
            getString(R.string.places_api_key)
        )
    }

    private fun updateMapPlaces(places: List<PlaceDTO>) {
        if(mapsViewModel.mMap.value != null) {
            for (place in places) {

                mapsViewModel.mMap.value!!.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            place.geometry.location.lat,
                            place.geometry.location.lng
                        )
                    ).title(organization.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                )
            }
        }
    }

    companion object {
        const val TAG = "Organization details"
        private const val ORGANIZATION = "Organization"
        fun create(organization: OrganizationDTO): OrganizationDetailsFragment {
            val args = Bundle()
            args.putSerializable(ORGANIZATION, organization)
            val fragment = OrganizationDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}