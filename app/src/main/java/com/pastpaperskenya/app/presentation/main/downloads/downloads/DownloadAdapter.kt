package com.pastpaperskenya.app.presentation.main.downloads.downloads

import com.pastpaperskenya.app.business.model.download.Download
import androidx.recyclerview.widget.RecyclerView
import com.pastpaperskenya.app.presentation.main.downloads.downloads.DownloadAdapter.DownloadViewHolder
import com.pastpaperskenya.app.presentation.main.downloads.downloads.ViewItemListener
import android.view.ViewGroup
import android.view.LayoutInflater
import com.pastpaperskenya.app.R
import android.annotation.SuppressLint
import android.content.Context
import com.pastpaperskenya.app.business.util.network.NetworkChangeReceiver
import com.pastpaperskenya.app.business.util.DownloadUtils
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.view.View
import android.widget.*
import com.downloader.*
import java.io.File
import java.lang.Exception

open class DownloadAdapter(private val context: Context, private val arrayList: List<Download>) : RecyclerView.Adapter<DownloadViewHolder>() {

    var dirPath: String? = null
    private var itemClickListener: ViewItemListener? = null
    var downloadIdOne = 0

    fun setItemClickListener(itemClickListener: ViewItemListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val vh: DownloadViewHolder
        val v = LayoutInflater.from(context).inflate(R.layout.item_download, parent, false)
        vh = DownloadViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(
        holder: DownloadViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.actionButton.setOnClickListener(null)
        holder.actionButton.isEnabled = true
        holder.wrapperImageView.setOnClickListener(null)
        holder.wrapperImageView.isEnabled = true
        holder.statusTextView.visibility = View.GONE
        holder.progressBar.visibility = View.GONE
        holder.titleTextView.text = arrayList[position].product_name

        val isfile = File(context.filesDir, arrayList[position].file.name)
        if (!isfile.exists()) {
            holder.progressBar.visibility = View.VISIBLE
            holder.actionButton.setOnClickListener(View.OnClickListener {
                if (NetworkChangeReceiver.isNetworkConnected()) {
                    if (Status.RUNNING == PRDownloader.getStatus(downloadIdOne)) {
                        PRDownloader.pause(downloadIdOne)
                        return@OnClickListener
                    }
                    holder.statusTextView.visibility = View.GONE
                    holder.actionButton.isEnabled = true
                    holder.progressBar.isIndeterminate = true
                    holder.progressBar.indeterminateDrawable.setColorFilter(
                        Color.GRAY, PorterDuff.Mode.SRC_IN
                    )
                    if (Status.PAUSED == PRDownloader.getStatus(downloadIdOne)) {
                        PRDownloader.resume(downloadIdOne)
                        return@OnClickListener
                    }
                    dirPath = DownloadUtils.getRootDirPath(context.applicationContext)
                    val url = arrayList[position].download_url
                    downloadIdOne =
                        PRDownloader.download(url, dirPath, arrayList[position].file.name)
                            .setTag(arrayList[position].file.name)
                            .build()
                            .setOnStartOrResumeListener {
                                holder.progressBar.isIndeterminate = false
                                holder.actionButton.isEnabled = true
                                holder.actionButton.setText(R.string.pause)
                                holder.statusTextView.visibility = View.GONE
                                /*buttonCancelOne.setEnabled(true);*/
                            }
                            .setOnPauseListener {
                                holder.actionButton.setText(R.string.resume)
                                holder.statusTextView.visibility = View.GONE
                            }
                            .setOnCancelListener {
                                holder.actionButton.setText(R.string.start)
                                /*buttonCancelOne.setEnabled(false);*/holder.progressBar.progress =
                                0
                                holder.progressTextView.text = ""
                                downloadIdOne = 0
                                holder.progressBar.isIndeterminate = false
                                holder.statusTextView.visibility = View.GONE
                            }
                            .setOnProgressListener { progress ->
                                val progressPercent =
                                    progress.currentBytes * 100 / progress.totalBytes
                                holder.progressBar.progress = progressPercent.toInt()
                                holder.progressTextView.text = DownloadUtils.getProgressDisplayLine(
                                    progress.currentBytes,
                                    progress.totalBytes
                                )
                                holder.progressBar.isIndeterminate = false
                                holder.statusTextView.visibility = View.GONE
                            }
                            .start(object : OnDownloadListener {
                                override fun onDownloadComplete() {
                                    holder.actionButton.isEnabled = false
                                    //buttonCancelOne.setEnabled(false);
                                    holder.progressTextView.text = ""
                                    holder.progressBar.progress = 0
                                    holder.progressBar.visibility = View.GONE
                                    holder.statusTextView.text = ""
                                    holder.statusTextView.visibility = View.GONE
                                    holder.actionButton.setText(R.string.download)
                                    if (isfile.exists()) {
                                        holder.statusTextView.setText(R.string.view)
                                        holder.statusTextView.visibility = View.VISIBLE
                                        holder.wrapperImageView.setImageBitmap(pdfToBitmap(isfile))
                                    } else {
                                        holder.statusTextView.text = ""
                                        holder.statusTextView.visibility = View.GONE
                                        holder.progressBar.visibility = View.GONE
                                    }
                                }

                                override fun onError(error: Error) {
                                    holder.actionButton.setText(R.string.start)
                                    Toast.makeText(
                                        context,
                                        "Error" + " " + error.serverErrorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    holder.progressTextView.text = ""
                                    holder.progressBar.progress = 0
                                    downloadIdOne = 0
                                    //buttonCancelOne.setEnabled(false);
                                    holder.progressBar.isIndeterminate = false
                                    holder.actionButton.isEnabled = true
                                    holder.statusTextView.visibility = View.GONE
                                }
                            })
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.failed_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else if (isfile.exists()) {
            holder.statusTextView.setText(R.string.view)
            holder.progressTextView.text = ""
            holder.progressBar.visibility = View.GONE
            holder.statusTextView.visibility = View.VISIBLE
            holder.wrapperImageView.setImageBitmap(pdfToBitmap(isfile))
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class DownloadViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val titleTextView: TextView
        val statusTextView: TextView
        val progressBar: ProgressBar
        val progressTextView: TextView
        val actionButton: Button
        val timeRemainingTextView: TextView
        val downloadedBytesPerSecondTextView: TextView
        val wrapperImageView: ImageView

        init {
            titleTextView = itemView.findViewById(R.id.titleTextView)
            statusTextView = itemView.findViewById(R.id.status_textview)
            progressBar = itemView.findViewById(R.id.progressBar)
            actionButton = itemView.findViewById(R.id.actionButton)
            progressTextView = itemView.findViewById(R.id.progress_TextView)
            timeRemainingTextView = itemView.findViewById(R.id.remaining_TextView)
            downloadedBytesPerSecondTextView = itemView.findViewById(R.id.downloadSpeedTextView)
            wrapperImageView = itemView.findViewById(R.id.wrapperImage)
            statusTextView.setOnClickListener(this)
            wrapperImageView.setOnClickListener(this)
            titleTextView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (itemClickListener != null) itemClickListener!!.onItemClickGetName(arrayList[absoluteAdapterPosition].file.name)
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