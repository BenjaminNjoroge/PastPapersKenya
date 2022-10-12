package com.pastpaperskenya.app.presentation.main.cart.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(), CartAdapter.RemoveCartItemClickListener {

    private var _binding: FragmentCartBinding?= null
    private val binding get() = _binding!!

    private val viewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.response.observe(viewLifecycleOwner){

        }
    }

    override fun removeItem(position: Int, cart: Cart?) {
        viewModel.deleteItem(cart!!)
    }

}