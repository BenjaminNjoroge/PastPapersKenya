package com.pastpaperskenya.app.presentation.main.downloads.downloads

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.recyclerview.widget.ListAdapter

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.downloader.*
import com.facebook.FacebookSdk.getApplicationContext
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.Download
import com.pastpaperskenya.app.business.util.DownloadUtils
import com.pastpaperskenya.app.databinding.ItemDownloadBinding
import java.io.File

class DownloadAdapter(private val listener: ClickListener) :
    ListAdapter<Download, DownloadAdapter.DownloadViewHolder>(DownloadsComparator()) {

    interface ClickListener{
        fun itemClick(position: Int, path:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val binding= ItemDownloadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DownloadViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {

        val item= getItem(position)
        if (item!=null){
            holder.bind(item)
        }
    }



    class DownloadViewHolder(private val binding: ItemDownloadBinding, private val listener: ClickListener)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var downloads: Download


        init {
            binding.titleTextView.setOnClickListener(this)
            binding.wrapperImage.setOnClickListener(this)
            binding.actionButton.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            listener.itemClick(absoluteAdapterPosition, downloads.file.name)
        }

        fun bind(item: Download){
            this.downloads= item
            binding.apply {
                titleTextView.setText(downloads.product_name)
            }


        }
    }

    private fun pdfToBitmap(pdfFile: File): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val renderer =
                PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
            val pageCount = renderer.pageCount
            if (pageCount > 0) {
                val page = renderer.openPage(0)
                bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                renderer.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return bitmap
    }

    class DownloadsComparator: DiffUtil.ItemCallback<Download>(){
        override fun areItemsTheSame(oldItem: Download, newItem: Download): Boolean {
            return oldItem.download_id == newItem.download_id
        }

        override fun areContentsTheSame(oldItem: Download, newItem: Download): Boolean {
            return oldItem== newItem
        }

    }
}