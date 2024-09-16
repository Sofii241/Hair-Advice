package com.example.escaner.ui.registro.Fragments.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.R
import com.example.escaner.databinding.CardviewNombreBinding
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NombreFragment : Fragment() {

    private lateinit var binding: CardviewNombreBinding
    private val viewModel: RegistroViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewNombreBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next9.setOnClickListener {
            if (!viewModel.validarNombre(binding.nombre.text.toString())) {

                viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                    binding.error.visibility = View.VISIBLE
                    binding.error.text = mensaje
                }
            } else {
                binding.error.visibility = View.INVISIBLE
                viewModel.registrarNombre(binding.nombre.text.toString())
                Log.d("NOMBRE ", viewModel.usuario().nombre)

                requireActivity().supportFragmentManager.commit {
                    replace<ApellidoFragment>(R.id.contenedor_fragments)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }
}