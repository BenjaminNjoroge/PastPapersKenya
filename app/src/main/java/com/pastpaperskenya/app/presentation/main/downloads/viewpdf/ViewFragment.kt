package com.pastpaperskenya.app.presentation.main.downloads.viewpdf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.databinding.FragmentViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewFragment : Fragment() {

    private lateinit var _binding: FragmentViewBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_view, container, false)
    }

}