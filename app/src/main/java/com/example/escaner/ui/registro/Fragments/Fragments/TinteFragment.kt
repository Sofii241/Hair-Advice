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
import com.example.escaner.databinding.CardviewTinteBinding
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class TinteFragment: Fragment() {

    private lateinit var binding: CardviewTinteBinding
    private val viewModel: RegistroViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewTinteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkSi.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkNo.isChecked = false
                viewModel.registrarPeloTenido(true)
            }
        }

        binding.checkNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkSi.isChecked = false
                viewModel.registrarPeloTenido(false)
            }
        }

        binding.next12.setOnClickListener {
            if (!viewModel.comprobacionCheckMarcadoTinte(binding.checkSi.isChecked, binding.checkNo.isChecked)) {
                viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                    binding.errorCheckVacio.visibility = View.VISIBLE
                    binding.errorCheckVacio.text = mensaje
                }
            } else {
                binding.errorCheckVacio.visibility = View.INVISIBLE

                requireActivity().supportFragmentManager.commit {
                    replace<ColorPeloFragment>(R.id.contenedor_fragments)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }
}