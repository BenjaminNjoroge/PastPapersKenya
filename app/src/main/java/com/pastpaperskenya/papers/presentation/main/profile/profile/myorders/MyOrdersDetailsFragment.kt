package com.pastpaperskenya.papers.presentation.main.profile.profile.myorders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import com.pastpaperskenya.papers.business.util.toast
import com.pastpaperskenya.papers.databinding.FragmentMyOrdersDetailsBinding


class MyOrdersDetailsFragment : Fragment() {

    private var _binding: FragmentMyOrdersDetailsBinding?= null
    private val binding get() = _binding!!

    private val viewModel: MyOrderDetailsViewModel by viewModels()
    private val args: MyOrdersDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentMyOrdersDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id= args.id
        id.let {
            viewModel.start(it)
        }
        viewModel.response.observe(viewLifecycleOwner){
            when(it.status){
                NetworkResult.Status.LOADING->{
                    binding.shimmerOrderDetail.shimmerLayout.visibility= View.VISIBLE
                }
                NetworkResult.Status.SUCCESS->{
                    binding.shimmerOrderDetail.shimmerLayout.visibility= View.GONE

                    binding.apply {
                        orderDetailId.text= it.data?.number
                        orderStatus.text= it.data?.status
                        paymentMethod.text= it.data?.payment_method
                        billingFullname.text= it.data?.billing?.first_name + " " + it.data?.billing?.last_name
                        billingEmail.text= it.data?.billing?.email
                        billingPhone.text= it.data?.billing?.phone
                        billingZone.text= it.data?.billing?.country + " | " + it.data?.billing?.state
                        checkoutTotal.text= it.data?.line_items?.get(0)?.total
                    }
                }
                NetworkResult.Status.ERROR->{
                    binding.shimmerOrderDetail.shimmerLayout.visibility= View.VISIBLE
                    toast(it.message.toString())
                }
            }
        }
    }

}