package com.pastpaperskenya.app.presentation.main.home.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.databinding.ItemHomeCategoryLayoutBinding
import com.pastpaperskenya.app.presentation.main.ClickListener
import javax.inject.Inject

class HomeAdapter(private val listener :CategoryClickListener)
    : RecyclerView.Adapter<CharacterViewHolder>() {

    interface CategoryClickListener{
        fun onClick(categoryId: Int)
    }

    private val items= ArrayList<Category>()

    fun setItems(items: ArrayList<Category>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding: ItemHomeCategoryLayoutBinding = ItemHomeCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int {
        return items.size
    }

}

