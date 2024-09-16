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
import com.example.escaner.databinding.CardviewApellidoBinding
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApellidoFragment : Fragment() {

    companion object {
        fun newInstance() = ApellidoFragment()
    }

    private lateinit var binding: CardviewApellidoBinding
    private val viewModel: RegistroViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewApellidoBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next4.setOnClickListener {
            if (!viewModel.validarApellido(binding.apellido.text.toString())){
                viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                    mensaje.let {
                        binding.error2.visibility = View.VISIBLE
                        binding.error2.text = mensaje
                    }
                }
            } else {
                binding.error2.visibility = View.INVISIBLE
                viewModel.registrarApellido(binding.apellido.text.toString())
                Log.d("NOMBRE ", viewModel.usuario().nombre)
                Log.d("NOMBRE ", viewModel.usuario().apellido)

                requireActivity().supportFragmentManager.commit {
                    replace<FechaNacimientoFragment>(R.id.contenedor_fragments)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }
}


