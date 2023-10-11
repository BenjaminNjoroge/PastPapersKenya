package com.pastpaperskenya.papers.presentation.main.profile.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.siyamed.shapeimageview.CircularImageView
import com.google.firebase.auth.FirebaseAuth
import com.hbb20.CountryCodePicker
import com.pastpaperskenya.papers.R
import com.pastpaperskenya.papers.business.model.user.CustomerBilling
import com.pastpaperskenya.papers.business.model.user.CustomerUpdate
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.util.convertIntoNumeric
import com.pastpaperskenya.papers.business.util.getFileName
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import com.pastpaperskenya.papers.databinding.FragmentEditProfileBinding
import com.pastpaperskenya.papers.presentation.auth.AuthActivity
import com.pastpaperskenya.papers.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val viewModel: EditProfileViewModel by viewModels()
    private var _binding: FragmentEditProfileBinding?= null
    private val binding get() = _binding!!

    private lateinit var email: String
    private lateinit var country: String
    private lateinit var county: String
    private lateinit var userServerId: String

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userId: String

    private var profileUri: Uri?= null

    private lateinit var ccp: CountryCodePicker
    private lateinit var countySpinner: Spinner

    private lateinit var profileImage: CircularImageView

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

            viewModel.addBackgroundImageToStorage(getFileName(profileUri!!).toString(), profileUri!!)


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

        profileImage= view.findViewById(R.id.ivProfileImageP)

        binding.inputBillingEmail.keyListener= null
        email= binding.inputBillingEmail.text.toString()

        ccp = view.findViewById(R.id.input_billing_country);
        country= ccp.selectedCountryName

        countySpinner= view.findViewById(R.id.input_billing_county)
        val countiesAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(requireContext(),
        R.array.counties, android.R.layout.simple_list_item_1)
        countiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        countySpinner.adapter= countiesAdapter

        countySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
                val ss: String = countySpinner.selectedItem.toString()
                county= ss
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }


        binding.apply {

            btnLogout.setOnClickListener{
                rotateProgress.isVisible= true
                viewModel.logout()
            }

            btnChangePassword.setOnClickListener {
                findNavController().navigate(R.id.action_editProfileFragment_to_changePasswordFragment)
            }

            saveAddress.setOnClickListener {
                val firstname= binding.inputBillingFirstName.text.toString()
                val lastname= binding.inputBillingLastName.text.toString()
                val phone= binding.inputBillingPhone.text.toString()
                val email= FirebaseAuth.getInstance().currentUser?.email

                viewModel.updateFirestoreDetails(userId, phone, firstname, lastname, country, county,
                    email.toString()
                )

                viewModel.updateLocalDetails(phone, firstname, lastname, country, county, convertIntoNumeric(userServerId),
                    profileUri.toString()
                )

                binding.rotateProgress.visibility= View.VISIBLE

                val billing= CustomerBilling(county, country, phone)
                val customer= CustomerUpdate(firstname, lastname, billing)

                viewModel.fieldsChecker(convertIntoNumeric(userServerId), phone, firstname, lastname, customer)

            }

            ivProfileImageP.setOnClickListener{
                ImagePicker.with(requireActivity())
                    .createIntent { intent->
                        startForProfileImageResult.launch(intent)
                    }
            }

            backgroundProfile.setOnClickListener {
//                ImagePicker.with(requireActivity()).createIntent { intent->
//                        startForBackgroundImageResult.launch(intent)
//                    }
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

        viewModel.addImageStorageResponse.observe(viewLifecycleOwner){
            when(it.status){
                NetworkResult.Status.LOADING->{
                    binding.rotateProgress.visibility= View.VISIBLE
                }
                NetworkResult.Status.SUCCESS->{
                    binding.rotateProgress.visibility= View.GONE
                    viewModel.addBackgoundImageToFirestore(userId, it.data?.path.toString())
                }
                NetworkResult.Status.ERROR->{
                    binding.rotateProgress.visibility= View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


        viewModel.addImageDatabaseResponse.observe(viewLifecycleOwner){
            when(it.status){
                NetworkResult.Status.LOADING->{
                    binding.rotateProgress.visibility= View.VISIBLE
                }
                NetworkResult.Status.SUCCESS->{
                    binding.rotateProgress.visibility= View.GONE
                    Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                }
                NetworkResult.Status.ERROR->{
                    binding.rotateProgress.visibility= View.GONE
                }
            }
        }

        viewModel.userProfile.observe(viewLifecycleOwner){ details->
            binding.inputBillingFirstName.text= Editable.Factory.getInstance().newEditable(details.firstname)
            binding.inputBillingLastName.text= Editable.Factory.getInstance().newEditable(details.lastname)
            binding.inputBillingEmail.text= Editable.Factory.getInstance().newEditable(details.email)
            //binding.inputBillingCounty.text= Editable.Factory.getInstance().newEditable(details.county)
            binding.inputBillingPhone.text= Editable.Factory.getInstance().newEditable(details.phone)

            userServerId= details.userServerId.toString()
        }

    }

    private fun listenToChannels(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect{ events->
                when(events){
                    is AuthEvents.Message->{
                        binding.rotateProgress.visibility= View.GONE
                        Toast.makeText(requireActivity(), events.message, Toast.LENGTH_SHORT).show()
                        //findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)

                    }
                    is AuthEvents.Error->{
                        Toast.makeText(requireActivity(), events.message, Toast.LENGTH_SHORT).show()

                    }
                    is AuthEvents.ErrorCode->{
                        if (events.code==1){
                            binding.apply {
                                rotateProgress.isInvisible= true
                                inputBillingFirstName.error= "Firstname cannot be empty"
                            }
                        }
                        if (events.code ==2){
                            binding.apply {
                                rotateProgress.isInvisible= true
                                inputBillingLastName.error= "Lastname cannot be empty"
                            }
                        }
                        if(events.code==3){
                            binding.apply {
                                rotateProgress.isInvisible= true
                                inputBillingPhone.error= "Phone cannot be empty"
                            }
                        }
                        if(events.code==100){
                            binding.apply {
                                rotateProgress.isInvisible= true
                                findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                            }
                        }
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