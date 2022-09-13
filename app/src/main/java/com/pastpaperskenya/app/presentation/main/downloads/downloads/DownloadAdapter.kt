package com.pastpaperskenya.app.presentation.main.downloads.downloads

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.downloader.*
import com.facebook.FacebookSdk.getApplicationContext
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.Download
import com.pastpaperskenya.app.business.util.DownloadUtils
import java.io.File

  class DownloadAdapter(private var context: Context) :
     RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder>() {
      private var arrayList=  ArrayList<Download>()
      private lateinit var dirPath: String
    private lateinit var itemClickListener: ViewItemListener
    private var downloadIdOne = 0

     fun setItems(context: Context, downloadList: ArrayList<Download>) {
        this.context = context
        arrayList = downloadList
         notifyDataSetChanged()
    }

    fun setItemClickListener(itemClickListener: ViewItemListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_download, parent, false)
        return DownloadViewHolder(v)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        holder.actionButton.setOnClickListener(null)
        holder.actionButton.isEnabled = true
        holder.statusTextView.visibility = View.GONE
        holder.progressBar.visibility = View.GONE
        holder.titleTextView.setText(arrayList[position].product_name)

        val isfile = File(context.cacheDir, arrayList[position].file.name)
        if (!isfile.exists()) {
            holder.progressBar.visibility = View.VISIBLE
            holder.actionButton.setOnClickListener(View.OnClickListener {

                //if (isConnected()) {
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
                    dirPath = DownloadUtils.getRootDirPath(getApplicationContext())
                    val url = arrayList[position].download_url
                    downloadIdOne = PRDownloader.download(
                        url,
                        dirPath,
                        arrayList[position].file.name
                    )
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
                                holder.actionButton.visibility= View.GONE
                                holder.statusTextView.visibility = View.VISIBLE
                                holder.progressBar.visibility = View.GONE
                                holder.descriptionBar.visibility= View.VISIBLE
                                if (isfile.exists()) {
                                    holder.descriptionBar.visibility= View.VISIBLE
                                    holder.wrapperImage.setImageBitmap(pdfToBitmap(isfile))
                                } else {
                                    holder.statusTextView.text = "Error"
                                    holder.statusTextView.visibility = View.VISIBLE
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
 //               }
//                else {
//                    Toast.makeText(
//                        context,
//                        context.getString(R.string.failed_msg),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
            })
        } else if (isfile.exists()) {
            holder.descriptionBar.visibility= View.VISIBLE
            holder.progressTextView.text = ""
            holder.progressBar.visibility = View.GONE
            holder.actionButton.visibility= View.GONE
            holder.statusTextView.visibility = View.VISIBLE
            holder.wrapperImage.setImageBitmap(pdfToBitmap(isfile))
        }
    }

    override fun getItemCount(): Int{
        return arrayList.size
    }


     inner class DownloadViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.status_textView)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val progressTextView: TextView = itemView.findViewById(R.id.progress_TextView)
        val actionButton: Button = itemView.findViewById(R.id.actionButton)
        val timeRemainingTextView: TextView = itemView.findViewById(R.id.remaining_TextView)
        val downloadedBytesPerSecondTextView: TextView = itemView.findViewById(R.id.downloadSpeedTextView)
         val descriptionBar: LinearLayout= itemView.findViewById(R.id.descriptionBar)
         val cardview: CardView= itemView.findViewById(R.id.cardview)
         val wrapperImage: ImageView= itemView.findViewById(R.id.wrapperImage)


        override fun onClick(view: View) {
            itemClickListener.onItemClickGetName(
                arrayList[absoluteAdapterPosition].file.name
            )
        }

        init {
            titleTextView.setOnClickListener(this)
            cardview.setOnClickListener(this)
            descriptionBar.setOnClickListener(this)
            wrapperImage.setOnClickListener(this)

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