package com.pastpaperskenya.app.presentation.main.home.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.category.SliderCategory
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.databinding.FragmentHomeBinding
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeAdapter= HomeAdapter(this)
        val gridLayoutManager= GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.layoutManager= gridLayoutManager
        binding.recyclerview.adapter= homeAdapter

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.homeResponse.observe(viewLifecycleOwner){
            when(it.status){
                 Resource.Status.SUCCESS->{
                    binding.pbLoading.visibility= View.GONE
                    if (!it.data.isNullOrEmpty()) homeAdapter.submitList(it.data)
                }
                 Resource.Status.ERROR->{
                    Toast.makeText(requireContext(), it.message,Toast.LENGTH_SHORT).show()
                }
                 Resource.Status.LOADING->{
                    binding.pbLoading.visibility= View.VISIBLE
                }
            }
        }

        viewModel.sliderResponse.observe(viewLifecycleOwner){
            when(it.status){
                Resource.Status.LOADING->{
                    binding.sliderShimmerView.visibility= View.VISIBLE
                }
                Resource.Status.SUCCESS->{

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
                Resource.Status.ERROR->{
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