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
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.databinding.FragmentUserProfileBinding
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.listener.FavouriteItemClickListener
import com.mcecelja.catalogue.ui.login.LoginActivity
import com.mcecelja.catalogue.ui.organization.OrganizationsListFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileFragment : Fragment(), FavouriteItemClickListener {

    private lateinit var userProfileBinding: FragmentUserProfileBinding

    private val userProfileViewModel by viewModel<UserProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userProfileBinding = FragmentUserProfileBinding.inflate(inflater, container, false)

        userProfileBinding.rvFavourites.adapter = FavouritesAdapter(mutableListOf(), this)
        userProfileBinding.mbLogout.setOnClickListener { logout() }

        userProfileViewModel.setUser()

        userProfileViewModel.user.observe(
            viewLifecycleOwner,
            {
                userProfileBinding.tvUserInfo.text = String.format(
                    "%s %s",
                    userProfileViewModel.user.value!!.firstName,
                    userProfileViewModel.user.value!!.lastName
                )
            })

        userProfileViewModel.favourites.observe(
            viewLifecycleOwner,
            {
                (userProfileBinding.rvFavourites.adapter as FavouritesAdapter).refreshData(
                    userProfileViewModel.favourites.value!!
                )
            })

        return userProfileBinding.root
    }

    override fun onResume() {
        super.onResume()
        userProfileViewModel.setFavourites(requireActivity())
    }

    override fun onItemClicked(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_fragmentContainer,
                OrganizationsListFragment.create(userProfileViewModel.favourites.value!![position]),
                OrganizationsListFragment.TAG
            )
            .addToBackStack(TAG)
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