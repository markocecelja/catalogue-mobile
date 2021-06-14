package com.mcecelja.catalogue.ui.userprofile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.users.UserDTO
import com.mcecelja.catalogue.databinding.FragmentUserProfileBinding
import com.mcecelja.catalogue.services.UserService
import com.mcecelja.catalogue.ui.login.LoginActivity
import com.mcecelja.catalogue.utils.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileFragment : Fragment() {

    private lateinit var userProfileBinding: FragmentUserProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userProfileBinding = FragmentUserProfileBinding.inflate(inflater, container, false)

        arguments?.let {
            val token = it.getSerializable(TOKEN) as String

            val apiCall =
                RestUtil.createService(UserService::class.java, token).getCurrentUserInfo()

            apiCall.enqueue(object : Callback<ResponseMessage<UserDTO>> {
                override fun onResponse(
                    call: Call<ResponseMessage<UserDTO>>,
                    response: Response<ResponseMessage<UserDTO>>
                ) {
                    if (response.isSuccessful) {
                        userProfileBinding.tvUsername.text = response.body()?.payload?.name
                    }
                }

                override fun onFailure(call: Call<ResponseMessage<UserDTO>>, t: Throwable) {
                    Toast.makeText(
                        Catalogue.application,
                        "Get current user info failed!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        }

        userProfileBinding.bLogout.setOnClickListener { logout() }

        return userProfileBinding.root
    }

    companion object {
        const val TAG = "User profile"
        private const val TOKEN = "Token"
        fun create(token: String): UserProfileFragment {
            val args = Bundle()
            args.putSerializable(TOKEN, token)
            val fragment = UserProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun logout() {
        PreferenceManager.removePreference("Token")
        val loginIntent = Intent(Catalogue.application, LoginActivity::class.java)
        startActivity(loginIntent)
    }
}