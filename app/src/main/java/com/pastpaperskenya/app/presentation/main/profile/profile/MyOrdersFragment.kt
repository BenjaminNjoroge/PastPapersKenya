package com.pastpaperskenya.app.presentation.main.profile.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.util.network.NetworkChangeReceiver
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.business.util.toast
import com.pastpaperskenya.app.databinding.FragmentMyOrdersBinding
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class MyOrdersFragment : Fragment(),
    MyOrdersAdapter.OrderClickListener {

    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: MyOrdersViewModel by viewModels()
    private lateinit var adapter: MyOrdersAdapter

    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentMyOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MyOrdersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= MyOrdersAdapter(this)
        val linearLayoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.myOrdersRecycler.layoutManager= linearLayoutManager
        binding.myOrdersRecycler.adapter= adapter

        if(NetworkChangeReceiver.isNetworkConnected()){
            viewmodel.response.observe(viewLifecycleOwner){
                when(it.status){
                    Resource.Status.LOADING->{
                        binding.pbLoading.visibility= View.VISIBLE
                    }
                    Resource.Status.SUCCESS->{
                        binding.myOrdersRecycler.visibility= View.VISIBLE
                        binding.pbLoading.visibility= View.GONE
                        if (!it.data.isNullOrEmpty()) adapter.submitList(it.data) else binding.pbLoading.visibility= View.VISIBLE

                    }
                    Resource.Status.ERROR->{
                        binding.pbLoading.visibility= View.GONE
                        toast(it.message.toString())
                    }
                }
            }
        }else{
            binding.pbLoading.visibility= View.GONE
            binding.myOrdersRecycler.visibility= View.GONE
            binding.emptyListLayout.visibility= View.VISIBLE
        }
    }

    override fun onClick(position: Int, id: Int) {
        val bundle= bundleOf("id" to id)
        findNavController().navigate(R.id.action_myOrdersFragment_to_myOrdersDetailsFragment, bundle)
    }

}