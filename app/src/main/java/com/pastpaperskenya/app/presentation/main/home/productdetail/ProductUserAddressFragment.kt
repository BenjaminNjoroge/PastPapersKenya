package com.pastpaperskenya.app.presentation.main.home.productdetail

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.hbb20.CountryCodePicker
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.user.CustomerBilling
import com.pastpaperskenya.app.business.model.user.CustomerUpdate
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.databinding.FragmentUserAddressBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductUserAddressFragment : Fragment() {

    private val viewModel: ProductUserAddressViewModel by viewModels()
    private var _binding: FragmentUserAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var email: String
    private lateinit var country: String
    private lateinit var county: String
    private lateinit var userServerId: String

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userId: String

    private lateinit var ccp: CountryCodePicker
    private lateinit var countySpinner: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        userId = firebaseAuth.currentUser?.uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAddressBinding.inflate(layoutInflater)

        (activity as MainActivity).supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_TITLE

        listenToChannels()
        registerObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.inputBillingEmail.keyListener = null
        email = binding.inputBillingEmail.text.toString()

        ccp = view.findViewById(R.id.input_billing_country);
        country = ccp.selectedCountryName

        countySpinner = view.findViewById(R.id.input_billing_county)
        val countiesAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.counties, android.R.layout.simple_list_item_1
        )
        countiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        countySpinner.adapter = countiesAdapter

        countySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                position: Int,
                id: Long
            ) {
                val ss: String = countySpinner.selectedItem.toString()
                county = ss
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }


        binding.saveAddress.setOnClickListener {
            val firstname = binding.inputBillingFirstName.text.toString()
            val lastname = binding.inputBillingLastName.text.toString()
            val phone = binding.inputBillingPhone.text.toString()

            viewModel.updateFirestoreDetails(userId, phone, firstname, lastname, country, county)
            viewModel.updateLocalDetails(
                phone,
                firstname,
                lastname,
                country,
                county,
                convertIntoNumeric(userServerId)
            )


            binding.rotateProgress.visibility = View.VISIBLE

            val billing= CustomerBilling(county, country, phone)
            val customer= CustomerUpdate(firstname, lastname, billing)

            viewModel.fieldsChecker(convertIntoNumeric(userServerId), phone, firstname, lastname, customer)


        }


    }

    private fun registerObservers() {

        viewModel.userProfile.observe(viewLifecycleOwner) { details ->
            binding.inputBillingFirstName.text =
                Editable.Factory.getInstance().newEditable(details.firstname)
            binding.inputBillingLastName.text =
                Editable.Factory.getInstance().newEditable(details.lastname)
            binding.inputBillingEmail.text =
                Editable.Factory.getInstance().newEditable(details.email)
            binding.inputBillingPhone.text =
                Editable.Factory.getInstance().newEditable(details.phone)

            userServerId = details.userServerId.toString()
        }

    }

    private fun listenToChannels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { events ->
                when (events) {
                    is AuthEvents.Message -> {
                        binding.rotateProgress.visibility = View.GONE
                        Toast.makeText(requireActivity(), events.message, Toast.LENGTH_SHORT).show()
                        //findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)

                    }
                    is AuthEvents.Error -> {
                        Toast.makeText(requireActivity(), events.message, Toast.LENGTH_SHORT).show()

                    }
                    is AuthEvents.ErrorCode -> {
                        if (events.code == 1) {
                            binding.apply {
                                rotateProgress.isInvisible = true
                                inputBillingFirstName.error = "Firstname cannot be empty"
                            }
                        }
                        if (events.code == 2) {
                            binding.apply {
                                rotateProgress.isInvisible = true
                                inputBillingLastName.error = "Lastname cannot be empty"
                            }
                        }
                        if (events.code == 3) {
                            binding.apply {
                                rotateProgress.isInvisible = true
                                inputBillingPhone.error = "Phone cannot be empty"
                            }
                        }
                        if (events.code == 100) {
                            binding.apply {
                                rotateProgress.isInvisible = true
                                findNavController().navigate(R.id.action_productUserAddressFragment_to_productDetailFragment)
                            }
                        }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}