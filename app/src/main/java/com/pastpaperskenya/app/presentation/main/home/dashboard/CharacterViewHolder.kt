package com.pastpaperskenya.app.presentation.main.home.dashboard

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.databinding.ItemHomeCategoryLayoutBinding

class CharacterViewHolder(private val itemBinding: ItemHomeCategoryLayoutBinding,
                          private val listener: HomeAdapter.CategoryClickListener)
    : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    private lateinit var category: Category

    init {
        itemBinding.root.setOnClickListener(this)
    }

    //@SuppressLint("SetTextI18n")
    fun bind(item: Category) {
        this.category = item
        itemBinding.categoryTitle.text = item.name
        Glide.with(itemBinding.categoryImage.context)
            .load(item.image)
            .into(itemBinding.categoryImage)
    }

    override fun onClick(v: View?) {
        listener.onClick(category.id)
    }
}