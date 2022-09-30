package com.pastpaperskenya.app.presentation.main.home.productdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.databinding.FragmentProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private val viewModel: ProductDetailViewModel by viewModels()
    private var _binding: FragmentProductDetailBinding?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentProductDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers(){

        viewModel.response.observe(viewLifecycleOwner){
            when(it.status){
                 Resource.Status.LOADING->{
                     //binding.detailShimmerLayout.productDetailShimmerView.visibility= View.VISIBLE
                     binding.productDetailLayout.visibility= View.GONE
                 }
                Resource.Status.SUCCESS->{
                    //binding.detailShimmerLayout.productDetailShimmerView.visibility= View.GONE
                    binding.productDetailLayout.visibility= View.VISIBLE
                    bindDetail(it.data!!)

                }
                Resource.Status.ERROR->{
                    //binding.detailShimmerLayout.productDetailShimmerView.visibility= View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bindDetail(product: Product){
        binding.productTitle.text= product.name
        binding.productOrderCount.text= product.total_sales.toString()
        binding.productReviewCount.text= product.rating_count.toString()
        binding.productRatingText.text= product.average_rating
        //binding.productRating.rating = Float.parseFloat(product.getAverageRating());

        Glide.with(binding.root).load(product.images?.get(0)?.name).into(binding.productImageSlider)

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}