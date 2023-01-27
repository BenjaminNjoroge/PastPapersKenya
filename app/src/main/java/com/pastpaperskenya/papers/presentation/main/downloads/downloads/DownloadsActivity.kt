package com.pastpaperskenya.papers.presentation.main.downloads.downloads

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.papers.business.model.download.Download
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.util.DownloadUtils
import com.pastpaperskenya.papers.business.util.network.NetworkChangeReceiver
import com.pastpaperskenya.papers.business.util.preferences.AppPreference
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import com.pastpaperskenya.papers.databinding.ActivityDownloadBinding
import com.pastpaperskenya.papers.presentation.main.MainActivity
import com.pastpaperskenya.papers.presentation.main.downloads.viewpdf.ViewPfdActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DownloadsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDownloadBinding
    private val viewModel: DownloadsViewModel by viewModels()

    private lateinit var downloadsAdapter: DownloadAdapter

    private lateinit var downloadDataList: ArrayList<Download>

    private var userServerId: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDownloadBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel.userProfile.observe(this){ details->

            userServerId= details.userServerId.toString()
        }

        if (NetworkChangeReceiver.isNetworkConnected()) {

            setupObservers()

        } else {
            try {
                downloadDataList =
                    AppPreference.getInstance(this).getDownloadList() as ArrayList<Download>;
                if (downloadDataList != null) {
                    if (downloadDataList.size > 0) {
                        binding.pbLoading.visibility = View.GONE
                        AppPreference.getInstance(this).setDownloadList(downloadDataList);
                        setDownloadAdapter();
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Downloads is empty", Toast.LENGTH_LONG).show()
            }

        }

        registerObserver()

    }

    private fun registerObserver() {
        lifecycleScope.launch {
            viewModel.events.collect { events ->
                when (events) {
                    is AuthEvents.Message -> {
                        //nothing
                    }
                    is AuthEvents.ErrorCode -> {
                        //nothing
                    }
                    is AuthEvents.Error -> {
                        (events.message)
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.downloads.observe(this) {
            when (it.status) {
                NetworkResult.Status.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.textEmpty.text= "Searching downloads"
                    binding.textEmpty.visibility= View.VISIBLE
                    binding.downloadShimmer.shimmerCategoryLayout.visibility= View.VISIBLE
                }
                NetworkResult.Status.SUCCESS -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.emptyListLayout.visibility = View.GONE
                    downloadDataList = it.data as ArrayList<Download>

                    if (downloadDataList.size > 0) {
                        AppPreference.getInstance(this).downloadList = downloadDataList;
                        binding.pbLoading.visibility = View.GONE
                        setDownloadAdapter();

                    } else {
                        binding.pbLoading.visibility = View.VISIBLE
                        binding.emptyListLayout.visibility = View.GONE

                    }

                }
                NetworkResult.Status.ERROR -> {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                    try {
                        downloadDataList =
                            AppPreference.getInstance(this).downloadList as ArrayList<Download>;
                        downloadsAdapter = DownloadAdapter(this, downloadDataList)
                        setDownloadAdapter()
                    } catch (e: Exception) {
                        Toast.makeText(this, "$e oppss...there might be a problem with your internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setDownloadAdapter() {

        downloadsAdapter = DownloadAdapter(this, downloadDataList)
        binding.recyclerview.visibility= View.VISIBLE
        binding.textEmpty.visibility= View.GONE
        binding.downloadShimmer.shimmerCategoryLayout.visibility= View.GONE


        binding.recyclerview.adapter = downloadsAdapter
        downloadsAdapter.setItemClickListener(ViewItemListener { path ->
            val dirPath: String = DownloadUtils.getRootDirPath(this.applicationContext)
            val intent = Intent(this, ViewPfdActivity::class.java)
            intent.putExtra("filepath", "$dirPath/$path")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent)
        })
        downloadsAdapter.notifyDataSetChanged()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}