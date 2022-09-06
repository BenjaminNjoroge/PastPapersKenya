package com.pastpaperskenya.app.presentation.main.downloads.downloads

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.business.model.Download
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import com.pastpaperskenya.app.databinding.FragmentDownloadBinding
import com.pastpaperskenya.app.presentation.main.home.dashboard.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DownloadsFragment : Fragment(), DownloadsAdapter.ItemClickListener {

    private  var _binding: FragmentDownloadBinding?=null
    private val binding get() = _binding!!

    private val viewModel: DownloadsViewModel by activityViewModels()
    private lateinit var downloadsAdapter: DownloadsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadsAdapter= DownloadsAdapter(this)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding.recyclerview.adapter= downloadsAdapter

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.downloads.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Loading->{
                    binding.pbLoading.isVisible= it.loading
                }
                is NetworkResult.Error->{
                    binding.pbLoading.isVisible= false
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success->{
                    binding.pbLoading.isVisible= false
                    downloadsAdapter.setItems(requireContext(), it.data as ArrayList<Download>)
                }
            }
        }
    }

    override fun onItemClickGetPosition(path: String) {
        TODO("Not yet implemented")
    }


}