package com.pastpaperskenya.app.presentation.auth.reset

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
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.databinding.FragmentResetPasswordBinding
import com.pastpaperskenya.app.presentation.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private val viewModel : ResetPasswordViewModel by activityViewModels()
    private var _binding : FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater , container , false)

        userInput()
        listenToChannels()
        return binding.root
    }

    private fun listenToChannels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.resetChannel.collect{ events->
                when(events){
                    is AuthEvents.Message->{
                        Toast.makeText(requireActivity(), events.message, Toast.LENGTH_LONG).show()
                        binding.apply {
                            binding.errorTxt.isVisible= true
                            binding.errorTxt.text= events.message
                            binding.progressBar.isInvisible= true
                        }
                    }
                    is AuthEvents.Error->{
                        binding.apply {
                            binding.errorTxt.isVisible= true
                            binding.errorTxt.text= events.message
                            binding.progressBar.isInvisible= true
                        }
                    }
                    is AuthEvents.ErrorCode ->{
                        if(events.code== 1){
                            binding.progressBar.isInvisible= true
                            binding.inputAccountEmail.error= "Email field cannot be empty"
                        }
                    }
                }
            }
        }
    }

    private fun userInput(){
        binding.apply {
            btnResetPassword.setOnClickListener {
                progressBar.isVisible = true
                email= inputAccountEmail.text.toString().trim()
                viewModel.resetPassword(email)
            }
            txtAccountLogin.setOnClickListener {
                progressBar.isInvisible= true
                findNavController().navigate(R.id.action_resetPasswordFragment_to_signInFragment)
            }
        }
    }
}