package com.pastpaperskenya.app.presentation.main.home.subcategory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.category.SubCategory
import com.pastpaperskenya.app.databinding.ItemSubCategoryLayoutBinding

class SubCategoryAdapter(private val listener: ClickListener)
    : ListAdapter<SubCategory, SubCategoryAdapter.SubCategoryViewHolder>(CategoryComparator()) {

    interface ClickListener{
        fun onClick(categoryId: Int)
    }

    private val categories= ArrayList<SubCategory>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val binding= ItemSubCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubCategoryViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        val currentItem= getItem(position)
        if (currentItem !=null){
            holder.bind(currentItem)
        }
    }

    class SubCategoryViewHolder(private val binding: ItemSubCategoryLayoutBinding,
                                private val listener: ClickListener)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var category: SubCategory

        fun bind(item: SubCategory){
            this.category= item
            binding.categoryTitle.text= item.name
            Glide.with(binding.root).load(item.image?.src).into(binding.categoryImage)
        }

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onClick(category.id)
        }

    }

    private class CategoryComparator: DiffUtil.ItemCallback<SubCategory>(){

        override fun areItemsTheSame(oldItem: SubCategory, newItem: SubCategory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SubCategory, newItem: SubCategory): Boolean {
            return oldItem== newItem
        }

    }
}