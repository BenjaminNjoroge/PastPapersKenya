package com.pastpaperskenya.app.presentation.main.cart.checkout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBar
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
import com.pastpaperskenya.app.business.model.mpesa.MpesaTokenResponse
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.network.NetworkChangeReceiver
import com.pastpaperskenya.app.databinding.FragmentCheckoutBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "CheckoutFragment"

@AndroidEntryPoint
class CheckoutFragment : Fragment() {


    private val viewModel: CheckoutViewModel by viewModels()
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CheckoutAdapter
    private var netTotalAmount: Int = 0

    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var mBehavior: BottomSheetBehavior<*>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_TITLE

        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)

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
            }

            for (item in items) {
                netTotalAmount += Integer.parseInt(item.productPrice.toString())
            }

            binding.productSubtotalPrice.text = "Ksh " + netTotalAmount.toString()
            binding.paymentTotalPrice.text = "Ksh " + netTotalAmount.toString()

        }

        viewModel.userResponse.observe(viewLifecycleOwner) { details ->

            val billingEmail = details.email.toString()
            val billingFirstname = details.firstname.toString()
            val billingLastname = details.lastname.toString()
            val billingPhone = details.phone.toString()

            binding.email.text= details.email
            Toast.makeText(requireContext(), billingEmail, Toast.LENGTH_SHORT).show()

        }

        binding.paywithcard.setOnClickListener {
            Log.d(TAG, "onViewCreated: onclick")


        }

            binding.paywithmpesa.setOnClickListener {
                showPaymentSheet()
            }

            binding.changeAddress.setOnClickListener {
                findNavController().navigate(R.id.action_placeOrderFragment_to_userAddressFragment)
            }

        }

        private fun ravePayWithCard(email: String, phone: String, firstname: String, lastname: String, amount: Double
        ) {
            RaveUiManager(requireParentFragment()).setAmount(amount)
                .setCurrency("KES")
                .setCountry("KE")
                .setEmail(email)
                .setfName(firstname)
                .setlName(lastname)
                .setPublicKey(Constants.FLUTTER_PUBLIC_KEY)
                .setEncryptionKey(Constants.FLUTTER_ENCRYPTION_KEY)
                //.setTxRef(txRef)
                .setPhoneNumber(phone, false)
                .acceptCardPayments(true)
                .allowSaveCardFeature(false)
                .onStagingEnv(false)
                .isPreAuth(false)
                .shouldDisplayFee(true)
                .showStagingLabel(false)
                .initialize()
        }


        override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
            /*
             *  We advise you to do a further verification of transaction's details on your server to be
             *  sure everything checks out before providing service or goods.
             */
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
            amount_tv.text = "Pay Ksh $netTotalAmount"
            val phoneNo = view.findViewById<EditText>(R.id.et_Phone)
            val payNow_Btn: Button = view.findViewById(R.id.btnPay)

//        if (billingAddress.get(KEY_PHONE) != null) {
//            phoneNo.setText(billingAddress.get(KEY_PHONE))
//        }
            payNow_Btn.setOnClickListener(View.OnClickListener {
                val mobileNo = phoneNo.text.toString().trim { it <= ' ' }
                if (mobileNo.isEmpty()) {
                    phoneNo.error = "Enter Phone No"
                    phoneNo.requestFocus()
                } else if (mobileNo.length < 10) {
                    phoneNo.error = "Enter valid Phone No"
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


    }