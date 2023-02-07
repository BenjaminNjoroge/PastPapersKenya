package com.pastpaperskenya.papers.presentation.main.home.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.papers.R
import com.pastpaperskenya.papers.business.model.category.SliderCategory
import com.pastpaperskenya.papers.business.util.StoreTimeHelper
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import com.pastpaperskenya.papers.databinding.FragmentHomeBinding
import com.pastpaperskenya.papers.presentation.main.MainActivity
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), HomeAdapter.ClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!


    private lateinit var homeAdapter: HomeAdapter
    private lateinit var sliderAdapter: ImageSliderAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentHomeBinding.inflate(inflater, container, false)

        (activity as MainActivity).supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_TITLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!StoreTimeHelper.isStoreOpen()) {
            StoreTimeHelper.showCloseDialogue(requireContext())
        } else {
            homeAdapter = HomeAdapter(this)
            val gridLayoutManager =
                GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
            binding.recyclerview.layoutManager = gridLayoutManager
            binding.recyclerview.adapter = homeAdapter

            setupObservers()
        }
    }

    private fun setupObservers() {
        viewModel.homeResponse.observe(viewLifecycleOwner){
            when(it.status){
                 NetworkResult.Status.SUCCESS->{
                    binding.pbLoading.visibility= View.GONE
                    if (!it.data.isNullOrEmpty()) homeAdapter.submitList(it.data) else binding.pbLoading.visibility= View.VISIBLE
                }
                 NetworkResult.Status.ERROR->{
                    Toast.makeText(requireContext(), it.message,Toast.LENGTH_SHORT).show()
                }
                 NetworkResult.Status.LOADING->{
                    binding.pbLoading.visibility= View.VISIBLE
                }
            }
        }

        viewModel.sliderResponse.observe(viewLifecycleOwner){
            when(it.status){
                NetworkResult.Status.LOADING->{
                    binding.sliderShimmerView.visibility= View.VISIBLE
                }
                NetworkResult.Status.SUCCESS->{

                    binding.sliderShimmerView.visibility= View.GONE
                    binding.homeCard.visibility= View.VISIBLE
                    binding.imageSlider.visibility= View.VISIBLE

                    sliderAdapter= ImageSliderAdapter(requireContext(),
                        it.data as ArrayList<SliderCategory>)

                    binding.imageSlider.autoCycleDirection= SliderView.LAYOUT_DIRECTION_LTR
                    binding.imageSlider.setSliderAdapter(sliderAdapter)
                    binding.imageSlider.scrollTimeInSec = 3
                    binding.imageSlider.isAutoCycle = true
                    binding.imageSlider.startAutoCycle()

                }
                NetworkResult.Status.ERROR->{
                    Toast.makeText(requireContext(), it.message,Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onItemClick(characterId: Int, title:String) {
        val bundle= bundleOf("id" to characterId, "title" to title)
        findNavController().navigate(R.id.action_homeFragment_to_subCategoryFragment,bundle)
    }


}