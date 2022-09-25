package com.pastpaperskenya.app.presentation.main.home.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.databinding.FragmentProductsBinding


class ProductsFragment : Fragment() {
    private var _binding: FragmentProductsBinding?= null
    private val binding get()= _binding!!

    private val viewModel: ProductsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

}