package com.pastpaperskenya.app.presentation.main.home.subcategory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.databinding.ItemSubCategoryLayoutBinding

class SubCategoryViewHolder(private val binding: ItemSubCategoryLayoutBinding,
                            private val listener: SubCategoryAdapter.ClickListener)
    : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var category: Category

    fun bind(item: Category){
        this.category= item
        binding.categoryTitle.text= item.name
        Glide.with(binding.root).load(item.image?.src).into(binding.categoryImage)
    }

    override fun onClick(v: View?) {
        listener.onClick(category.id)
    }

}