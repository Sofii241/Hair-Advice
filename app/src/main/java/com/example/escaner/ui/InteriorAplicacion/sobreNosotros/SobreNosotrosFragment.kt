package com.example.escaner.ui.InteriorAplicacion.sobreNosotros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.escaner.R
import com.example.escaner.databinding.FragmentSobreNosotrosBinding

class SobreNosotrosFragment : Fragment(R.layout.fragment_sobre_nosotros) {

    private lateinit var _binding: FragmentSobreNosotrosBinding

    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        ViewModelProvider(this)[SobreNosotrosModel::class.java]

        _binding = FragmentSobreNosotrosBinding.inflate(inflater, container, false)
        return binding.root
    }
}