package com.pastpaperskenya.app.presentation.main.home.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.business.util.toast
import com.pastpaperskenya.app.databinding.FragmentProductsBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import com.pastpaperskenya.app.presentation.main.home.subcategory.SubCategoryAdapter
import com.pastpaperskenya.app.presentation.main.home.subcategory.SubCategoryFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(), ProductsAdapter.ClickListener {
    private var _binding: FragmentProductsBinding?= null
    private val binding get()= _binding!!

    private val viewModel: ProductsViewModel by viewModels()
    private val args: ProductsFragmentArgs by navArgs()
    private lateinit var adapter: ProductsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        val title= args.title
        (activity as MainActivity).supportActionBar?.title= title
       // (activity as MainActivity).supportActionBar?.setCustomView(R.layout.products_toolbar_layout)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id= args.id
        id.let {
            viewModel.start(it)
        }

        adapter= ProductsAdapter(this)
        val gridLayoutManager= GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        binding.primaryRecycler.layoutManager= gridLayoutManager
        binding.primaryRecycler.adapter= adapter

        registerObservers()

    }

    fun registerObservers(){
        viewModel.products.observe(viewLifecycleOwner){
            when(it.status){
                Resource.Status.LOADING->{
                    binding.progressBar.visibility= View.VISIBLE
                }
                Resource.Status.SUCCESS->{
                    binding.progressBar.visibility= View.GONE
                    if (!it.data.isNullOrEmpty()) adapter.submitList(it.data) else binding.progressBar.visibility= View.VISIBLE
                }
                Resource.Status.ERROR->{
                    binding.progressBar.visibility= View.GONE
                    toast(it.message.toString())
                }
            }
        }
    }

    override fun onClick(id: Int) {

    }


}