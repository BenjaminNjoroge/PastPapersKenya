package com.pastpaperskenya.app.presentation.main.cart.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.databinding.FragmentCartBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import com.pastpaperskenya.app.presentation.main.home.products.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(), CartAdapter.RemoveCartItemClickListener {

    private var _binding: FragmentCartBinding?= null
    private val binding get() = _binding!!

    private val viewModel: CartViewModel by viewModels()
    private lateinit var adapter:CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentCartBinding.inflate(inflater, container, false)
        (activity as MainActivity).supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_TITLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= CartAdapter(this)
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.productCartRecycler.layoutManager = linearLayoutManager
        binding.productCartRecycler.adapter = adapter

        viewModel.response.observe(viewLifecycleOwner){ items->

            if(!items.isNullOrEmpty()){
                binding.productCartRecycler.visibility= View.VISIBLE
                binding.layoutEmpty.visibility= View.GONE
                adapter.submitList(items)

            } else {
                binding.productCartRecycler.visibility= View.GONE
                binding.layoutEmpty.visibility= View.VISIBLE

            }
        }
    }

    override fun removeItem(position: Int, cart: Cart?) {
        viewModel.deleteItem(position)
    }

}