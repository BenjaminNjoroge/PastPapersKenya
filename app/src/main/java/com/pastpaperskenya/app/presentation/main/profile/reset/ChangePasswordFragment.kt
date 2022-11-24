package com.pastpaperskenya.app.presentation.main.profile.reset

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
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.business.util.hideKeyboard
import com.pastpaperskenya.app.databinding.FragmentChangePasswordBinding
import com.pastpaperskenya.app.presentation.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    private val viewModel : ChangePasswordViewModel by activityViewModels()
    private var _binding : FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater , container , false)

        userInput()
        listenToChannels()
        registerObservers()
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

    private fun registerObservers(){
        viewModel.firebaseUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                launchActivity()
            }
        }
    }

    private fun userInput(){
        binding.apply {
            btnResetPassword.setOnClickListener {
                progressBar.isVisible = true
                email= inputAccountEmail.text.toString().trim()

                hideKeyboard()
                viewModel.resetPassword(email)
            }
            txtAccountLogin.setOnClickListener {
                progressBar.isVisible= true
                viewModel.logout()
                launchActivity()
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



}