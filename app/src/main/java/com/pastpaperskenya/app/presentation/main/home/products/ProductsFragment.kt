package com.pastpaperskenya.app.presentation.main.home.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.business.util.sealed.Resource
import com.pastpaperskenya.app.business.util.toast
import com.pastpaperskenya.app.databinding.FragmentProductsBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment(), ProductsAdapter.ClickListener {
    private var _binding: FragmentProductsBinding?= null
    private val binding get()= _binding!!

    private val viewModel: ProductsViewModel by viewModels()
    private val args: ProductsFragmentArgs by navArgs()
    private lateinit var adapter: ProductsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        val title= args.title

        // Calling the support action bar and setting it to custom

        // Displaying the custom layout in the ActionBar
        (activity as MainActivity).supportActionBar!!.setDisplayShowCustomEnabled(true)

        (activity as MainActivity).supportActionBar?.title= title


        (activity as MainActivity).supportActionBar?.setCustomView(R.layout.primary_toolbar_layout)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id= args.id
        id.let {
            viewModel.start(it)
        }

        adapter= ProductsAdapter(this)
        val gridLayoutManager= GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        binding.primaryRecycler.layoutManager= gridLayoutManager
        binding.primaryRecycler.adapter= adapter

        registerObservers()

        listenToChannels()
    }

    private fun registerObservers(){
        viewModel.products.observe(viewLifecycleOwner){
            when(it.status){
                Resource.Status.LOADING->{
                    binding.progressBar.visibility= View.VISIBLE
                }
                Resource.Status.SUCCESS->{
                    binding.progressBar.visibility= View.GONE
                    if (!it.data.isNullOrEmpty()) adapter.submitList(it.data) else binding.progressBar.visibility= View.VISIBLE
                }
                Resource.Status.ERROR->{
                    binding.progressBar.visibility= View.GONE
                    toast(it.message.toString())
                }
            }
        }
    }

    override fun onClick(id: Int, title: String?) {
        val bundle= bundleOf("id" to id, "title" to title)
        findNavController().navigate(R.id.action_productsFragment_to_productDetailFragment, bundle)
    }

    override fun addToCart(cart: Cart) {
        viewModel.addToCart(cart)
    }

    override fun removeFromCart(productId: Int) {
        viewModel.removeFromCart(productId)
    }

    private fun listenToChannels(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authEventsFlow.collect{ events->
                when(events){
                    is AuthEvents.Message -> {
                    }
                    is AuthEvents.Error -> {
                    }
                    is AuthEvents.ErrorCode -> {
                        if (events.code == 100)
                            binding.apply {
                                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                            }
                        if (events.code == 101)
                            binding.apply {
                                Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }
}