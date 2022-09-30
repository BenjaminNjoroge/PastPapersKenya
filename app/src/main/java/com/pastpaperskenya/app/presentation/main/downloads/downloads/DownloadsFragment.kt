package com.pastpaperskenya.app.presentation.main.downloads.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.databinding.FragmentDownloadsBinding
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


        binding.swipe.setOnRefreshListener {
            @Override fun onRefresh(){
                setupObservers()
                binding.swipe.isRefreshing= false
            }
        }
        setupObservers()
    }


    private fun setupObservers(){
        viewModel.downloads.observe(viewLifecycleOwner){
            when(it.status){
                Resource.Status.LOADING->{
                    binding.pbLoading.visibility= View.VISIBLE
                }
                Resource.Status.SUCCESS->{
                    binding.pbLoading.visibility= View.GONE
                    downloadsAdapter= DownloadAdapter(requireContext(), it.data!!)
                    binding.recyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                    binding.recyclerview.adapter= downloadsAdapter

                }
                Resource.Status.ERROR->{
                    binding.pbLoading.visibility= View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}