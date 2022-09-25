package com.pastpaperskenya.app.presentation.main.home.subcategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.databinding.FragmentSubCategoryBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoryFragment : Fragment(), SubCategoryAdapter.ClickListener{

    private var _binding: FragmentSubCategoryBinding?= null
    private val binding get() = _binding!!
    private val subCategoryViewModel: SubCategoryViewModel by viewModels()

    private val args: SubCategoryFragmentArgs by navArgs()

    private lateinit var subCategoryAdapter: SubCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentSubCategoryBinding.inflate(inflater, container, false)

        val title= args.title
        (activity as MainActivity).supportActionBar?.title= title

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id= args.id
        id.let {
            subCategoryViewModel.start(it)
        }

        subCategoryAdapter= SubCategoryAdapter(this)
        val gridLayoutManager= GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.layoutManager= gridLayoutManager
        binding.recyclerview.adapter= subCategoryAdapter

        registerObservers()
    }

    private fun registerObservers(){
        subCategoryViewModel.category.observe(viewLifecycleOwner){
            when(it.status){
                Resource.Status.SUCCESS->{
                    binding.pbLoading.visibility=View.GONE
                    if (!it.data.isNullOrEmpty()) subCategoryAdapter.submitList(it.data)
                }
                Resource.Status.LOADING->{
                    binding.pbLoading.visibility= View.VISIBLE
                }
                Resource.Status.ERROR->{
                    binding.pbLoading.visibility= View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onClick(categoryId: Int) {
        TODO("Not yet implemented")
    }


}