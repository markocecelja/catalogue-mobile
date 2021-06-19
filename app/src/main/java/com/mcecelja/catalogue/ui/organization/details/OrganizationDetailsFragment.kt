package com.mcecelja.catalogue.ui.organization.details

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
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
import com.mcecelja.catalogue.utils.GpsUtils
import com.mcecelja.catalogue.utils.OnGpsListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class OrganizationDetailsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentOrganizationDetailsBinding

    private lateinit var mMap: GoogleMap

    private val mapsViewModel: MapsViewModel by viewModel()

    private val organizationDetailsViewModel: OrganizationDetailsViewModel by viewModel()

    private var isGPSEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().supportFragmentManager.popBackStackImmediate()
                }
            })

        binding = FragmentOrganizationDetailsBinding.inflate(inflater, container, false)

        arguments?.let {
            val organization = it.getSerializable(ORGANIZATION) as OrganizationDTO
            organizationDetailsViewModel.setOrganization(organization)
        }

        organizationDetailsViewModel.organization.observe(
            viewLifecycleOwner,
            { binding.tvStore.text = it.name })

        organizationDetailsViewModel.places.observe(viewLifecycleOwner, { updateMapPlaces(it) })

        GpsUtils(requireActivity()).turnGPSOn(object : OnGpsListener {

            override fun gpsStatus(isGPSEnable: Boolean) {
                this@OrganizationDetailsFragment.isGPSEnabled = isGPSEnable
            }
        })
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val currentLocation = LatLng(0.0, 0.0)
        mapsViewModel.setMarker(
            mMap.addMarker(
                MarkerOptions().position(currentLocation)
                    .title(getString(R.string.current_user_marker))
            )
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        invokeLocationAction()
    }

    private fun invokeLocationAction() {
        when (PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(
                Catalogue.application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> startLocationUpdate()
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
        mapsViewModel.updatePosition(currentLocation)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14f))
        organizationDetailsViewModel.setCurrentOrganizationPlaces(
            locationModel,
            getString(R.string.places_api_key)
        )

    }

    private fun updateMapPlaces(places: List<PlaceDTO>) {
        for (place in places) {

            mMap.addMarker(
                MarkerOptions().position(
                    LatLng(
                        place.geometry.location.lat,
                        place.geometry.location.lng
                    )
                ).title(organizationDetailsViewModel.organization.value!!.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            )
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