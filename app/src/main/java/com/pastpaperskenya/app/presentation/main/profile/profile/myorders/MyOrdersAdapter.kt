package com.pastpaperskenya.app.presentation.main.profile.profile.myorders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pastpaperskenya.app.business.model.orders.Orders
import com.pastpaperskenya.app.databinding.ItemMyOrdersLayoutBinding

class MyOrdersAdapter(private val listener: OrderClickListener) :
    ListAdapter<Orders, MyOrdersAdapter.MyOrdersViewHolder>(MyOrdersComparator()) {


    interface OrderClickListener{
        fun onClick(position: Int, id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {
        val binding= ItemMyOrdersLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyOrdersViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
        val items= getItem(position)
        if (items !=null){
            holder.bind(items)
        }
    }


    class MyOrdersViewHolder(private val binding: ItemMyOrdersLayoutBinding,
                             private val listener: OrderClickListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var items: Orders

        init {
            binding.parentView.setOnClickListener(this)
            binding.orderDetail.setOnClickListener(this)
        }

        fun bind(orders: Orders){
            this.items= orders
            binding.apply {
                orderId.text= orders.number
                orderDate.text= orders.date_completed_gmt
                totalNumProduct.text= orders.line_items?.get(0)?.quantity.toString()
                paymentAmount.text= orders.total
                paymentMethod.text= orders.payment_method_title
            }
        }

        override fun onClick(p0: View?) {
            listener.onClick(absoluteAdapterPosition, items.id)
        }

    }

    class MyOrdersComparator : DiffUtil.ItemCallback<Orders>() {

        override fun areItemsTheSame(oldItem: Orders, newItem: Orders): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Orders, newItem: Orders): Boolean =
            oldItem == newItem

    }
}