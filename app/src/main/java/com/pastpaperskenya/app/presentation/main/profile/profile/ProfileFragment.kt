package com.pastpaperskenya.app.presentation.main.profile.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.github.siyamed.shapeimageview.CircularImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.business.util.enums.ProfileTabs
import com.pastpaperskenya.app.databinding.FragmentProfileBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    //private lateinit var navHostController: NavController

    private val viewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentProfileBinding?= null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: String

    private lateinit var profileImage: CircularImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val navHostFragment= childFragmentManager.findFragmentById(R.id.profile_fragments_container) as NavHostFragment
        //navHostController= navHostFragment.navController

        firebaseAuth= FirebaseAuth.getInstance()
        firebaseUser= firebaseAuth.currentUser?.uid.toString()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentProfileBinding.inflate(inflater, container, false)

        (activity as MainActivity).supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_TITLE

        registerObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImage= view.findViewById(R.id.ivProfileImageP)


        clickListeners()

        binding.viewpager.adapter= PagerAdapter(this)
        viewPager2SetupWithTabLayout(
            tablayout= binding.tabLayout,
            viewpager= binding.viewpager
        )
    }

    private fun viewPager2SetupWithTabLayout(tablayout: TabLayout, viewpager: ViewPager2) {
        TabLayoutMediator(tablayout, viewpager){ tab, position->
            val view= LayoutInflater.from(requireContext()).inflate(R.layout.item_tab_layout, null)
            val textView: TextView= view.findViewById(R.id.tabTitle)

            when(position){
                ProfileTabs.MYORDERS.index->{
                    tab.customView= view
                    textView.text= "MYORDERS"
                    tab.onSelection(true)
                }
                ProfileTabs.WISHLIST.index->{
                    tab.customView= view
                    textView.text= "WISHLIST"
                    tab.onSelection(false)
                }
            }
        }.attach()
    }

    inline fun TabLayout.onTabSelectionListener(
        crossinline onTabSelected: (TabLayout.Tab?) -> Unit = {},
        crossinline onTabUnselected: (TabLayout.Tab?) -> Unit? = {},
    ) {
        addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.onSelection(true)
                onTabSelected.invoke(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.onSelection(false)
                onTabUnselected.invoke(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    fun TabLayout.Tab.onSelection(isSelected: Boolean = true) {
        val textViewTitle: TextView? = customView?.findViewById<TextView>(R.id.tabTitle)
        textViewTitle?.let { textView ->
            textView.setTextColor(
                ContextCompat.getColor(
                    textView.context,
                    if (isSelected) R.color.black else R.color.shimmerDark
                )
            )
        }
    }

    private fun clickListeners(){
        binding.apply {
            editButton.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            }
        }
    }

    private fun registerObservers(){

        viewModel.userProfile.observe(viewLifecycleOwner) {
            binding.tvUserNameP.text = it.firstname + " " + it.lastname
            binding.tvEmailP.text = it.email
            binding.tvPhone.text = it.phone
            binding.tvLastname.text = it.lastname
            binding.tvFirstname.text = it.firstname
            binding.tvCountry.text = it.country
            binding.tvCounty.text = it.county
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect{ events->
                when(events){
                    is AuthEvents.Message->{
                        //nothing
                    }
                    is AuthEvents.ErrorCode->{
                        //nothing
                    }
                    is AuthEvents.Error->{
                        (events.message)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding= null
    }

}