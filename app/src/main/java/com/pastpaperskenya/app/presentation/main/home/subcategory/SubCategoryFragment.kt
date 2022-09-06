package com.pastpaperskenya.app.presentation.main.home.subcategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import com.pastpaperskenya.app.databinding.FragmentSubCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoryFragment : Fragment(), SubCategoryAdapter.ClickListener {

    private var _binding: FragmentSubCategoryBinding?= null
    private val binding get() = _binding!!
    private val subCategoryViewModel: SubCategoryViewModel by viewModels()

    private lateinit var subCategoryAdapter: SubCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSubCategoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getInt(Constants.KEY_ID)?.let{
            //subCategoryViewModel.passedId(it)
        }
        subCategoryAdapter= SubCategoryAdapter(this)
        val gridLayoutManager= GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.layoutManager= gridLayoutManager
        binding.recyclerview.adapter= subCategoryAdapter
        setupObservers()
    }

    private fun setupObservers() {
        subCategoryViewModel.category.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Loading->{
                    binding.pbLoading.isVisible= it.loading
                }
                is NetworkResult.Success->{
                    binding.pbLoading.isVisible= false
                    subCategoryAdapter.setItems(it.data as ArrayList<Category>)
                }
                is NetworkResult.Error->{
                    binding.pbLoading.isVisible= false
                }
            }
        }
    }

    override fun onClick(categoryId: Int) {
        findNavController().navigate(R.id.action_subCategoryFragment_to_productsFragment)
        bundleOf("id" to categoryId)
    }

}