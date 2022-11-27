package com.pastpaperskenya.app.presentation.main.cart.checkout

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.orders.CreateOrder
import com.pastpaperskenya.app.business.model.orders.OrderBillingProperties
import com.pastpaperskenya.app.business.model.orders.OrderLineItems
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.databinding.FragmentCheckoutBinding
import dagger.hilt.android.AndroidEntryPoint


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
    private lateinit var billingFirstname:String
    private lateinit var billingLastname: String
    private lateinit var billingPhone: String
    private lateinit var billingCounty: String
    private lateinit var billingCountry: String

    private lateinit var mProgressDialog:ProgressDialog

    private val progressStatus = 120

    private var netTotalAmount= 0
    private var productId: Int= 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)

        viewModel.userResponse.observe(viewLifecycleOwner){ details->
            if (details.email!!.isEmpty() || details.lastname.toString().isEmpty()
                || details.firstname.toString().isEmpty() || details.phone.toString().isEmpty()){

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

        viewModel.cartResponse.observe(viewLifecycleOwner) { items ->

            if (!items.isNullOrEmpty()) {
                adapter.submitList(items)

                for(item in items){
                    productId= item.productId
                }
            }
        }

        viewModel.totalPrice.observe(viewLifecycleOwner){ total->
            binding.productSubtotalPrice.text = "Total Ksh: $total"
            binding.paymentTotalPrice.text = "Total Ksh: $total"
            if (total != null) {
                netTotalAmount= total
            }
        }

        viewModel.userResponse.observe(viewLifecycleOwner) { details ->

             billingEmail = details.email.toString()
             billingFirstname = details.firstname.toString()
             billingLastname = details.lastname.toString()
             billingPhone = details.phone.toString()
            billingCounty= details.county.toString()
            billingCountry= details.country.toString()

        }

        viewModel.orderResponse.observe(viewLifecycleOwner){
            when(it.status){
                Resource.Status.LOADING->{
                    binding.pbLoading.visibility= View.VISIBLE
                }
                Resource.Status.SUCCESS->{
                    binding.pbLoading.visibility= View.GONE
                    Toast.makeText(context, "Order success", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_checkoutFragment_to_orderConfirmedFragment)

                }
                Resource.Status.ERROR->{
                    binding.pbLoading.visibility= View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

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
                showPaymentSheet()
            }

            binding.changeAddress.setOnClickListener {
                findNavController().navigate(R.id.action_checkoutFragment_to_userAddressFragment)
            }

        }


    private fun showPaymentProcessingDialog() {
        mProgressDialog.setMessage("Processing your request")
        mProgressDialog.setTitle("Please Wait... " + progressStatus.toString() + "sec")
        mProgressDialog.setIndeterminate(true)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
//        // Start lengthy operation in a background thread
//        Thread {
//            while (progressStatus > 1) {
//                try {
//                    // Here I'm making thread sleep to show progress
//                    Thread.sleep(1000)
//                    progressStatus -= 1
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//                // Update the progress bar
//                handler.post(Runnable { mProgressDialog.setTitle("Please Wait... " + progressStatus.toString() + "sec") })
//            }
//            mProgressDialog.dismiss()
//            progressStatus = timerCount
//        }.start()
    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {

            if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
                val message = data.getStringExtra("response")
                if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                    // placeOrderWithCard()

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

                    binding.pbLoading.visibility= View.VISIBLE
                    val orderBillingProperties= OrderBillingProperties(billingFirstname, billingLastname, billingCounty, billingCountry, billingEmail, billingPhone)

                    val orderLineItems= OrderLineItems(1, productId, null)
                    val lineItems= ArrayList<OrderLineItems>()

                    lineItems.add(orderLineItems)


                    val order= CreateOrder("Mpesa", "Paid with mpesa", true, orderBillingProperties, lineItems)
                    createNewOrder(order)
                }
            })

            mBottomSheetDialog.setCanceledOnTouchOutside(false)
            mBottomSheetDialog.show()
        }


    private fun createNewOrder(order: CreateOrder){
        viewModel.createOrder(order)
    }

    }