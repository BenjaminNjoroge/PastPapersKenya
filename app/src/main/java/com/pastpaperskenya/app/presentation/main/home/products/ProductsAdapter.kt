package com.pastpaperskenya.app.presentation.main.home.products

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.databinding.ItemGridProductListLayoutBinding

 class ProductsAdapter(private val listener: ClickListener) :
     ListAdapter<Product, ProductsAdapter.ProductsViewHolder>(ProductsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding= ItemGridProductListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ProductsViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val items = getItem(position)
        if (items != null) {
            holder.bind(items)
        }
    }

    interface ClickListener{
        fun onClick(id: Int)
    }
    class ProductsViewHolder( private val binding: ItemGridProductListLayoutBinding, private val listener: ClickListener):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        private lateinit var product: Product

            @SuppressLint("SetTextI18n")
            fun bind(product: Product){
                this.product= product

                Glide.with(binding.root).load(product.images?.get(0)?.src).into(binding.productImage)
                binding.productTitle.text= product.name

                val newSalePrice= product.regular_price?.toInt()?.minus(product.sale_price?.toInt()!!)?.times(100)
                val newRegularPrice= product.regular_price?.toInt()!!

                val percent= newSalePrice!!.div(newRegularPrice).toString() + "%"
                binding.productDiscountPercent.text= percent
                binding.productSalePrice.text= "Ksh "+product.sale_price
            }

        init {
            binding.productImage.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onClick(product.id)
        }

    }
    private class ProductsComparator: DiffUtil.ItemCallback<Product>(){

        override fun areItemsTheSame(oldItem: Product, newItem: Product)=
            oldItem.id== newItem.id


        override fun areContentsTheSame(oldItem: Product, newItem: Product)=
            oldItem== newItem
    }


}