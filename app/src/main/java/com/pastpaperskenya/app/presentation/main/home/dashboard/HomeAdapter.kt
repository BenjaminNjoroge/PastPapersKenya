package com.pastpaperskenya.app.presentation.main.home.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.databinding.ItemHomeCategoryLayoutBinding


class HomeAdapter constructor(private val listener: ClickListener): RecyclerView.Adapter<HomeViewHolder>() {


    interface ClickListener{
        fun onClick(categoryId: Int)
    }

    private val categories= ArrayList<Category>()

    fun setItems(categories: ArrayList<Category>){
        this.categories.clear()
        this.categories.addAll(categories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding: ItemHomeCategoryLayoutBinding = ItemHomeCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) =
        holder.bind(categories[position])


    override fun getItemCount(): Int {
        return categories.size
    }


}

