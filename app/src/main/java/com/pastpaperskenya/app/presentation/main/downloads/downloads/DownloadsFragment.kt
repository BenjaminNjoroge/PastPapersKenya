package com.pastpaperskenya.app.presentation.main.downloads.downloads

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.business.model.Download
import com.pastpaperskenya.app.business.util.DownloadUtils
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentDownloadsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadsAdapter= DownloadAdapter(requireContext())
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding.recyclerview.adapter= downloadsAdapter

        downloadsAdapter.setItemClickListener(object : ViewItemListener{
            override fun onItemClickGetName(path: String?) {
                val dirPath= DownloadUtils.getRootDirPath(context)
                val intent = Intent(context, ViewPfdActivity::class.java)
                val bundle = Bundle()
                Log.e("path", dirPath + "/" + attr.path)
                bundle.putString("filepath", dirPath + "/" + path)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                intent.putExtras(bundle)
                startActivity(intent)

            }

        })

        //setupObservers()
    }

//    private fun setupObservers() {
//        viewModel.downloads.observe(viewLifecycleOwner){
//            when(it){
//                is Resource.Loading->{
//                    binding.pbLoading.isVisible= it.loading
//                }
//                is Resource.Error->{
//                    binding.pbLoading.isVisible= false
//                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
//                }
//                is Resource.Success->{
//                    binding.pbLoading.isVisible= false
//                    downloadsAdapter.setItems(requireContext(), it.data as ArrayList<Download>)
//                }
//            }
//        }
//    }

}