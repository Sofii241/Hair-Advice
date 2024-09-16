package com.example.escaner.ui.registro

//noinspection SuspiciousImport
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escaner.Firebase.casosDeUso.CreateUserCasoUso
import com.example.escaner.Model.Usuarios.Enums.Condicion
import com.example.escaner.Model.Usuarios.Enums.Genero
import com.example.escaner.Model.Usuarios.Enums.Largo
import com.example.escaner.Model.Usuarios.Enums.Tenido
import com.example.escaner.Model.Usuarios.Enums.Textura
import com.example.escaner.Model.Usuarios.Enums.Tipo
import com.example.escaner.Model.Usuarios.Enums.Tratamiento
import com.example.escaner.Model.Usuarios.UserDAO
import com.example.escaner.R
import com.example.escaner.Util.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class RegistroViewModel @Inject constructor(private val registroFirebaseCasoUso: CreateUserCasoUso): ViewModel() {

    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> get() = _mensajeError

    private var user: UserDAO = UserDAO()

    private val _estadoRegistroFirebase = MutableLiveData<Resource<Boolean>>()
    val estadoRegistroFirebase: LiveData<Resource<Boolean>> get() = _estadoRegistroFirebase

    private val _currentUser = MutableLiveData<String>()
    val currentUser: LiveData<String> get() = _currentUser

    var fotoPerfilSeleccionada: Boolean = false
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var idCurrentUser = ""

    init {
        idCurrentUser = firebaseAuth.currentUser?.uid.toString()
        _currentUser.postValue(idCurrentUser)
    }

    fun registrarUserFirestore(uid:String, user: UserDAO) {
        viewModelScope.launch {
            registroFirebaseCasoUso(uid, user).onEach { state ->
             _estadoRegistroFirebase.value = state
            }.launchIn(viewModelScope)
        }
    }

    fun usuario(): UserDAO {
        return user
    }

    //_____________________________NOMBRE_________________________________
    fun validarNombre(nombre: String): Boolean {
        if (nombre.isEmpty()) {
            _mensajeError.value = "El nombre no puede estar vacío"
            return false
        } else if (!nombre.matches(Regex("^[a-zA-ZÁÉÍÓÚáéíóúÑñ\\s]+$"))) { // Permite letras con tildes y espacios
            _mensajeError.value = "Hay valores incorrectos en el nombre"
            return false
        } else if (nombre.length < 3) {
            _mensajeError.value = "¡Ups! Parece que el nombre está incompleto"
            return false
        }
        _mensajeError.value = null
        return true
    }

    fun validarApellido(apellido: String): Boolean {
        if (apellido.isEmpty()) {
            _mensajeError.value = "El apellido no puede estar vacío"
            return false
        } else if (!apellido.matches(Regex("^[a-zA-ZÁÉÍÓÚáéíóúÑñ\\s]+$"))) { // Permite letras con tildes y espacios
            _mensajeError.value = "Hay valores incorrectos en el apellido"
            return false
        } else if (apellido.length < 3) {
            _mensajeError.value = "¡Ups! Parece que el apellido está incompleto"
            return false
        }
        _mensajeError.value = null
        return true
    }

    fun registrarNombre(nom: String) {
        user.nombre = nom
    }
    fun registrarApellido(ap: String) {
        user.apellido = ap
    }


//    _____________________________FECHA____________________________________--

    fun comprobacionCampoFechaVacio(fecha: String): Boolean {
        if (fecha.isEmpty()) {
            _mensajeError.value = "Debes seleccionar una fecha"
            return false
        }

        return true
    }

    fun comprobacionCaracteresFecha(fecha: String): Boolean {
        if (!fecha.matches(Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}"))) {
            _mensajeError.value ="El formato de la fecha es incorrecto"
            return false
        }

        return true
    }


    fun registrarFechaSeleccionada(fec: Date) {
       user.fechaNacimiento = fec
    }


//    _________________________________TOAST_______________________________________

    fun limpiarMensajeToast() {
        _mensajeError.value = null
    }

//    GENERO

    fun comprobacionCheckMarcadoMujer(mujer: Boolean): Boolean {
        if (!mujer) {
            _mensajeError.value = "Debes seleccionar una opción"
            return false
        }

        return true
    }
    fun comprobacionCheckMarcadoHombre(hombre: Boolean): Boolean {
        if (!hombre) {
            _mensajeError.value = "Debes seleccionar una opción"
            return false
        }

        return true
    }
    fun comprobacionCheckMarcadoNo(no: Boolean): Boolean {
        if (!no) {
            _mensajeError.value = "Debes seleccionar una opción"
            return false
        }

        return true
    }

    fun registrarGeneroSeleccionado(gen: Genero) {
        user.genero = gen
    }

    fun arrayLocalidades(): Int {
        return R.array.array_provincia_a_localidades
    }

    fun arrayProvincias(): Int {
        return R.array.provincias
    }

    fun mensajeErrorProvincia(): String {
        return "Los campos son obligatorios"
    }

    fun registrarProvinciaSeleccionada(prov: String) {
        user.provincia = prov
    }

    fun registrarLocalidadSeleccionada(loc: String) {
        user.localidad = loc
    }

    fun spinnerPersonalizado(): Int {
        return R.layout.spinner_item
    }

    // _____________________________________FOTO DE PERFIL______________________________________________

    fun controlSeleccion(btn: ImageButton, btn2: ImageButton, btn3: ImageButton): Boolean{
        fotoPerfilSeleccionada = !(!btn.isSelected && !btn2.isSelected && !btn3.isSelected)

        if (!fotoPerfilSeleccionada) {
            _mensajeError.value = "Debes seleccionar una opción"
        }

        return fotoPerfilSeleccionada
    }

    fun registrarFotoPerfil(boolean: Boolean) {
        user.fotoPerfil = boolean
    }

    fun comprobacionCheckMarcadoTinte(checkSi: Boolean, checkNo: Boolean): Boolean {
        if (!checkSi && !checkNo) {
            _mensajeError.value = "Todos los campos son obligatorios"
            return false
        }
        return true
    }

    fun registrarPeloTenido(boolean: Boolean) {
        if(boolean) {
            user.tenido = Tenido.Tenido
        } else {
            user.tenido = Tenido.Natural
        }
    }

    fun registrarPeloDecolorado(boolean: Boolean) {
        if(boolean) {
            user.tenido = Tenido.Decolorado
        } else {
            user.tenido = Tenido.Natural
        }
    }

    fun comprobacionCampoVacioColor(color: String): Boolean {
        if (color.isEmpty()) {
            _mensajeError.value = "El campo es obligatorio"
            return false
        }

        return true
    }

    fun registrarColorPelo(color: String) {
        user.peloColor = color
    }

    fun registrarLargo(largo: Largo) {
        user.largo = largo
    }

    fun comprobacionMarcadoLargoPelo(seleccionado: Boolean): Boolean {
        if (!seleccionado) {
            _mensajeError.value = "Debes seleccionar una opción"
            return false
        }

        return true
    }

    fun controlMarcadoPantalla(imageView: ImageView) {
        if (!imageView.isSelected) {
            desmarcarSeleccionPantalla(imageView)
        } else {
            marcarSeleccionPantalla(imageView)
        }
    }

     fun marcarSeleccionPantalla(imageView: ImageView) {
        imageView.setBackgroundResource(R.drawable.round_button_bk)
    }

    @SuppressLint("ResourceAsColor")
     fun desmarcarSeleccionPantalla(imageView: ImageView) {
        imageView.setBackgroundResource(0)

    }

    fun registrarPeloTipo(tipo: String) {
        when(tipo){
            "Liso" -> user.peloTipo = Tipo.Liso
            "Rizado" -> user.peloTipo = Tipo.Rizado
            "Ondulado" -> user.peloTipo = Tipo.Ondulado
            "Afro" -> user.peloTipo = Tipo.Afro
        }
    }

    fun registrarPeloTextura(textura: String) {
        when(textura) {
            "Grueso" -> user.peloTextura = Textura.Grueso
            "Fino" -> user.peloTextura = Textura.Fino
        }
    }

    fun registrarPeloCondicion(condicion: String) {
        when(condicion) {
            "Graso" -> user.peloCondicion = Condicion.Graso
            "Seco" -> user.peloCondicion = Condicion.Seco
            "Mixto" -> user.peloCondicion = Condicion.Mixto
        }
    }

   fun listaTiposPelo(): Int {
       return R.array.tipo
   }

    fun listaTexturasPelo(): Int {
        return R.array.textura
    }

    fun listaCondicionesPelo(): Int {
        return R.array.condicion
    }

    fun mensajeErrorSpinerTipoPelo(): String {
        return "Todas las opciones son obligatorias"
    }

    fun listaTratamientos(): Int {
        return R.array.tratamiento
    }

    fun registrarSiPeloTratamiento(boolean: Boolean) {
        user.peloTratamiento = boolean
    }

    fun registrarTipoTratamiento(tipo: String) {
        when(tipo) {
            "Hidratacion" -> user.tipoTratamiento = Tratamiento.Hidratacion
            "Alisado" -> user.tipoTratamiento = Tratamiento.Alisado
            "Ondas permanentes" -> user.tipoTratamiento = Tratamiento.Ondas
        }
    }

    fun mensajeErrorSpinerTratamientoPelo(): String {
        return "Debes seleccionar una opción"
    }

    fun comprobacionCheckMarcadoTratamiento(checkSi: Boolean, checkNo: Boolean): Boolean{
        if (!checkSi && !checkNo) {
            _mensajeError.value = "Debes seleccionar una opción"
            return false
        }

        return true
    }

    fun comprobacionSeleccionTipoTratamiento(seleccionado: Boolean): Boolean {
        if (!seleccionado) {
            _mensajeError.value = "Indica el tipo de tratamiento"
            return false
        }

        return true
    }

    fun validarCampoAlergias(palabra: String): Boolean {
        if (!palabra.matches(Regex("[a-zA-Z]+"))) {
            _mensajeError.value = "Hay valores incorrectos en el nombre"
            return false
        }
        return true
    }

    fun registrarAlergia(palabra: String) {
        user.alergias = palabra
    }

    fun mensajeErrorFotoPerfil() {
        _mensajeError.value = "La foto de perfil es obligatoria"
    }

    fun guardarImagenEnAlmacenamientoInterno(context: Context, bitmap: Bitmap, nombreArchivo: String): Uri {
        val archivo = File(context.filesDir, nombreArchivo)
        FileOutputStream(archivo).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        }
        return Uri.fromFile(archivo)
    }

    fun guardarImagenEnAlmacenamientoInterno(context: Context, uri: Uri, nombreArchivo: String): Uri {
        val archivo = File(context.filesDir, nombreArchivo)
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(archivo)
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        inputStream.close()
        outputStream.close()
        return Uri.fromFile(archivo)
    }

    fun obtenerImagenDesdeAlmacenamientoInterno(context: Context, nombreArchivo: String): Bitmap? {
        val archivo = File(context.filesDir, nombreArchivo)
        return if (archivo.exists()) {
            BitmapFactory.decodeFile(archivo.absolutePath)
        } else {
            null
        }
    }
}