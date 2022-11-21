package com.pastpaperskenya.app.presentation.main.profile.profile.wishlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.wishlist.WishList
import com.pastpaperskenya.app.databinding.ItemProductCartLayoutBinding
import com.pastpaperskenya.app.databinding.ItemWishlistProductLayoutBinding

class WishlistAdapter constructor(private val listener: RemoveWishlistItemClickListener):
    ListAdapter<WishList, WishlistAdapter.WishlistViewHolder>(WishlistViewHolder.WishlistComparator()) {

    interface RemoveWishlistItemClickListener {
        fun removeItem(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val binding =
            ItemWishlistProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishlistViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val items = getItem(position)

        if (items != null) {
            holder.bind(items)
        }
    }

    class WishlistViewHolder(
        private val binding: ItemWishlistProductLayoutBinding,
        private val listener: RemoveWishlistItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var wishList: WishList


        init {
            binding.removeProduct.setOnClickListener(this)
        }

        fun bind(wishList: WishList) {
            this.wishList = wishList

            binding.apply {
                productTitle.text = wishList.productName
                productSalePrice.text = "Ksh "+wishList.productPrice
                Glide.with(binding.root).load(wishList.productImage).into(binding.productImage)
            }
        }

        override fun onClick(view: View?) {
            listener.removeItem(wishList.productId)
        }

        class WishlistComparator : DiffUtil.ItemCallback<WishList>() {
            override fun areItemsTheSame(oldItem: WishList, newItem: WishList) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: WishList, newItem: WishList) =
                oldItem == newItem

        }
    }
}