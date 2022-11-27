package com.pastpaperskenya.app.presentation.main.downloads.downloads

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.download.Download
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.business.util.DownloadUtils
import com.pastpaperskenya.app.business.util.network.NetworkChangeReceiver
import com.pastpaperskenya.app.business.util.preferences.AppPreference
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.databinding.FragmentDownloadsBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import com.pastpaperskenya.app.presentation.main.downloads.viewpdf.ViewPfdActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DownloadsFragment : Fragment() {

    private var _binding: FragmentDownloadsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DownloadsViewModel by viewModels()
    private lateinit var downloadsAdapter: DownloadAdapter

    private lateinit var downloadDataList: ArrayList<Download>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDownloadsBinding.inflate(inflater, container, false)

        (activity as MainActivity).supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_TITLE

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipe_parent_view)

        if (NetworkChangeReceiver.isNetworkConnected()) {
            swipeRefreshLayout.setOnRefreshListener {
                setupObservers()
            }
            setupObservers()
        } else {
            try {
                downloadDataList =
                    AppPreference.getInstance(context).getDownloadList() as ArrayList<Download>;
                if (downloadDataList != null) {
                    if (downloadDataList.size > 0) {
                        binding.pbLoading.visibility = View.GONE
                        AppPreference.getInstance(getContext()).setDownloadList(downloadDataList);
                        setDownloadAdapter();
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Downloads is empty", Toast.LENGTH_LONG).show()
            }

        }

        registerObserver()

    }

    private fun registerObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
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
        viewModel.downloads.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.textEmpty.text= "Searching downloads"
                    binding.textEmpty.visibility= View.VISIBLE
                    binding.downloadShimmer.shimmerCategoryLayout.visibility= View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.emptyListLayout.visibility = View.GONE
                    downloadDataList = it.data as ArrayList<Download>
                    swipeRefreshLayout.isRefreshing = false

                    if (downloadDataList.size > 0) {
                        AppPreference.getInstance(context).downloadList = downloadDataList;
                        binding.pbLoading.visibility = View.GONE
                        setDownloadAdapter();

                    } else {
                        binding.pbLoading.visibility = View.VISIBLE
                        binding.emptyListLayout.visibility = View.GONE

                    }

                }
                Resource.Status.ERROR -> {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

                    try {
                        downloadDataList =
                            AppPreference.getInstance(context).downloadList as ArrayList<Download>;
                        downloadsAdapter = DownloadAdapter(requireContext(), downloadDataList)
                        setDownloadAdapter()
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "$e oppss...there might be a problem with your internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setDownloadAdapter() {

        downloadsAdapter = DownloadAdapter(requireContext(), downloadDataList)
        binding.recyclerview.visibility= View.VISIBLE
        binding.textEmpty.visibility= View.GONE
        binding.downloadShimmer.shimmerCategoryLayout.visibility= View.GONE

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.adapter = downloadsAdapter
        downloadsAdapter.setItemClickListener(ViewItemListener { path ->
            val dirPath: String = DownloadUtils.getRootDirPath(context?.applicationContext)
            val intent = Intent(context, ViewPfdActivity::class.java)
            intent.putExtra("filepath", "$dirPath/$path")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent)
        })
        downloadsAdapter.notifyDataSetChanged()
    }


}