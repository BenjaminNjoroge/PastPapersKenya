package com.pastpaperskenya.app.presentation.main.home.subcategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.databinding.ItemSubCategoryLayoutBinding

class SubCategoryAdapter(private val listener: ClickListener)
    :RecyclerView.Adapter<SubCategoryViewHolder>() {

    interface ClickListener{
        fun onClick(categoryId: Int)
    }

    private val categories= ArrayList<HomeCategory>()

    fun setItems(items: ArrayList<HomeCategory>){
        this.categories.clear()
        this.categories.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val binding:ItemSubCategoryLayoutBinding = ItemSubCategoryLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent,false)
        return SubCategoryViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}