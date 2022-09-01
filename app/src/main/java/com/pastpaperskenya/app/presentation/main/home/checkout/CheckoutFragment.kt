package com.pastpaperskenya.app.presentation.main.home.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.pastpaperskenya.app.business.util.sealedclasses.ResourceOne
import com.pastpaperskenya.app.business.util.toast
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
        customerId= "Benamin mbuthia"
        orderId= "first order"
        viewModel.payMpesa(1, phone, customerId, orderId).observe(viewLifecycleOwner, { resource->
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