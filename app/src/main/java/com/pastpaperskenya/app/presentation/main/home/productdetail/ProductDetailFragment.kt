package com.pastpaperskenya.app.presentation.main.home.productdetail

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.databinding.FragmentProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private val viewModel: ProductDetailViewModel by viewModels()
    private var _binding: FragmentProductDetailBinding?= null
    private val binding get() = _binding!!

    private val args: ProductDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentProductDetailBinding.inflate(inflater, container, false)

        val id= args.id

        viewModel.start(id)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        binding.detailToolbar.backArrow.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun setupObservers(){

        viewModel.response.observe(viewLifecycleOwner){
            when(it.status){
                 Resource.Status.LOADING->{
                     binding.pbLoading.visibility= View.VISIBLE
                 }
                Resource.Status.SUCCESS->{
                    binding.pbLoading.visibility= View.GONE

                    bindDetails(it.data)

                }
                Resource.Status.ERROR->{
                    binding.pbLoading.visibility= View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun bindDetails(product: Product?){
        binding.productTitle.text= product?.name

        binding.productShortDescription.text= product?.description
        binding.productRegularPrice.text= "Ksh "+ product?.regular_price
        binding.productSalePrice.text= "Ksh "+ product?.sale_price
        binding.productRatingText.text= product?.average_rating.toString()
        binding.paymentTotalPrice.text= "Ksh "+ product?.sale_price
        binding.productPrice.text= "Ksh "+product?.sale_price


        val num1= product?.regular_price?.let { it1 -> convertIntoNumeric(it1) }
        val num2= product?.sale_price?.let { it1 -> convertIntoNumeric(it1) }
        val newSalePrice= num1?.minus(num2!!)?.times(100)

        val newRegularPrice= product?.regular_price?.let { it1 -> convertIntoNumeric(it1) }

        val percent= newRegularPrice?.let { newSalePrice?.div(it).toString() } + "%"
        binding.productDiscountPercent.text= "OFF $percent"
        binding.productReviewCount.text= product?.rating_count.toString()

        //Glide.with(binding.root).load(it.data?.images?.get(0)?.src).into(binding.productImageSlider)
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