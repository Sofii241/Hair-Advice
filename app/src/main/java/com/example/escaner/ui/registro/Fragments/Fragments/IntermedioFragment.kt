package com.example.escaner.ui.registro.Fragments.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.R
import com.example.escaner.databinding.CardviewIntermedioBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class IntermedioFragment : Fragment() {

    private lateinit var binding: CardviewIntermedioBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewIntermedioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next10.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace<TinteFragment>(R.id.contenedor_fragments)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }
}