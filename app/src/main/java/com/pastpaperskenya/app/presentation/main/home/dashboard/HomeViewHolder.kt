package com.pastpaperskenya.app.presentation.main.home.dashboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.databinding.ItemHomeCategoryLayoutBinding

class HomeViewHolder(private val binding: ItemHomeCategoryLayoutBinding,
                     private val listener: HomeAdapter.ClickListener
): RecyclerView.ViewHolder(binding.root),
    View.OnClickListener{

    private lateinit var category: Category

    init {
        binding.categoryCardviewLayout.setOnClickListener(this)
    }

    fun bind(item: Category){
        this.category= item
        binding.categoryTitle.text= item.name
        Glide.with(binding.root).load(item.image?.src).into(binding.categoryImage)
    }

    override fun onClick(v: View?) {
        listener.onClick(category.id)
    }

}