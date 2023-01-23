package com.pastpaperskenya.app.presentation.main.cart.checkout

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.mpesa.MpesaStatus
import com.pastpaperskenya.app.business.model.mpesa.Payment
import com.pastpaperskenya.app.business.model.orders.CreateOrder
import com.pastpaperskenya.app.business.model.orders.OrderBillingProperties
import com.pastpaperskenya.app.business.model.orders.OrderLineItems
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.sanitizePhoneNumber
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import com.pastpaperskenya.app.databinding.FragmentCheckoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

private const val TAG = "CheckoutFragment"

@AndroidEntryPoint
class CheckoutFragment : Fragment() {


    private val viewModel: CheckoutViewModel by viewModels()
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CheckoutAdapter

    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var mBehavior: BottomSheetBehavior<*>

    private lateinit var billingEmail: String
    private lateinit var billingFirstname: String
    private lateinit var billingLastname: String
    private lateinit var billingPhone: String
    private lateinit var billingCounty: String
    private lateinit var billingCountry: String

    private lateinit var accessToken: String

    private lateinit var mProgressDialog: ProgressDialog

    private val progressStatus = 120

    private var netTotalAmount = 0
    private var customerId = 0

    private val lineItems = ArrayList<OrderLineItems>()

    private var orderId: Int= 0

    private var paymentSuccess: String?=null
    private var paymentError: String?= null
    private var checkout_id: String?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)


        viewModel.userResponse.observe(viewLifecycleOwner) { details ->
            if (details.email!!.isEmpty() || details.lastname.toString().isEmpty()
                || details.firstname.toString().isEmpty() || details.phone.toString().isEmpty()
            ) {

                findNavController().navigate(R.id.action_checkoutFragment_to_userAddressFragment)
            }
        }

        mProgressDialog = ProgressDialog(requireContext());

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = CheckoutAdapter()

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.checkoutProducts.layoutManager = linearLayoutManager
        binding.checkoutProducts.adapter = adapter

        viewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            binding.productSubtotalPrice.text = "Total Ksh: $total"
            binding.paymentTotalPrice.text = "Total Ksh: $total"
            if (total != null) {
                netTotalAmount = total
            }
        }

        viewModel.userResponse.observe(viewLifecycleOwner) { details ->

            billingEmail = details.email.toString()
            billingFirstname = details.firstname.toString()
            billingLastname = details.lastname.toString()
            billingPhone = details.phone.toString()
            billingCounty = details.county.toString()
            billingCountry = details.country.toString()
            customerId = details.userServerId!!

        }
        viewModel.cartResponse.observe(viewLifecycleOwner) { items ->

            if (!items.isNullOrEmpty()) {
                adapter.submitList(items)

                for (item in items) {
                    lineItems.add(
                        OrderLineItems(
                            1,
                            item.productId,
                            item.productName,
                            item.totalPrice,
                            item.totalPrice
                        )
                    )
                }
            }
        }

        clickListeners()

        listenToChannels()
    }


    private fun showPaymentProcessingDialog() {
        mProgressDialog.setMessage("Processing your request")
        mProgressDialog.setTitle("Please Wait... " + progressStatus.toString() + "sec")
        mProgressDialog.setIndeterminate(true)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message = data.getStringExtra("response")
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                //placeOrderWithCard()

                //Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(requireContext(), "ERROR $message", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(requireContext(), "CANCELLED $message", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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

    private fun clickListeners() {

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

                    val order= CreateOrder(null, null, customerId,"Mpesa", "Paid with mpesa", false, orderBillingProperties, lineItems)
                    viewModel.createOrder(order)
                    viewModel.getMpesaToken()

                    registerObservers()
                }
            })

            mBottomSheetDialog.setCanceledOnTouchOutside(false)
            mBottomSheetDialog.show()
        }

        binding.changeAddress.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutFragment_to_userAddressFragment)
        }
    }

    private fun listenToChannels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { events ->
                when (events) {

                    is AuthEvents.Message -> {
                        binding.pbLoading.isInvisible = true
                        Toast.makeText(requireContext(), events.message, Toast.LENGTH_SHORT).show()
                    }
                    is AuthEvents.Error -> {

                        binding.pbLoading.isInvisible = true
                        Toast.makeText(requireContext(), events.message, Toast.LENGTH_SHORT).show()
                    }
                    is AuthEvents.ErrorCode -> {

                        if (events.code== 101){

                        }

                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventBus(mpesa: MpesaStatus){
        Log.d(TAG, "onEventBus: Displaying event"+ mpesa.status)

        if (mpesa.status == "completed"){
            viewModel.deleteAllCart()
            Toast.makeText(requireContext(), "Order ${mpesa.status}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_checkoutFragment_to_orderConfirmedFragment)
        } else{
            Toast.makeText(requireContext(), "Order ${mpesa.status}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_checkoutFragment_to_orderFailedFragment)
        }

    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}