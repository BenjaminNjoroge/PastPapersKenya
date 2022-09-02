package com.pastpaperskenya.app.presentation.main.home.dashboard


import androidx.fragment.app.viewModels
import com.pastpaperskenya.app.databinding.FragmentHomeBinding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(),  HomeAdapter.ClickListener{

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!


    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeAdapter= HomeAdapter(this)
        val gridLayoutManager= GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.layoutManager= gridLayoutManager
        binding.recyclerview.adapter= homeAdapter

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.category.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Loading->{
                    binding.pbLoading.isVisible= it.loading
                }
                is NetworkResult.Error->{
                    binding.pbLoading.isVisible= false
                    Toast.makeText(requireContext(), it.error,Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success->{
                    homeAdapter.setItems(it.data as ArrayList<Category>)
                    binding.pbLoading.isVisible= false
                }
            }
        }
    }

    override fun onClick(categoryId: Int) {
        findNavController().navigate(R.id.action_homeFragment_to_subCategoryFragment)
        bundleOf("id" to categoryId)
    }


}