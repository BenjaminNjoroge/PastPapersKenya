package com.pastpaperskenya.app.presentation.main.downloads.downloads

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pastpaperskenya.app.business.model.Download
import com.pastpaperskenya.app.databinding.ItemDownloadBinding
import java.io.File


class DownloadsAdapter constructor
    (private val listener: ItemClickListener): RecyclerView.Adapter<DownloadsAdapter.DownloadsViewHolder>() {


    interface ItemClickListener{
        fun onItemClickGetPosition(path: String)
    }

    private val downloads= ArrayList<Download>()

    fun setItems(context: Context, downloads: ArrayList<Download>){
        this.downloads.clear()
        this.downloads.addAll(downloads)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadsViewHolder {
        val binding: ItemDownloadBinding = ItemDownloadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DownloadsViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: DownloadsViewHolder, position: Int) {
        holder.bind(downloads[position])

    }


    override fun getItemCount(): Int {
        return downloads.size
    }

    class DownloadsViewHolder(private val binding: ItemDownloadBinding,
                              private val listener: ItemClickListener
    ): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{

        private lateinit var download: Download
        private val downloads= ArrayList<Download>()

        init {
            binding.actionButton.setOnClickListener(this)
            binding.titleTextView.setOnClickListener(this)
            binding.relLayout1.setOnClickListener(this)
            binding.statusTextView.setOnClickListener(this)

            binding.statusTextView.visibility= View.GONE

        }

        fun bind(item: Download){
            this.download= item
            binding.titleTextView.text= item.download_name
        }

        override fun onClick(v: View?) {
            if (listener != null) {
                listener.onItemClickGetPosition(downloads.get(absoluteAdapterPosition).file.name)
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
}

