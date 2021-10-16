package com.etwicaksono.submission2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.etwicaksono.submission2.databinding.FragmentDetailUserBinding

class DetailUserFragment : Fragment() {
    
    private var _binding:FragmentDetailUserBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailUserBinding.inflate(inflater,container,false)
        return binding.root
    }

}