package com.pastpaperskenya.papers.presentation.main.cart.orderconfirmed

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.pastpaperskenya.papers.R
import com.pastpaperskenya.papers.databinding.FragmentOrderConfirmedBinding
import com.pastpaperskenya.papers.presentation.main.downloads.downloads.DownloadsActivity


class OrderConfirmedFragment : Fragment() {

    private var _binding: FragmentOrderConfirmedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_orderConfirmedFragment_to_cartFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentOrderConfirmedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customMessage = arguments?.getString("completed_message")
        if (customMessage != null) {
            binding.resultDesc.text= customMessage
        }

        binding.checkDownloadsStatus.setOnClickListener {
            val intent= Intent(requireContext(), DownloadsActivity::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }



}