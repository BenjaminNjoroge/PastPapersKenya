package com.pastpaperskenya.app.presentation.main.home.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.pastpaperskenya.app.databinding.FragmentCheckoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private val viewModel: CheckoutViewModel by viewModels()
    private var _binding: FragmentCheckoutBinding?= null
    private val binding get() = _binding!!

    private lateinit var phone: String
    private lateinit var customerId: String
    private lateinit var orderId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentCheckoutBinding.inflate(inflater, container, false)


        binding.btnPayMpesaPay.setOnClickListener {
        }

        return binding.root
    }



}