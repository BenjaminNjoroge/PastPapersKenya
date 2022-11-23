package com.pastpaperskenya.app.presentation.main.home.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.databinding.ItemHomeCategoryLayoutBinding


class HomeAdapter(private val listener: ClickListener) :
    ListAdapter<HomeCategory, HomeAdapter.ViewHolder>(CategoryComparator()){

    interface ClickListener {
        fun onItemClick(characterId: Int, title: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= ItemHomeCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem= getItem(position)
        if (currentItem != null){
            holder.bind(currentItem)
        }
    }

    class ViewHolder(private val binding: ItemHomeCategoryLayoutBinding, private val listener: ClickListener):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        private lateinit var homeCategory: HomeCategory

        fun bind(item: HomeCategory){
            this.homeCategory= item
            binding.apply {
                categoryTitle.text = item.name
                if (item.image?.src != null) {
                    Glide.with(binding.root).load(item.image.src)
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                        .fallback(R.drawable.image_placeholder)
                        .into(categoryImage)
                }
            }
        }

        init {
            binding.categoryImage.setOnClickListener(this)
            binding.categoryTitle.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onItemClick(homeCategory.id, homeCategory.name!!)
        }

    }

    class CategoryComparator: DiffUtil.ItemCallback<HomeCategory>(){

        override fun areItemsTheSame(oldItem: HomeCategory, newItem: HomeCategory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HomeCategory, newItem: HomeCategory): Boolean {
            return oldItem== newItem
        }

    }


}

