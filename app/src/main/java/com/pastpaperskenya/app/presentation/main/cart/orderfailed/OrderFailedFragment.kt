package com.pastpaperskenya.app.presentation.main.cart.orderfailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pastpaperskenya.app.databinding.FragmentOrderConfirmedBinding
import com.pastpaperskenya.app.databinding.FragmentOrderFailedBinding

class OrderFailedFragment : Fragment(){

    private var _binding: FragmentOrderFailedBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentOrderFailedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkDownloadsStatus.setOnClickListener {
            activity?.fragmentManager?.popBackStack();
        }
    }
}