package com.pastpaperskenya.app.presentation.main.home.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import com.pastpaperskenya.app.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(),  HomeAdapter.ClickListener{

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!


    private lateinit var homeAdapter: HomeAdapter
    private lateinit var sliderAdapter: ImageSliderAdapter


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

        viewModel.sliderImages.observe(viewLifecycleOwner){

            when(it){
                is NetworkResult.Loading->{
                    binding.sliderShimmerView.isVisible= it.loading
                }
                is NetworkResult.Success->{
                    binding.sliderShimmerView.visibility= View.GONE
                    binding.sliderViewpager.visibility= View.VISIBLE
                    binding.sliderIndicator.visibility= View.VISIBLE

                    sliderAdapter= ImageSliderAdapter(requireContext(), it.data)
                    binding.sliderViewpager.adapter= sliderAdapter
                    binding.sliderIndicator.setViewPager(binding.sliderViewpager)
                }
                is NetworkResult.Error->{
                    Toast.makeText(requireContext(), it.error,Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onClick(categoryId: Int) {
        findNavController().navigate(R.id.action_homeFragment_to_subCategoryFragment)
        bundleOf(Constants.KEY_ID to categoryId)
    }


}