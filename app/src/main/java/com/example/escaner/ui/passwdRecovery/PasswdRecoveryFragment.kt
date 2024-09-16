package com.example.escaner.ui.passwdRecovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.escaner.R
import com.example.escaner.Util.Resource
import com.example.escaner.databinding.FragmentPasswdRecoveryBinding
import com.example.escaner.ui.inicioAcceso.InicioAccesoFragment

class PasswdRecoveryFragment : Fragment() {


    private val viewModel: PasswdRecoveryViewModel by activityViewModels()
    private lateinit var binding: FragmentPasswdRecoveryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPasswdRecoveryBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observadorVerificacion()
    }

    private fun initListeners() {
            binding.btnRecoverPasswd.setOnClickListener {
                val email = binding.emailRecoveryPasswd.text.toString()
                if (viewModel.validarCorreo(email)) {
                    binding.error6.visibility = View.GONE
                    viewModel.enviarCorreoRecuperacionClave(email,
                        onSuccess = {
                            mostrarCardViewEnlaceEnviado()
                        },
                        onFailure = { e ->
                            ocultarCardViewEnlaceEnviado()
                            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        })
                } else {
                    binding.error6.visibility = View.VISIBLE
                }
            }
            viewModel.mensajeError.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    binding.error6.visibility = View.VISIBLE
                    binding.error6.text = it
                }
                binding.error6.visibility =  View.GONE

            })
    }

    private fun ocultarCardViewEnlaceEnviado() {
        binding.cardRecovery.visibility = View.VISIBLE
        binding.cardLinkSent.visibility = View.GONE
    }

    private fun mostrarCardViewEnlaceEnviado() {
        binding.cardRecovery.visibility = View.GONE
        binding.cardLinkSent.visibility = View.VISIBLE
    }

    private fun observadorVerificacion() {
        viewModel.passwordChanged.observe(viewLifecycleOwner, Observer { isChanged ->
            if (isChanged) {
                handleLoading(false)
                Toast.makeText(requireContext(), "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show()
                redirigirAPantallaInicio()
            }
            else{
                handleLoading(true)
            }
        })
    }

    private fun redirigirAPantallaInicio() {
        requireActivity().supportFragmentManager.commit {
            replace<InicioAccesoFragment>(R.id.contenedor_fragments)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarLinkSent.visibility = View.VISIBLE
        } else {
            binding.progressBarLinkSent.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
        viewModel.stopCheckRecuperacionClave()
    }
}