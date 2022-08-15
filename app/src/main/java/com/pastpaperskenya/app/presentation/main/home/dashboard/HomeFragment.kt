package com.pastpaperskenya.app.presentation.main.home.dashboard


import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.business.util.hide
import com.pastpaperskenya.app.business.util.show
import com.pastpaperskenya.app.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.ResourceOne
import com.pastpaperskenya.app.business.util.toast
import com.pastpaperskenya.app.databinding.FragmentCheckoutBinding
import com.pastpaperskenya.app.presentation.main.home.checkout.CheckoutViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.builders.FragmentComponentBuilder
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!

    private lateinit var phone: String
    private lateinit var amount: String
    private lateinit var customerId: String
    private lateinit var orderId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentHomeBinding.inflate(inflater, container, false)

        observeLoading()

        binding.btnPayMpesaPay.setOnClickListener {
            payMpesa()
        }

        return binding.root
    }


    protected fun handleApiError(resource: ResourceOne.Failure<Any>) {
        if (resource.response.message == "Invalid session-id") {
        } else {
            toast(resource.response.message)
        }
    }
    private fun observeLoading() {
        viewModel.loading.observe(viewLifecycleOwner, { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun payMpesa(){
        phone= binding.etPayMpesaMobilerNumber.text.toString()
        amount= binding.etPayMpesaAmount.text.toString()
        customerId= "Benjamin mbuthia"
        orderId= "first order"
        viewModel.payMpesa(amount.toInt(), phone, customerId, orderId).observe(viewLifecycleOwner, { resource->
            when(resource){
                is ResourceOne.Success -> {
                    toast("Request successful")
                    requireActivity().finish()
                }
                is ResourceOne.Failure -> handleApiError(resource)
            }
        })
    }

}