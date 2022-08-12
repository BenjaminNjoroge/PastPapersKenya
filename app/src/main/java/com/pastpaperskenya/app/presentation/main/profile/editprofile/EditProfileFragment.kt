package com.pastpaperskenya.app.presentation.main.profile.editprofile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.databinding.FragmentEditProfileBinding
import com.pastpaperskenya.app.presentation.auth.AuthActivity
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val viewModel: EditProfileViewModel by activityViewModels()
    private var _binding: FragmentEditProfileBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentEditProfileBinding.inflate(layoutInflater)

        clickListeners()
        listenToChannels()
        registerObservers()

        return binding.root
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