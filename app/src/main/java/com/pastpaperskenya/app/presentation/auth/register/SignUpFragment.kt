package com.pastpaperskenya.app.presentation.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.util.hideKeyboard
import com.pastpaperskenya.app.databinding.FragmentSignUpBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private val viewModel : SignUpViewModel by activityViewModels()
    private var _binding : FragmentSignUpBinding? = null
    private val binding get()  = _binding!!
    private val TAG = "SignUpFragment"

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmPassword: String
    private lateinit var firstname: String
    private lateinit var lastname: String
    private lateinit var phone: String
    private lateinit var country:String
    private lateinit var county: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater , container , false)

        userInput()
        listenToChannels()
        registerObservers()

        return binding.root
    }


    private fun userInput(){

        binding.apply {
            btnCreateAccount.setOnClickListener {
                email= binding.inputAccountEmail.text.toString().trim()
                password= binding.inputAccountPassword.text.toString().trim()
                confirmPassword= binding.inputConfirmAccountPassword.text.toString().trim()
                firstname= binding.inputAccountFirstname.text.toString().trim()
                lastname= binding.inputAccountLastname.text.toString().trim()
                phone= ""
                country= ""
                county=""

                hideKeyboard()
                progressBar.isVisible= true
                viewModel.register(email, phone, firstname, lastname, country, county, password, confirmPassword)

            }
            txtAccountLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            }

        }

    }

    private fun registerObservers(){
        viewModel.firebaseUser.observe(viewLifecycleOwner, { user->
            user?.let {
                launchActivity()
            }
        })
    }

    private fun listenToChannels(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authEventsFlow.collect{ events->
                when(events){
                    is AuthEvents.Message->{
                        binding.apply {
                            progressBar.isInvisible= true
                            Toast.makeText(requireActivity(), events.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is AuthEvents.Error->{
                        binding.apply {
                            progressBar.isInvisible= true
                            errorTxt.text= events.message
                        }
                    }
                    is AuthEvents.ErrorCode->{

                        if (events.code==1){
                            binding.apply {
                                progressBar.isInvisible= true
                                inputAccountFirstname.error= "Firstname cannot be empty"
                            }
                        }
                        if (events.code ==2){
                            binding.apply {
                                progressBar.isInvisible= true
                                inputAccountLastname.error= "Lastname cannot be empty"
                            }
                        }
                        if(events.code==3){
                            binding.apply {
                                progressBar.isInvisible= true
                                inputAccountEmail.error= "Email cannot be empty"
                            }
                        }
                        if(events.code==4){
                            binding.apply {
                                progressBar.isInvisible= true
                                inputAccountPassword.error= "Password cannot be empty"
                            }
                        }
                        if(events.code==5){
                            binding.apply {
                                progressBar.isInvisible= true
                                inputConfirmAccountPassword.error= "Confirm password must be same"
                            }
                        }

                    }
                }
            }
        }
    }

    private fun launchActivity() {
        val intent= Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}