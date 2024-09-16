package com.example.escaner.ui.registro.Fragments.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.R
import com.example.escaner.databinding.CardviewBienvenidaRegistroBinding

class BienvenidaFragment : Fragment() {

    companion object {
        fun newInstance() = BienvenidaFragment()
    }

    private lateinit var binding: CardviewBienvenidaRegistroBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewBienvenidaRegistroBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace<NombreFragment>(R.id.contenedor_fragments)
                setReorderingAllowed(true)
                addToBackStack(null)
            }

        }
    }
}