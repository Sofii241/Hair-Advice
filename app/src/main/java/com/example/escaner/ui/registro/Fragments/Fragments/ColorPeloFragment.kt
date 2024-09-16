package com.example.escaner.ui.registro.Fragments.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.R
import com.example.escaner.databinding.CardviewColorPeloBinding
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ColorPeloFragment: Fragment() {

    private lateinit var binding: CardviewColorPeloBinding
    private val viewModel: RegistroViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewColorPeloBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next3.setOnClickListener {
            if (!viewModel.comprobacionCampoVacioColor(binding.editTxtColorPelo.text.toString())) {
                viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                    binding.error5.visibility = View.VISIBLE
                    binding.error5.text = mensaje
                }
            } else {
                viewModel.registrarColorPelo(binding.editTxtColorPelo.text.toString())

                requireActivity().supportFragmentManager.commit {
                    replace<LargoPeloFragment>(R.id.contenedor_fragments)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }
}