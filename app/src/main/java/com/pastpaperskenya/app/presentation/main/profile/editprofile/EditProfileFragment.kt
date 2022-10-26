package com.pastpaperskenya.app.presentation.main.profile.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.auth.Customer
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.databinding.FragmentEditProfileBinding
import com.pastpaperskenya.app.presentation.auth.AuthActivity
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val viewModel: EditProfileViewModel by viewModels()
    private var _binding: FragmentEditProfileBinding?= null
    private val binding get() = _binding!!

    private lateinit var firstname: String
    private lateinit var lastname: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var country: String
    private lateinit var county: String
    private lateinit var userServerId: String

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userId: String

    private var profileUri: Uri?= null

    private val startForProfileImageResult= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        val resultCode= result.resultCode
        val data= result.data

        if (resultCode == Activity.RESULT_OK){
            val fileUri= data!!.data
            profileUri = fileUri

            Glide.with(requireContext()).load(profileUri).into(binding.ivProfileImageP)
        } else if(resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(requireContext(),"Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private val startForBackgroundImageResult= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        val resultCode= result.resultCode
        val data= result.data

        if (resultCode == Activity.RESULT_OK){
            val fileUri= data!!.data
            profileUri = fileUri

            Glide.with(requireContext()).load(profileUri).into(binding.backgroundProfile)
        } else if(resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(requireContext(),"Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth= FirebaseAuth.getInstance()
        userId= firebaseAuth.currentUser?.uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentEditProfileBinding.inflate(layoutInflater)

        (activity as MainActivity).supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_TITLE

        listenToChannels()
        registerObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstname= binding.inputBillingFirstName.text.toString()
        lastname= binding.inputBillingLastName.text.toString()
        binding.inputBillingEmail.keyListener= null
        phone= binding.inputBillingPhone.text.toString()
        email= binding.inputBillingEmail.text.toString()
        country= binding.inputBillingCountry.text.toString()
        county= binding.inputBillingCounty.text.toString()
        userServerId= ""

        clickListeners()
    }

    private fun clickListeners() {
        binding.apply {
            btnLogout.setOnClickListener{
                rotateProgress.isVisible= true
                viewModel.logout()
            }
            btnChangePassword.setOnClickListener {
                findNavController().navigate(R.id.action_editProfileFragment_to_changePasswordFragment)
            }
            saveAddress.setOnClickListener {
                binding.rotateProgress.isVisible= true
                viewModel.updateUserDetails(userId, phone, firstname, lastname, country, county, userServerId)
                binding.rotateProgress.isVisible= false
            }

            ivProfileImageP.setOnClickListener{
                ImagePicker.with(requireActivity())
                    .createIntent { intent->
                        startForProfileImageResult.launch(intent)
                    }
            }

            backgroundProfile.setOnClickListener {
                ImagePicker.with(requireActivity())

                    .createIntent { intent->
                        startForBackgroundImageResult.launch(intent)
                    }
            }

        }
    }


    private fun registerObservers(){
        viewModel.firebaseUser.observe(viewLifecycleOwner) { user ->
            if(user!= null){
                //do nothing
            }else{
                launchActivity()
            }
        }

        viewModel.userProfile.observe(viewLifecycleOwner){ details->
            binding.inputBillingFirstName.text= Editable.Factory.getInstance().newEditable(details.firstname)
            binding.inputBillingLastName.text= Editable.Factory.getInstance().newEditable(details.lastname)
            binding.inputBillingEmail.text= Editable.Factory.getInstance().newEditable(details.email)
            binding.inputBillingCountry.text= Editable.Factory.getInstance().newEditable(details.country)
            binding.inputBillingCounty.text= Editable.Factory.getInstance().newEditable(details.county)
            binding.inputBillingPhone.text= Editable.Factory.getInstance().newEditable(details.phone)
        }

    }

    private fun listenToChannels(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authEventsChannel.collect{ events->
                when(events){
                    is AuthEvents.Message->{
                        Toast.makeText(requireActivity(), events.message, Toast.LENGTH_SHORT).show()
                        binding.apply {
                            rotateProgress.isInvisible= true
                        }
                    }
                    is AuthEvents.Error->{
                        Toast.makeText(requireActivity(), events.message, Toast.LENGTH_SHORT).show()
                        binding.apply {
                            rotateProgress.isInvisible= true
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun launchActivity() {
        val intent= Intent(requireActivity(), AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding= null
    }

}