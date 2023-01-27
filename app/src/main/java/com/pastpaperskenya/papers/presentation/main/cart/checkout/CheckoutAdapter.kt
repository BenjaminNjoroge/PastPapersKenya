package com.pastpaperskenya.papers.presentation.main.cart.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.papers.business.model.cart.Cart
import com.pastpaperskenya.papers.databinding.ItemCheckoutProductLayoutBinding

class CheckoutAdapter : ListAdapter<Cart, CheckoutAdapter.CartViewHolder>(CartComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding= ItemCheckoutProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val items= getItem(position)
        if (items !=null){
            holder.bind(items)
        }
    }

    class CartViewHolder(private val binding: ItemCheckoutProductLayoutBinding) :
        RecyclerView.ViewHolder(binding.root){

        private lateinit var items: Cart


        fun bind(cart: Cart){
            this.items= cart
            binding.apply {
                productTitle.text= cart.productName
                productSalePrice.text= "Ksh " +cart.totalPrice
                productQuantity.text= "1 pcs"
                Glide.with(binding.root).load(cart.productImage).into(binding.productImage)
            }
        }

    }

    class CartComparator : DiffUtil.ItemCallback<Cart>() {

        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean =
            oldItem.productId == newItem.productId


        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean =
            oldItem == newItem

    }
}