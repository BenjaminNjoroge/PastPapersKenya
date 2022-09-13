package com.pastpaperskenya.app.presentation.main.home.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.Category

class ImageSliderAdapter(private val context: Context, private var arrayList: List<Category>):
    PagerAdapter() {

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View= (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.item_image_slider,null)
        val images= view.findViewById<ImageView>(R.id.slider_image)
        arrayList[position].let {
            Glide.with(context).load(it.image?.src).into(images)
        }

        val vp= container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp= container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }


}