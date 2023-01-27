package com.pastpaperskenya.papers.presentation.main.home.products

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.papers.R
import com.pastpaperskenya.papers.business.model.cart.Cart
import com.pastpaperskenya.papers.business.model.product.Product
import com.pastpaperskenya.papers.business.util.convertIntoNumeric
import com.pastpaperskenya.papers.databinding.ItemGridProductListLayoutBinding

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
        fun onClick(id: Int, name: String?)
        fun addToCart(cart: Cart)
        fun removeFromCart(productId: Int)
    }

    class ProductsViewHolder( private val binding: ItemGridProductListLayoutBinding,
                              private val listener: ClickListener):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        private lateinit var product: Product

            @SuppressLint("SetTextI18n")
            fun bind(product: Product){
                this.product= product

                if(product.images != null && product.images.size >= 1){
                    Glide.with(binding.root)
                        .load(product.images.get(0)!!.src)
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                        .fallback(R.drawable.image_placeholder)
                        .into(binding.productImage)

                }
                binding.productTitle.text= product.name

                val num1= product.regular_price?.let { it1 -> convertIntoNumeric(it1) }
                val num2= product.sale_price?.let { it1 -> convertIntoNumeric(it1) }
                val newSalePrice= num1?.minus(num2!!)?.times(100)

                val newRegularPrice= product.regular_price?.let { it1 -> convertIntoNumeric(it1) }

                val percent= newSalePrice!!.div(newRegularPrice!!).toString() + "%"
                binding.productDiscountPercent.text= "OFF $percent"
                binding.productSalePrice.text= "Ksh "+product.sale_price
                binding.productRegularPrice.text= "Ksh "+product.regular_price
            }

        init {
            binding.productImage.setOnClickListener(this)
            binding.productTitle.setOnClickListener(this)
            binding.productDiscountPercent.setOnClickListener(this)
            binding.productRegularPrice.setOnClickListener(this)
            binding.productSalePrice.setOnClickListener(this)

            binding.checkCart.setOnClickListener {
                val productImage: String
                if (product.images.isNullOrEmpty()){
                    productImage= R.drawable.image_placeholder.toString()
                }else{
                    productImage= product.images!!.get(0)!!.src!!
                }
                listener.addToCart(Cart(product.id, product.name, product.price, product.sale_price, productImage,
                    product.categories?.get(0)?.id!!
                ))
            }
        }

        override fun onClick(v: View?) {
            listener.onClick(product.id, product.name)
        }

    }

    private class ProductsComparator: DiffUtil.ItemCallback<Product>(){

        override fun areItemsTheSame(oldItem: Product, newItem: Product)=
            oldItem.id== newItem.id


        override fun areContentsTheSame(oldItem: Product, newItem: Product)=
            oldItem== newItem
    }

}