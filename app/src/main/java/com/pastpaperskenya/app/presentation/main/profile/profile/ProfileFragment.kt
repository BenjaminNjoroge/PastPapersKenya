package com.pastpaperskenya.app.presentation.main.profile.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.UserDetails
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentProfileBinding?= null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth= FirebaseAuth.getInstance()
        firebaseUser= firebaseAuth.currentUser?.uid.toString()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentProfileBinding.inflate(inflater, container, false)

        registerObservers()
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

    private fun registerObservers(){
        viewModel.userProfile.observe(viewLifecycleOwner, Observer {
            binding.tvUserNameP.text= it.firstname + " "+ it.lastname
            binding.tvEmailP.text= it.email
            binding.tvPhone.text= it.phone
            binding.tvLastname.text= it.lastname
            binding.tvFirstname.text= it.firstname
            binding.tvCountry.text= it.country
            binding.tvCounty.text= it.county
        })


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding= null
    }
}