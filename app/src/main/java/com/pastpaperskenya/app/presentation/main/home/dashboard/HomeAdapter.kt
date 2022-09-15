package com.pastpaperskenya.app.presentation.main.home.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.databinding.ItemHomeCategoryLayoutBinding


class HomeAdapter : ListAdapter<Category, HomeAdapter.ViewHolder>(CategoryComparator()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= ItemHomeCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem= getItem(position)
        if (currentItem != null){
            holder.bind(currentItem)
        }
    }

    class ViewHolder(private val binding: ItemHomeCategoryLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(category: Category){
            binding.apply {
                categoryTitle.text= category.name
                Glide.with(binding.root).load(category.image?.src).into(categoryImage)
            }
        }

    }

    class CategoryComparator: DiffUtil.ItemCallback<Category>(){

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem== newItem
        }

    }



}

