package com.pastpaperskenya.app.presentation.main.cart.orderconfirmed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.databinding.FragmentOrderConfirmedBinding
import com.pastpaperskenya.app.databinding.FragmentResetPasswordBinding


class OrderConfirmedFragment : Fragment() {

    private var _binding: FragmentOrderConfirmedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentOrderConfirmedBinding.inflate(inflater, container, false)

        return binding.root
    }

}