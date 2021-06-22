package com.mcecelja.catalogue.ui.userprofile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.adapters.favourites.FavouritesAdapter
import com.mcecelja.catalogue.adapters.recension.RecensionAdapter
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.databinding.FragmentUserProfileBinding
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.listener.FavouriteItemClickListener
import com.mcecelja.catalogue.listener.OrganizationItemClickListener
import com.mcecelja.catalogue.ui.catalogue.MainActivity
import com.mcecelja.catalogue.ui.login.LoginActivity
import com.mcecelja.catalogue.ui.organization.OrganizationsListFragment
import com.mcecelja.catalogue.ui.organization.details.OrganizationDetailsFragment

class UserProfileFragment : Fragment(), FavouriteItemClickListener, OrganizationItemClickListener {

    private lateinit var userProfileBinding: FragmentUserProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userProfileBinding = FragmentUserProfileBinding.inflate(inflater, container, false)

        userProfileBinding.rvFavourites.adapter = FavouritesAdapter(mutableListOf(), this)
        userProfileBinding.rvRecension.adapter =
            RecensionAdapter(mutableListOf(), this)
        userProfileBinding.mbLogout.setOnClickListener { logout() }

        (requireActivity() as MainActivity).userProfileViewModel.setUser()
        (requireActivity() as MainActivity).userProfileViewModel.setOrganizations()

        (requireActivity() as MainActivity).userProfileViewModel.user.observe(
            viewLifecycleOwner,
            {
                userProfileBinding.tvUserInfo.text = String.format(
                    "%s %s",
                    it.firstName,
                    it.lastName
                )
            })

        (requireActivity() as MainActivity).userProfileViewModel.favourites.observe(
            viewLifecycleOwner,
            {
                (userProfileBinding.rvFavourites.adapter as FavouritesAdapter).refreshData(it)
            })

        (requireActivity() as MainActivity).userProfileViewModel.userRatedOrganizations.observe(
            viewLifecycleOwner,
            {
                (userProfileBinding.rvRecension.adapter as RecensionAdapter).refreshData(it)
            })

        return userProfileBinding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).userProfileViewModel.setFavourites(requireActivity())
        (requireActivity() as MainActivity).userProfileViewModel.setOrganizations()
    }

    override fun onItemClicked(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_fragmentContainer,
                OrganizationsListFragment.create((requireActivity() as MainActivity).userProfileViewModel.favourites.value!![position]),
                OrganizationsListFragment.TAG
            )
            .addToBackStack(TAG)
            .commit()
    }

    override fun onOrganizationClicked(organization: OrganizationDTO) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_fragmentContainer,
                OrganizationDetailsFragment.create(organization),
                OrganizationDetailsFragment.TAG
            )
            .addToBackStack(OrganizationsListFragment.TAG)
            .commit()
    }

    companion object {
        const val TAG = "User profile"
        fun create(): UserProfileFragment {
            return UserProfileFragment()
        }
    }

    private fun logout() {
        PreferenceManager.removePreference(PreferenceEnum.TOKEN)
        val loginIntent = Intent(Catalogue.application, LoginActivity::class.java)
        startActivity(loginIntent)
    }
}