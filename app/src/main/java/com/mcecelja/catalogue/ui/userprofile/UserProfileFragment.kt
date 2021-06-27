package com.mcecelja.catalogue.ui.userprofile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.adapters.favourites.FavouritesAdapter
import com.mcecelja.catalogue.adapters.recension.RecensionAdapter
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.data.dto.users.UserDTO
import com.mcecelja.catalogue.databinding.FragmentUserProfileBinding
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.listener.FavouriteItemClickListener
import com.mcecelja.catalogue.listener.OrganizationItemClickListener
import com.mcecelja.catalogue.ui.catalogue.CatalogueViewModel
import com.mcecelja.catalogue.ui.login.LoginActivity
import com.mcecelja.catalogue.ui.organization.OrganizationsListFragment
import com.mcecelja.catalogue.ui.organization.details.OrganizationDetailsFragment

class UserProfileFragment : Fragment(), FavouriteItemClickListener, OrganizationItemClickListener {

    private lateinit var userProfileBinding: FragmentUserProfileBinding

    private lateinit var catalogueViewModel: CatalogueViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userProfileBinding = FragmentUserProfileBinding.inflate(inflater, container, false)

        ViewModelProvider(requireActivity()).get(CatalogueViewModel::class.java).also {
            catalogueViewModel = it
        }

        setupRecyclers()

        userProfileBinding.mbLogout.setOnClickListener { logout() }

        catalogueViewModel.setUser(requireActivity())

        catalogueViewModel.user.observe(viewLifecycleOwner, { setupUser(it) })

        catalogueViewModel.currentUserFavourites.observe(
            viewLifecycleOwner,
            {
                (userProfileBinding.rvFavourites.adapter as FavouritesAdapter).refreshData(it)
            })

        catalogueViewModel.userRatedOrganizations.observe(
            viewLifecycleOwner,
            {
                (userProfileBinding.rvRecension.adapter as RecensionAdapter).refreshData(it)
            })

        catalogueViewModel.selectedProduct.observe(
            viewLifecycleOwner,
            { catalogueViewModel.setCurrentUserFavourites(requireActivity()) })

        return userProfileBinding.root
    }

    private fun setupUser(user: UserDTO) {

        userProfileBinding.tvName.text = String.format(
            "%s %s\n%s",
            user.firstName,
            user.lastName,
            user.username
        )
        userProfileBinding.tvEmail.text = user.email
    }

    private fun setupRecyclers() {

        userProfileBinding.rvFavourites.adapter = FavouritesAdapter(
            catalogueViewModel.currentUserFavourites.value ?: mutableListOf(),
            this
        )

        userProfileBinding.rvRecension.adapter = RecensionAdapter(
            catalogueViewModel.userRatedOrganizations.value ?: mutableListOf(),
            this
        )
    }

    override fun onItemClicked(product: ProductDTO) {
        catalogueViewModel.setSelectedProduct(product)
        catalogueViewModel.setOrganizationsByProductId(requireActivity(), product.id)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_fragmentContainer,
                OrganizationsListFragment.create(),
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
        requireActivity().finish()
    }
}