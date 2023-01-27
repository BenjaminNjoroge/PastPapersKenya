package com.pastpaperskenya.papers.presentation.main.profile.profile.myorders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pastpaperskenya.papers.R
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import com.pastpaperskenya.papers.business.util.toast
import com.pastpaperskenya.papers.databinding.FragmentMyOrdersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class MyOrdersFragment : Fragment(R.layout.fragment_my_orders),
    MyOrdersAdapter.OrderClickListener {

    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: MyOrdersViewModel by viewModels()
    private lateinit var adapter: MyOrdersAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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

        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)


        binding.continueShopping.setOnClickListener{
            Toast.makeText(requireContext(), "Navigate to home page to buy revision papers", Toast.LENGTH_SHORT).show()
        }
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

        swipeRefreshLayout= view.findViewById<SwipeRefreshLayout>(R.id.parent_view)

        adapter = MyOrdersAdapter(this)
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.myOrdersRecycler.layoutManager = linearLayoutManager
        binding.myOrdersRecycler.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            setupobserver()
        }
        setupobserver()
        registerObserver()
    }

    private fun registerObserver(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.events.collect{ events->
                when(events){
                    is AuthEvents.Message->{
                        //nothing
                    }
                    is AuthEvents.ErrorCode->{
                        //nothing
                    }
                    is AuthEvents.Error->{
                        events.message
                    }
                }
            }
        }
    }

    private fun setupobserver(){
        viewmodel.response.observe(viewLifecycleOwner) { items->
            when (items.status) {
                NetworkResult.Status.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
                NetworkResult.Status.SUCCESS -> {

                    if(!items.data.isNullOrEmpty()){
                        binding.myOrdersRecycler.visibility= View.VISIBLE
                        binding.holderLayout.visibility= View.GONE
                        binding.pbLoading.visibility= View.GONE
                        adapter.submitList(items.data)
                    } else{
                        binding.myOrdersRecycler.visibility= View.GONE
                        binding.holderLayout.visibility= View.VISIBLE
                        binding.pbLoading.visibility= View.VISIBLE
                    }


                    swipeRefreshLayout.setOnRefreshListener {
                        swipeRefreshLayout.isRefreshing= false
                    }
                }
                NetworkResult.Status.ERROR -> {
                    binding.myOrdersRecycler.visibility= View.GONE
                    binding.pbLoading.visibility = View.GONE
                    binding.holderLayout.visibility= View.VISIBLE
                    toast(items.message.toString())
                }
            }
        }
    }

    override fun onClick(position: Int, id: Int) {
        val bundle = bundleOf("id" to id)
//        findNavController().navigate(
//            R.id.action_myOrdersFragment_to_myOrdersDetailsFragment,
//            bundle
//        )
    }

}