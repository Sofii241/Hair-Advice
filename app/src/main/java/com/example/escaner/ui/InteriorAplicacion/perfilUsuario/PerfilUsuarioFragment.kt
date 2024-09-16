package com.example.escaner.ui.InteriorAplicacion.perfilUsuario

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.escaner.Model.Usuarios.Enums.Condicion
import com.example.escaner.Model.Usuarios.Enums.Largo
import com.example.escaner.Model.Usuarios.Enums.Tenido
import com.example.escaner.Model.Usuarios.Enums.Textura
import com.example.escaner.Model.Usuarios.Enums.Tipo
import com.example.escaner.Model.Usuarios.Enums.Tratamiento
import com.example.escaner.Model.Usuarios.UserDAO
import com.example.escaner.R
import com.example.escaner.databinding.FragmentPerfilUsuarioBinding
import com.example.escaner.ui.registro.Fragments.Fragments.EditarFotoFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class PerfilUsuarioFragment : Fragment(R.layout.fragment_perfil_usuario) {

    private lateinit var binding: FragmentPerfilUsuarioBinding
    private val viewModel: PerfilUsuarioModel by activityViewModels()

    private var currentUser: UserDAO = UserDAO()
    private var usuarioModificado: UserDAO = UserDAO()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPerfilUsuarioBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
        configurarRadioButtonGroups()
    }



    private fun initObservers() {
        viewModel.usuarioBuscado.observe(viewLifecycleOwner) { user ->
            if(user!= null) {
                currentUser = user
                usuarioModificado = user.copy()
                cargarDatosEnPerfil(user)

                Log.d("USUARIO EN FRAGMENT", " ${user}")
            } else{
                Log.d("USUARIO EN FRAGMENT VACIO", " ${user}")
            }
        }
    }



    @SuppressLint("SetTextI18n")
    private fun cargarDatosEnPerfil(user: UserDAO) {
        cargarFoto(user)
        cargarNombre(user)
        cargarFechaNacimiento(user)
        cargarGenero(user)
        cargarCiudad(user)
        cargarPeloTenido(user)
        cargarColorPelo(user)
        cargarLargoPelo(user)
        cargarTexturaPelo(user)
        cargarCondicionPelo(user)
        cargarTipoPelo(user)
        cargarTratamientoPelo(user)
        cargarAlergias(user)

        Log.d("VARIABLE CURRENT EN FRAGMENT", " ${currentUser}")
    }

    private fun cargarFoto(user: UserDAO) {
        if(user.fotoPerfil) {
            val bitmap = obtenerFotoPerfil()
            if (bitmap != null) {
                binding.fotoPerfil.setImageBitmap(bitmap)
            } else {
                Log.d("USER", "LA FOTO DE PERFIL NO EXISTE")

                binding.fotoPerfil.setImageResource(R.mipmap.ic_sin_foto_perfil)
            }
        } else {
            Log.d("USER", "NO FOTO DE PERFIL")

            binding.fotoPerfil.setImageResource(R.mipmap.ic_sin_foto_perfil)
        }
    }

    private fun obtenerFotoPerfil(): Bitmap? {
        return EditarFotoFragment.obtenerImagenDesdeAlmacenamientoInterno(requireContext(), "FotoPerfilCamara.jpg")
            ?: EditarFotoFragment.obtenerImagenDesdeAlmacenamientoInterno(requireContext(), "FotoPerfilGaleria.jpg")
    }

    @SuppressLint("SetTextI18n")
    private fun cargarNombre(user: UserDAO) {
        val nombre = user.nombre
        val apellido = user.apellido

        binding.nombreUsuario.text = "$nombre $apellido"
    }

    private fun cargarFechaNacimiento(user: UserDAO) {
        val fechaFormateada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(user.fechaNacimiento)

        binding.fechaNaciPerfil.setText(fechaFormateada)
    }

    @SuppressLint("SetTextI18n")
    private fun cargarCiudad(user: UserDAO) {
        val provincia = user.provincia
        val ciudad = user.localidad
        binding.ciudadPerfil.setText("$provincia - $ciudad")
    }

    private fun cargarGenero(user: UserDAO) {
        binding.generoPerfil.setText(user.genero.toString())
    }

    private fun cargarFotoPerfil() {

    }

    @SuppressLint("SetTextI18n")
    private fun cargarPeloTenido(user: UserDAO) {
        if (user.peloTenido) {
            user.tenido = Tenido.Tenido

            binding.tintePeloPerfil.setText("Cabello teñido")
        } else {
            binding.tintePeloPerfil.setText("Cabello sin tinte")
            user.tenido = Tenido.Natural
        }
    }

    private fun cargarColorPelo(user: UserDAO) {
        binding.colorPeloPerfil.setText(user.peloColor)
    }

    private fun cargarLargoPelo(user: UserDAO) {
        binding.largoPeloPerfil.setText(user.largo.toString())
    }

    private fun cargarTexturaPelo(user: UserDAO) {
        binding.texturaPeloPerfil.setText(user.peloTextura.toString())
    }

    private fun cargarCondicionPelo(user: UserDAO) {
        binding.condicionPeloPerfil.setText(user.peloCondicion.toString())
    }

    private fun cargarTipoPelo(user: UserDAO) {
        binding.tipoPeloPerfil.setText(user.peloTipo.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun cargarTratamientoPelo(user: UserDAO) {
        if (user.peloTratamiento){
            binding.tratamientoPeloPerfil.setText(user.tipoTratamiento.toString())
        } else {
            binding.tratamientoPeloPerfil.setText("Sin tratamiento")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun cargarAlergias(user: UserDAO) {
        if (user.alergias.isNotEmpty()){
            binding.alergiasPeloPerfil.setText(user.alergias)
        } else {
            binding.alergiasPeloPerfil.setText("Sin alergias")
        }
    }

    private fun initListeners() {
        // Lógica para mostrar y ocultar opciones de edición
        escuchadoresEndIcons()

        // Lógica para manejar las confirmaciones de cambios
        listenersChecks()

    }

    private fun escuchadoresEndIcons() {
        binding.layoutTinte.setEndIconOnClickListener {
            toggleVisibilidad(binding.linearTintePeloPerfil, binding.layoutTinte)
        }



        binding.layoutColor.setEndIconOnClickListener {
            toggleEditar(binding.colorPeloPerfil, binding.layoutColor)
        }

        binding.layoutLargo.setEndIconOnClickListener {
            toggleVisibilidad(binding.linearLargoPelo, binding.layoutLargo)
        }

        binding.layoutTextura.setEndIconOnClickListener {
            toggleVisibilidad(binding.linearTexturaPeloPerfil, binding.layoutTextura)
        }

        binding.layoutCondicion.setEndIconOnClickListener {
            toggleVisibilidad(binding.linearCondicionPeloPerfil, binding.layoutCondicion)
        }

        binding.layoutTipo.setEndIconOnClickListener {
            toggleVisibilidad(binding.linearTiposPeloPerfil, binding.layoutTipo)
        }

        binding.layoutTratamiento.setEndIconOnClickListener {
            toggleVisibilidad(binding.linearTrataPelo, binding.layoutTratamiento)
        }

        binding.layoutAlergias.setEndIconOnClickListener {
            toggleEditar(binding.alergiasPeloPerfil, binding.layoutAlergias)
        }
    }

    private fun toggleVisibilidad(view: View, layout: TextInputLayout) {
        if (view.isVisible) {
            view.visibility = View.GONE
            layout.setEndIconDrawable(R.drawable.edit_icon)
        }
        else {
            view.visibility = View.VISIBLE
            layout.setEndIconDrawable(R.drawable.icon_check)
        }
    }

    private fun toggleEditar(view: EditText, layout: TextInputLayout) {
        if (view.isEnabled) {
            view.isEnabled = false
            layout.setEndIconDrawable(R.drawable.edit_icon)
        } else {
            view.isEnabled = true
            layout.setEndIconDrawable(R.drawable.icon_check)
        }
    }

    private fun listenersChecks() {
        binding.checkOkTintePelo.setOnClickListener {
            actualizarTinte()
            binding.linearTintePeloPerfil.visibility = View.GONE
            binding.layoutTinte.setEndIconDrawable(R.drawable.edit_icon)
        }

        binding.checkOkLargoPelo.setOnClickListener {
            actualizarLargoPelo()
            binding.linearLargoPelo.visibility = View.GONE
            binding.layoutLargo.setEndIconDrawable(R.drawable.edit_icon)
        }

        binding.checkOkTexturaPelo.setOnClickListener {
            actualizarTexturaPelo()
            binding.linearTexturaPeloPerfil.visibility = View.GONE
            binding.layoutTextura.setEndIconDrawable(R.drawable.edit_icon)
        }

        binding.checkOkCondicionPelo.setOnClickListener {
            actualizarCondicionPelo()
            binding.linearCondicionPeloPerfil.visibility = View.GONE
            binding.layoutCondicion.setEndIconDrawable(R.drawable.edit_icon)
        }

        binding.checkOkTipoPelo.setOnClickListener {
            actualizarTipoPelo()
            binding.linearTiposPeloPerfil.visibility = View.GONE
            binding.layoutTipo.setEndIconDrawable(R.drawable.edit_icon)
        }

        binding.checkOkTratamientoPelo.setOnClickListener {
            actualizarTratamientoPelo()
            binding.linearTrataPelo.visibility = View.GONE
            binding.layoutTratamiento.setEndIconDrawable(R.drawable.edit_icon)
        }

        binding.layoutAlergias.setEndIconOnClickListener {
            if (binding.alergiasPeloPerfil.isEnabled) {
                actualizarAlergias()
                binding.alergiasPeloPerfil.isEnabled = false
                binding.layoutAlergias.setEndIconDrawable(R.drawable.edit_icon)
            } else {
                binding.alergiasPeloPerfil.isEnabled = true
                binding.layoutAlergias.setEndIconDrawable(R.drawable.icon_check)
            }
        }
    }

    private fun actualizarTinte() {
        val seleccionTinte = binding.radioGroupTinte.checkedRadioButtonId == R.id.btnSiTinte

        if (currentUser.peloTenido != seleccionTinte) {
            usuarioModificado.peloTenido = seleccionTinte

            viewModel.actualizarUsuario("tenido", seleccionTinte)

            cargarPeloTenido(usuarioModificado)
        }
    }

    private fun actualizarLargoPelo() {
        val nuevoLargo = when {
            binding.radioGroupLargo1.checkedRadioButtonId == R.id.btnMuyCorto -> Largo.MuyCorto
            binding.radioGroupLargo1.checkedRadioButtonId == R.id.btnCorto -> Largo.Corto
            binding.radioGroupLargo2.checkedRadioButtonId == R.id.btnMedio -> Largo.Medio
            binding.radioGroupLargo2.checkedRadioButtonId == R.id.btnLargo -> Largo.Largo
            else -> currentUser.largo
        }

        if (currentUser.largo != nuevoLargo) {
            usuarioModificado.largo = nuevoLargo
            viewModel.actualizarUsuario("peloLargo", nuevoLargo)
            cargarLargoPelo(usuarioModificado)
        }
    }

    private fun actualizarTexturaPelo() {
        val nuevaTextura = when (binding.radioGroupTexturaPelo.checkedRadioButtonId) {
            R.id.btnFino -> Textura.Fino
            R.id.btnGrueso -> Textura.Grueso
            else -> currentUser.peloTextura
        }

        if (currentUser.peloTextura != nuevaTextura) {
            usuarioModificado.peloTextura = nuevaTextura
            viewModel.actualizarUsuario("peloTextura", nuevaTextura)
            cargarTexturaPelo(usuarioModificado)
        }
    }

    private fun actualizarCondicionPelo() {
        val nuevaCondicion = when (binding.radioGroupCondicionPelo.checkedRadioButtonId) {
            R.id.btnSeco -> Condicion.Seco
            R.id.btnGraso -> Condicion.Graso
            R.id.btnMixto -> Condicion.Mixto
            else -> currentUser.peloCondicion
        }

        if (currentUser.peloCondicion != nuevaCondicion) {
            usuarioModificado.peloCondicion = nuevaCondicion
            viewModel.actualizarUsuario("peloCondicion", nuevaCondicion)
            cargarCondicionPelo(usuarioModificado)
        }
    }

    private fun actualizarTipoPelo() {
        val nuevoTipo = when {
            binding.radioGroupTipoPelo1.checkedRadioButtonId == R.id.btnLiso -> Tipo.Liso
            binding.radioGroupTipoPelo1.checkedRadioButtonId == R.id.btnOndulado -> Tipo.Ondulado
            binding.radioGroupTipoPelo2.checkedRadioButtonId == R.id.btnRizado -> Tipo.Rizado
            binding.radioGroupTipoPelo2.checkedRadioButtonId == R.id.btnAfro -> Tipo.Afro
            else -> currentUser.peloTipo
        }

        if (currentUser.peloTipo != nuevoTipo) {
            usuarioModificado.peloTipo = nuevoTipo
            viewModel.actualizarUsuario("peloTipo", nuevoTipo)
            cargarTipoPelo(usuarioModificado)
        }
    }

    private fun actualizarTratamientoPelo() {
        val nuevoTratamiento = when {
            binding.radioGroupTratamientoPelo1.checkedRadioButtonId == R.id.btnAlisado -> Tratamiento.Alisado
            binding.radioGroupTratamientoPelo1.checkedRadioButtonId == R.id.btnHidratacion -> Tratamiento.Hidratacion
            binding.radioGroupTratamientoPelo2.checkedRadioButtonId == R.id.btnOndas -> Tratamiento.Ondas
            else -> currentUser.tipoTratamiento
        }

        if (currentUser.tipoTratamiento != nuevoTratamiento) {
            usuarioModificado.tipoTratamiento = nuevoTratamiento
            viewModel.actualizarUsuario("tipoTratamiento", nuevoTratamiento)
            cargarTratamientoPelo(usuarioModificado)
        }
    }

    private fun actualizarAlergias() {
        val nuevasAlergias = binding.alergiasPeloPerfil.text.toString()

        if (currentUser.alergias != nuevasAlergias) {
            usuarioModificado.alergias = nuevasAlergias
            viewModel.actualizarUsuario("alergias", nuevasAlergias)
            cargarAlergias(usuarioModificado)
        }
    }


//_______________________________CONTROL DE LA SELECCION DE LOS RADIO BUTTONS_______________________________

    private fun manejadorRadioButtonsGroups(vararg radioGroups: RadioGroup) {
        val listener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                radioGroups.forEach { radioGroup ->
                    if (radioGroup != group) {
                        radioGroup.clearCheck()
                    }
                }
            }
        }

        radioGroups.forEach { radioGroup ->
            radioGroup.setOnCheckedChangeListener(listener)
        }
    }

    private fun configurarRadioButtonGroups() {
        manejadorRadioButtonsGroups(binding.radioGroupTinte)
        manejadorRadioButtonsGroups(binding.radioGroupLargo1, binding.radioGroupLargo2)
        manejadorRadioButtonsGroups(binding.radioGroupTipoPelo1, binding.radioGroupTipoPelo2)
        manejadorRadioButtonsGroups(binding.radioGroupTexturaPelo)
        manejadorRadioButtonsGroups(binding.radioGroupCondicionPelo)
        manejadorRadioButtonsGroups(binding.radioGroupTratamientoPelo1, binding.radioGroupTratamientoPelo2)
    }

}