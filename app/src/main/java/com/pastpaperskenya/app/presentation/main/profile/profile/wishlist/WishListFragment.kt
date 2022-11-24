package com.pastpaperskenya.app.presentation.main.profile.profile.wishlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.databinding.FragmentWishListBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class WishListFragment : Fragment(), WishlistAdapter.WishlistItemClickListener {

    private val TAG = "WishListFragment"
    private var param1: String? = null

    private var _binding: FragmentWishListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WishListViewModel by viewModels()
    private lateinit var adapter: WishlistAdapter

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

        _binding = FragmentWishListBinding.inflate(inflater, container, false)
        (activity as MainActivity).supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_TITLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WishlistAdapter(this)
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.myOrdersRecycler.layoutManager = linearLayoutManager
        binding.myOrdersRecycler.adapter = adapter

        registerObserver()
        listenToChannels()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            WishListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }


    private fun registerObserver() {
        viewModel.response.observe(viewLifecycleOwner) { items ->

            if (!items.isNullOrEmpty()) {
                binding.myOrdersRecycler.visibility = View.VISIBLE

                binding.layoutEmpty.visibility = View.GONE
                binding.pbLoading.visibility = View.GONE
                adapter.submitList(items)

            } else {
                binding.myOrdersRecycler.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun listenToChannels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { events ->
                when (events) {
                    is AuthEvents.ErrorCode -> {
                        if(events.code== 100){
                            //                      requireParentFragment()
                            Toast.makeText(requireContext(), "too bad", Toast.LENGTH_SHORT).show()
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

    override fun removeItem(position: Int) {
        viewModel.deleteItem(position)
    }

    override fun createOder(position: Int, productId: Int) {
        val bundle= bundleOf("id" to productId)
        findNavController().navigate(R.id.action_wishListFragment_to_productWishlistDetailFragment, bundle)
    }
}