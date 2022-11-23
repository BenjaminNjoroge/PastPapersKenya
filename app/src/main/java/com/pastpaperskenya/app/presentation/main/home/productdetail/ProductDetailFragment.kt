package com.pastpaperskenya.app.presentation.main.home.productdetail

import android.os.Bundle
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
import com.flutterwave.raveandroid.RaveUiManager
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.model.wishlist.WishList
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.databinding.FragmentProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private val viewModel: ProductDetailViewModel by viewModels()
    private var _binding: FragmentProductDetailBinding?= null
    private val binding get() = _binding!!

    private val args: ProductDetailFragmentArgs by navArgs()

    private lateinit var paymentMethod: String
    private lateinit var paymentTitle: String
    private lateinit var currentDateandTime: String

    private lateinit var billingPhone:String
    private lateinit var billingLastname:String
    private lateinit var billingFirstname: String
    private lateinit var billingEmail: String

    private lateinit var productImage: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentProductDetailBinding.inflate(inflater, container, false)

        viewModel.userDetails.observe(viewLifecycleOwner){ details->
            if (details.email.toString().isEmpty() || details.lastname.toString().isEmpty()
                || details.firstname.toString().isEmpty() || details.phone.toString().isEmpty()){

                
                findNavController().navigate(R.id.action_productDetailFragment_to_productUserAddressFragment)
            }
            else{

                val id= args.id
                viewModel.start(id)
            }
        }

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        currentDateandTime = sdf.format(Date())

        binding.apply {
            detailToolbar.backArrow.setOnClickListener{
                findNavController().popBackStack()
            }

        }
    }

    private fun setupObservers(){

        viewModel.userDetails.observe(viewLifecycleOwner){
            billingEmail= it.email.toString()
            billingFirstname= it.firstname.toString()
            billingLastname= it.lastname.toString()
            billingPhone= it.phone.toString()
        }

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

        val productId= product?.id
        val productName= product?.name
        val productRegularPrice= product?.regular_price
        val productPrice= product?.sale_price
        if (product?.images.isNullOrEmpty()){
            productImage= R.drawable.image_placeholder.toString()
        }else{
            productImage= product?.images!!.get(0)!!.src!!
        }
        val categoryIds= product?.categories?.get(0)?.id
        val productDescription= product?.description
        val productCount= product?.rating_count
        val productRating= product?.average_rating


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

        if(product?.images != null && product.images.size >= 1){
            Glide.with(binding.root).load(product.images.get(0)!!.src)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .fallback(R.drawable.image_placeholder)
                .into(binding.productImageSlider)
        }

        binding.paywithcard.setOnClickListener {
            paymentMethod = Constants.PAYMENT_METHOD_CARD;
            paymentTitle = getString(R.string.payment_title_card);

            val txRef = currentDateandTime;

            if (num2 != null) {
                RaveUiManager(requireParentFragment()).setAmount(num2.toDouble())
                    .setCurrency("KES")
                    .setCountry("KE")
                    .setEmail(billingEmail)
                    .setfName(billingFirstname)
                    .setlName(billingLastname)
                    .setPublicKey(Constants.FLUTTER_PUBLIC_KEY)
                    .setEncryptionKey(Constants.FLUTTER_ENCRYPTION_KEY)
                    //.setTxRef(txRef)
                    .setPhoneNumber(billingPhone, false)
                    .acceptCardPayments(true)
                    .allowSaveCardFeature(false)
                    .onStagingEnv(false)
                    .isPreAuth(false)
                    .shouldDisplayFee(true)
                    .showStagingLabel(false)
                    .initialize()
            }
        }

        binding.productFavourite.setOnClickListener {
            viewModel.addToWishlist(WishList(productId, productName, productRegularPrice, productPrice, productImage,
                categoryIds, productDescription, percent, productCount, productRating
            ))
        }

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