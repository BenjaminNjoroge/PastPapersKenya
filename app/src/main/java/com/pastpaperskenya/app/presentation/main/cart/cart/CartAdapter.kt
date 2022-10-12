package com.pastpaperskenya.app.presentation.main.cart.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.databinding.ItemProductCartLayoutBinding

class CartAdapter constructor(private val listener: RemoveCartItemClickListener):
    ListAdapter<Cart, CartAdapter.CartViewHolder>(CartViewHolder.CartComparator()) {

    interface RemoveCartItemClickListener {
        fun removeItem(position: Int, cart: Cart?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            ItemProductCartLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val items = getItem(position)

        if (items != null) {
            holder.bind(items)
        }
    }

    class CartViewHolder(
        private val binding: ItemProductCartLayoutBinding,
        private val listener: RemoveCartItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var cart: Cart


        init {
            binding.removeProduct.setOnClickListener(this)
        }

        fun bind(cart: Cart) {
            this.cart = cart

            binding.apply {
                productTitle.text = cart.productName
                productSalePrice.text = cart.productPrice
                productSubtotalPrice.text = cart.productPrice
                Glide.with(binding.root).load(cart.productImage).into(binding.productImage)
            }
        }

        override fun onClick(view: View?) {
            listener.removeItem(absoluteAdapterPosition, cart)
        }

        class CartComparator : DiffUtil.ItemCallback<Cart>() {
            override fun areItemsTheSame(oldItem: Cart, newItem: Cart) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Cart, newItem: Cart) =
                oldItem == newItem

        }
    }
}