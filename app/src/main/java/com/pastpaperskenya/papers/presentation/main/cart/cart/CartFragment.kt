package com.pastpaperskenya.papers.presentation.main.cart.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.papers.R
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.databinding.FragmentCartBinding
import com.pastpaperskenya.papers.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment(), CartAdapter.RemoveCartItemClickListener {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CartViewModel by viewModels()
    private lateinit var adapter: CartAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        (activity as MainActivity).supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_TITLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CartAdapter(this)
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.productCartRecycler.layoutManager = linearLayoutManager
        binding.productCartRecycler.adapter = adapter

        registerObserver()
        listenToChannels()

        binding.cartCheckout.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_placeOrderFragment)
        }

    }

    private fun registerObserver() {
        viewModel.response.observe(viewLifecycleOwner) { items ->

            if (!items.isNullOrEmpty()) {
                binding.productCartRecycler.visibility = View.VISIBLE
                binding.cartCheckoutLayout.visibility = View.VISIBLE


                binding.layoutEmpty.visibility = View.GONE
                binding.pbLoading.visibility = View.GONE
                adapter.submitList(items)

            } else {
                binding.productCartRecycler.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.cartCheckoutLayout.visibility = View.GONE
            }
        }

        viewModel.totalPrice.observe(viewLifecycleOwner){ total->
            binding.cartTotalPrice.text = "Total Ksh: $total"
        }
    }

    override fun removeItem(position: Int) {
        viewModel.deleteItem(position)
    }

    private fun listenToChannels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { events ->
                when (events) {
                    is AuthEvents.ErrorCode -> {
                        if(events.code== 100){
                            Toast.makeText(requireContext(), "removed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is AuthEvents.Error -> {
                    }
                    is AuthEvents.Message -> {

                    }
                }

            }
        }
    }
}