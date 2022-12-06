package com.pastpaperskenya.app.presentation.main.profile.profile.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.flutterwave.raveandroid.RaveUiManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.model.wishlist.WishList
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import com.pastpaperskenya.app.databinding.FragmentProductDetailBinding
import com.pastpaperskenya.app.databinding.FragmentWishlistProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ProductWishlistDetailFragment : Fragment() {

    private val viewModel: ProductWishlistDetailViewModel by viewModels()
    private var _binding: FragmentWishlistProductDetailBinding?= null
    private val binding get() = _binding!!

    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var mBehavior: BottomSheetBehavior<*>

    private val args: ProductWishlistDetailFragmentArgs by navArgs()

    private lateinit var paymentMethod: String
    private lateinit var paymentTitle: String
    private lateinit var currentDateandTime: String

    private lateinit var billingPhone:String
    private lateinit var billingLastname:String
    private lateinit var billingFirstname: String
    private lateinit var billingEmail: String

    private var netTotalAmount= 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentWishlistProductDetailBinding.inflate(inflater, container, false)

        val id= args.id

        viewModel.start(id)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        currentDateandTime = sdf.format(Date())

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
                NetworkResult.Status.LOADING->{
                     binding.pbLoading.visibility= View.VISIBLE
                 }
                NetworkResult.Status.SUCCESS->{
                    binding.pbLoading.visibility= View.GONE

                    bindDetails(it.data)

                }
                NetworkResult.Status.ERROR->{
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

        Glide.with(binding.root).load(product?.images?.get(0)?.src).into(binding.productImageSlider)

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

        netTotalAmount= product?.sale_price!!.toInt()

        binding.paywithmpesa.setOnClickListener {
            showPaymentSheet()
        }

    }

    private fun showPaymentSheet() {
        mBottomSheetDialog = BottomSheetDialog(requireContext())
        val view: View = layoutInflater.inflate(
            R.layout.mpesa_payment_sheet,
            requireActivity().getWindow().getDecorView().getRootView() as ViewGroup,
            false
        )
        mBottomSheetDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)

        mBottomSheetDialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)

        val img_close: ImageView = view.findViewById(R.id.img_close)
        img_close.setOnClickListener(View.OnClickListener { mBottomSheetDialog.dismiss() })
        val amount_tv = view.findViewById<TextView>(R.id.tv_Title)
        amount_tv.text = "Pay $netTotalAmount"+"ksh"

        val phoneNo = view.findViewById<EditText>(R.id.et_Phone)
        val payNow_Btn: Button = view.findViewById(R.id.btnPay)

        if (!billingPhone.isNullOrEmpty()) {
            phoneNo.setText(billingPhone)
        }
        payNow_Btn.setOnClickListener(View.OnClickListener {
            val mobileNo = phoneNo.text.toString().trim { it <= ' ' }
            if (mobileNo.isEmpty()) {
                phoneNo.error = "Enter Phone No"
                phoneNo.requestFocus()
            } else if (mobileNo.length < 10) {
                phoneNo.error = "Phone No too small"
                phoneNo.requestFocus()
            } else if (mobileNo.startsWith("0") && mobileNo.length != 10) {
                phoneNo.error = "Enter valid Phone No"
                phoneNo.requestFocus()
            } else if (mobileNo.startsWith("254") && mobileNo.length != 12) {
                phoneNo.error = "Enter valid Phone No"
                phoneNo.requestFocus()
            } else if (!mobileNo.startsWith("0") && !mobileNo.startsWith("254") && mobileNo.length != 9) {
                phoneNo.error = "Enter valid Phone No"
                phoneNo.requestFocus()
            } else {
                mBottomSheetDialog.dismiss()
                //generateToken(false, mobileNo)
            }
        })

        mBottomSheetDialog.setCanceledOnTouchOutside(false)
        mBottomSheetDialog.show()
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