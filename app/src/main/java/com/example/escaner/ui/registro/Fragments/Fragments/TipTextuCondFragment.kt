package com.example.escaner.ui.registro.Fragments.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.R
import com.example.escaner.databinding.CardviewTipoTexturaCondicionBinding
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class TipTextuCondFragment: Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: CardviewTipoTexturaCondicionBinding
    private val viewModel: RegistroViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewTipoTexturaCondicionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSpinerTipoPelo()
        loadSpinerTexturaPelo()
        loadSpinerCondicionPelo()

        binding.next13.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace<TratamientoPeloFragment>(R.id.contenedor_fragments)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }

    private fun loadSpinerTipoPelo() {
        val tipos = viewModel.listaTiposPelo()

        val adapter = ArrayAdapter.createFromResource(requireContext(), tipos, R.layout.simple_spinner_item_pelo)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinerTipoPelo.adapter = adapter
        binding.spinerTipoPelo.onItemSelectedListener = this
    }

    private fun loadSpinerTexturaPelo() {
        val texturas = viewModel.listaTexturasPelo()

        val adapter = ArrayAdapter.createFromResource(requireContext(), texturas, R.layout.simple_spinner_item_pelo)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinerTexturaPelo.adapter = adapter
        binding.spinerTexturaPelo.onItemSelectedListener = this
    }

    private fun loadSpinerCondicionPelo() {
        val condiciones = viewModel.listaCondicionesPelo()

        val adapter = ArrayAdapter.createFromResource(requireContext(), condiciones, R.layout.simple_spinner_item_pelo)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinerCondicionPelo.adapter = adapter
        binding.spinerCondicionPelo.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        binding.textErrorTip.visibility = View.INVISIBLE

        when (parent!!.id) {
            binding.spinerTipoPelo.id -> {
                viewModel.registrarPeloTipo(binding.spinerTipoPelo.selectedItem.toString())
                Log.d("TIPO", binding.spinerTipoPelo.selectedItem.toString())
            }
            binding.spinerTexturaPelo.id -> {
                viewModel.registrarPeloTextura(binding.spinerTexturaPelo.selectedItem.toString())
                Log.d("TEXTURA", binding.spinerTexturaPelo.selectedItem.toString())
            }
            binding.spinerCondicionPelo.id -> {
                viewModel.registrarPeloCondicion(binding.spinerCondicionPelo.selectedItem.toString())
                Log.d("CONDICION", binding.spinerCondicionPelo.selectedItem.toString())
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        binding.textErrorTip.visibility = View.VISIBLE
        binding.textErrorTip.text = viewModel.mensajeErrorSpinerTipoPelo()
    }
}