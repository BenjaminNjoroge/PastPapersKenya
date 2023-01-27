package com.pastpaperskenya.papers.presentation.main.home.productdetail

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.papers.R
import com.pastpaperskenya.papers.business.model.mpesa.MpesaStatus
import com.pastpaperskenya.papers.business.model.mpesa.Payment
import com.pastpaperskenya.papers.business.model.orders.CreateOrder
import com.pastpaperskenya.papers.business.model.orders.OrderBillingProperties
import com.pastpaperskenya.papers.business.model.orders.OrderLineItems
import com.pastpaperskenya.papers.business.model.product.Product
import com.pastpaperskenya.papers.business.model.wishlist.WishList
import com.pastpaperskenya.papers.business.util.Constants
import com.pastpaperskenya.papers.business.util.convertIntoNumeric
import com.pastpaperskenya.papers.business.util.sanitizePhoneNumber
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import com.pastpaperskenya.papers.databinding.FragmentProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "ProductDetailFragment"
@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private val viewModel: ProductDetailViewModel by viewModels()
    private var _binding: FragmentProductDetailBinding?= null
    private val binding get() = _binding!!

    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var mBehavior: BottomSheetBehavior<*>

    private val args: ProductDetailFragmentArgs by navArgs()

    private lateinit var paymentMethod: String
    private lateinit var paymentTitle: String
    private lateinit var currentDateandTime: String

    private lateinit var billingPhone:String
    private lateinit var billingLastname:String
    private lateinit var billingFirstname: String
    private lateinit var billingEmail: String
    private lateinit var billingCounty: String
    private lateinit var billingCountry: String
    private lateinit var accessToken: String
    private lateinit var pName: String

    private lateinit var productImage: String

    private var netTotalAmount= 0
    private var customerId = 0
    private var orderId: Int= 0
    private var pId: Int= 0

    private var checkout_id: String?=null
    private val lineItems = ArrayList<OrderLineItems>()


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

        clickListeners()
    }

    private fun setupObservers(){

        viewModel.userDetails.observe(viewLifecycleOwner){
            billingEmail= it.email.toString()
            billingFirstname= it.firstname.toString()
            billingLastname= it.lastname.toString()
            billingPhone= it.phone.toString()
            billingCountry= it.country.toString()
            billingCounty= it.county.toString()
            customerId= it.userServerId!!
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
        if (product != null) {
            pId= product.id
        }
        pName= product?.name.toString()
        val productId= product?.id
        val productName= product?.name
        val productRegularPrice= product?.regular_price
        val productPrice= product?.sale_price

        netTotalAmount= convertIntoNumeric(product?.sale_price.toString())

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

        binding.productFavourite.setOnClickListener {
            viewModel.addToWishlist(WishList(productId, productName, productRegularPrice, productPrice, productImage,
                categoryIds, productDescription, percent, productCount, productRating
            ))
        }

        binding.changeAddress.setOnClickListener {
            findNavController().navigate(R.id.action_productDetailFragment_to_productUserAddressFragment)
        }

        binding.linLayout6.visibility= View.VISIBLE
    }

    private fun registerObservers() {

        viewModel.orderResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkResult.Status.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
                NetworkResult.Status.SUCCESS -> {
                    binding.pbLoading.visibility = View.GONE

                    Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show()
                    orderId= it.data?.orderId!!


                }
                NetworkResult.Status.ERROR -> {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.mpesaTokenResponse.observe(viewLifecycleOwner){
            when(it.status){
                NetworkResult.Status.LOADING->{
                    binding.pbLoading.visibility= View.VISIBLE
                }
                NetworkResult.Status.SUCCESS->{
                    binding.pbLoading.visibility= View.GONE
                    Toast.makeText(context, "Sending request", Toast.LENGTH_SHORT).show()
                    accessToken= it.data?.mpesaTokenData?.token.toString()

                    viewModel.createStkpush(netTotalAmount.toString(),
                        sanitizePhoneNumber(billingPhone), orderId.toString(),
                        accessToken
                    )
                }
                NetworkResult.Status.ERROR->{
                    binding.pbLoading.visibility= View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

                }
            }
        }

        viewModel.stkpushResponse.observe(viewLifecycleOwner){
            when(it.status){
                NetworkResult.Status.LOADING->{
                    binding.pbLoading.visibility= View.VISIBLE

                }
                NetworkResult.Status.SUCCESS->{
                    binding.pbLoading.visibility= View.GONE
                    Toast.makeText(context, "Enter mpesa pin", Toast.LENGTH_SHORT).show()

                    val checkoutId= it.data?.mpesaPaymentReqResponseData?.checkoutRequestID
                    val merchantRequestId= it.data?.mpesaPaymentReqResponseData?.merchantRequestID
                    val firebaseId= FirebaseAuth.getInstance().currentUser?.uid
                    val email= FirebaseAuth.getInstance().currentUser?.email

                    checkout_id= checkoutId

                    val firestoreDetails= Payment(checkoutId, customerId.toString(), null, merchantRequestId,
                        orderId.toString(), null, null, null, null, firebaseId, email)

                    viewModel.savePendingPaymentFirestore(firestoreDetails)

                }
                NetworkResult.Status.ERROR->{
                    binding.pbLoading.visibility= View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

                }
            }
        }

    }

    private fun clickListeners(){

        binding.paywithcard.setOnClickListener {
            Log.d(TAG, "onViewCreated: onclick")
            RaveUiManager(requireParentFragment()).setAmount(1.0)
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

        binding.paywithmpesa.setOnClickListener {

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
            amount_tv.text = "Pay $netTotalAmount" + "ksh"

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

                    binding.pbLoading.visibility = View.VISIBLE
                    val orderBillingProperties= OrderBillingProperties(billingFirstname, billingLastname, billingCounty, billingCountry, billingEmail, sanitizePhoneNumber(billingPhone))

                    lineItems.add(OrderLineItems(1, pId, pName, netTotalAmount.toString(), netTotalAmount.toString()))

                    val order= CreateOrder(null, null, customerId,"Mpesa", "Paid with mpesa", false, orderBillingProperties, lineItems)
                    viewModel.createOrder(order)
                    viewModel.getMpesaToken()

                    registerObservers()
                }
            })

            mBottomSheetDialog.setCanceledOnTouchOutside(false)
            mBottomSheetDialog.show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventBus(mpesa: MpesaStatus){
        Log.d(TAG, "onEventBus: Displaying event"+ mpesa.status)

        if (mpesa.status == "completed"){
            Toast.makeText(requireContext(), "Order ${mpesa.status}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_productDetailFragment_to_orderConfirmedFragment2)
        } else{
            Toast.makeText(requireContext(), "Order ${mpesa.status}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_productDetailFragment_to_orderFailedFragment2)
        }

    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        EventBus.getDefault().unregister(this)
    }
}