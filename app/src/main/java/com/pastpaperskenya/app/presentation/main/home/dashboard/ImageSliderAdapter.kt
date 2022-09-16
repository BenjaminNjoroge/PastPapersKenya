package com.pastpaperskenya.app.presentation.main.home.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.category.SliderCategory
import com.smarteist.autoimageslider.SliderViewAdapter

class ImageSliderAdapter(private val context: Context, private var arrayList: ArrayList<SliderCategory>):
    SliderViewAdapter<ImageSliderAdapter.SliderViewHolder>() {

    var sliderList: ArrayList<SliderCategory> = arrayList

    class SliderViewHolder(itemView: View?) : SliderViewAdapter.ViewHolder(itemView) {
        var imageView: ImageView = itemView!!.findViewById(R.id.slider_image)
    }

    override fun getCount(): Int {
        return sliderList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        // inside this method we are inflating our layout file for our slider view.
        val inflate: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_image_slider, null)
        return SliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        // on below line we are checking if the view holder is null or not.
        if (viewHolder != null) {
            Glide.with(viewHolder.itemView).load(sliderList.get(position).image?.src).fitCenter()
                .into(viewHolder.imageView)
        }
    }
}