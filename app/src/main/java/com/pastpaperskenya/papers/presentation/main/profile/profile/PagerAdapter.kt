package com.pastpaperskenya.papers.presentation.main.profile.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pastpaperskenya.papers.business.util.enums.ProfileTabs
import com.pastpaperskenya.papers.presentation.main.profile.profile.myorders.MyOrdersFragment
import com.pastpaperskenya.papers.presentation.main.profile.profile.wishlist.WishListFragment

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = ProfileTabs.values().size

    override fun createFragment(position: Int): Fragment {
        return when(position){
            ProfileTabs.MYORDERS.index -> MyOrdersFragment.newInstance(ProfileTabs.MYORDERS.name)
            ProfileTabs.WISHLIST.index -> WishListFragment.newInstance(ProfileTabs.WISHLIST.name)
                else -> throw IllegalStateException("Fragment not found")
        }
    }

}