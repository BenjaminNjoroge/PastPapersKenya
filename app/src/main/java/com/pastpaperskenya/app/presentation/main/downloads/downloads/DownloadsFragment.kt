package com.pastpaperskenya.app.presentation.main.downloads.downloads

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.business.model.download.Download
import com.pastpaperskenya.app.business.util.DownloadUtils
import com.pastpaperskenya.app.business.util.network.NetworkChangeReceiver
import com.pastpaperskenya.app.business.util.preferences.AppPreference
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.databinding.FragmentDownloadsBinding
import com.pastpaperskenya.app.presentation.main.downloads.viewpdf.ViewPfdActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DownloadsFragment : Fragment(){

    private  var _binding: FragmentDownloadsBinding?=null
    private val binding get() = _binding!!

    private val viewModel: DownloadsViewModel by activityViewModels()
    private lateinit var downloadsAdapter: DownloadAdapter

    private lateinit var downloadDataList: ArrayList<Download>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentDownloadsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (NetworkChangeReceiver.isNetworkConnected()){
            setupObservers()
        } else{
            downloadDataList= AppPreference.getInstance(context).getDownloadList() as ArrayList<Download>;
            if(downloadDataList!=null) {
                if (downloadDataList.size > 0) {
                    binding.pbLoading.visibility= View.GONE
                    AppPreference.getInstance(getContext()).setDownloadList(downloadDataList);
                    setDownloadAdapter();
                }
            }
        }

        binding.swipe.setOnRefreshListener {
            @Override fun onRefresh(){
                setupObservers()
                binding.swipe.isRefreshing= false
            }
        }
    }


    private fun setupObservers(){
        viewModel.downloads.observe(viewLifecycleOwner){
            when(it.status){
                Resource.Status.LOADING->{
                    binding.pbLoading.visibility= View.VISIBLE
                }
                Resource.Status.SUCCESS->{
                    binding.pbLoading.visibility= View.GONE
                    downloadDataList= it.data as ArrayList<Download>

                    if(downloadDataList!=null) {
                        if (downloadDataList.size > 0) {
                            AppPreference.getInstance(context).downloadList = downloadDataList;
                            binding.pbLoading.visibility= View.GONE
                            setDownloadAdapter();
                        }
                    }

                }
                Resource.Status.ERROR->{
                    binding.pbLoading.visibility= View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setDownloadAdapter() {

        downloadsAdapter = DownloadAdapter(requireContext(), downloadDataList)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding.recyclerview.adapter= downloadsAdapter
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