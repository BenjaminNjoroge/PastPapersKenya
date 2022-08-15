package com.pastpaperskenya.app.presentation.main.profile.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentProfileBinding?= null
    private val binding get() = _binding!!
    private lateinit var fbemail: String
    private lateinit var fbUsername: String
    private lateinit var fbProfilePhoto: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentProfileBinding.inflate(inflater, container, false)

        requireActivity().intent.extras?.apply {
            fbemail= this.getString(Constants.KEY_EMAIL).toString()
            fbUsername= this.getString(Constants.KEY_USERNAME).toString()
            fbProfilePhoto= this.getString(Constants.KEY_PROFILE_PHOTO).toString()
        }
        clickListeners()
        return binding.root
    }


    private fun clickListeners(){
        binding.apply {
            editButton.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding= null
    }
}