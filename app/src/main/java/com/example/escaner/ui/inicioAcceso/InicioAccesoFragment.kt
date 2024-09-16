package com.example.escaner.ui.inicioAcceso

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.example.escaner.Model.viewPagerImages.CarouselItem
import com.example.escaner.R
import com.example.escaner.Util.Resource
import com.example.escaner.databinding.PantallaInicioBinding
import com.example.escaner.ui.MainInteriorAplicacion
import com.example.escaner.ui.passwdRecovery.PasswdRecoveryFragment
import com.example.escaner.ui.registro.Fragments.Fragments.BienvenidaFragment
import com.example.escaner.ui.registrocorreo.RegistroCorreoFragment
import com.example.escaner.ui.viewPagerAdapter.CarouselAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InicioAccesoFragment : Fragment(R.layout.pantalla_inicio) {

    private lateinit var binding: PantallaInicioBinding
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private val viewModel: InicioAccesoViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = PantallaInicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iniciarListeners()
        iniciarObservadores()

        carouselAdapter = CarouselAdapter(listaCarouselItems())
        binding.viewPagerImages.adapter = carouselAdapter

        sliderHandler.postDelayed(sliderRunnable, 3000)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        //CERRAR SESION PARA QUE NO ACCEDA DIRECTO
        signOut()
        binding.signInGoogle2.setOnClickListener{
            signInGoogle()
        }


        viewModel.mensajeError.observe(viewLifecycleOwner, Observer { mensaje ->
            mensaje?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })


    }



//    ACCESO CON GOOGLE

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                accesoConGoogle(account!!)
            } catch (e: ApiException) {
                Log.d("ERROR GOOGLE", e.toString())
                Toast.makeText(requireContext(), "Error acceso con Google", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun accesoConGoogle(account: GoogleSignInAccount) {
            val idToken = account.idToken ?: return
            viewModel.accesoGoogle(idToken,
                onRegistered = {
                    Log.d("USUARIO REGISTRADO", "ACCEDE A APLICACION")
                    accederAplicacion()

                },
                onNotRegistered = {
                    Log.d("USUARIO NO REGISTRADO", "ACCEDE A REGISTRO")
                    accederPantallaRegistroGoogle()

                },
                onFailure = { e ->
                    Toast.makeText(requireContext(), "Fallo de autentificación", Toast.LENGTH_SHORT).show()
                })
        }




    //MANEJADOR PARA EL INICIO DE SESION
    private fun iniciarObservadores() {
        viewModel.estadoInicio.observe(viewLifecycleOwner) { estado ->
            when (estado) {
                is Resource.completado -> {
                    accederAplicacion()
                    Log.d("ACCESO INICIO", estado.toString())
                }
                is Resource.error -> {
                    Log.d("ERROR INICIO", estado.message)
                }
                is Resource.cargando -> Unit
                else -> Unit
            }
        }
    }


    private fun handleLogin(email: String, password: String) {
        viewModel.login(email, password)
    }


    private fun iniciarListeners() {
        //ACCESO
        binding.iniciarSesion.setOnClickListener {
            binding.linearBotonesInicio.visibility = View.INVISIBLE
            binding.emailInputLayout.visibility = View.VISIBLE
            binding.linearGoogle.visibility = View.VISIBLE

        }

        //BOTON DE ATRAS CUANDO ESTAN DENTRO DE SECCION ACCESO
        binding.volverAInicio.setOnClickListener {
            binding.linearBotonesInicio.visibility = View.VISIBLE
            binding.emailInputLayout.visibility = View.INVISIBLE
            binding.linearGoogle.visibility = View.INVISIBLE
            borrarCamposAccesoApp()
        }

        //REGISTRO
        binding.crearCuenta.setOnClickListener {
            borrarCamposAccesoApp()
            accederPantallaRegistro()
        }

        binding.buttonLogin.setOnClickListener {
            validacion()
        }

        binding.buttonRecuperarClave.setOnClickListener {
            accederPasswdRecovery()
        }
    }

    private fun borrarCamposAccesoApp() {
        binding.emailInicio.text = null
        binding.passwordInicio.text = null
    }

    private fun validacion() {
        if (viewModel.validarCorreo(binding.emailInicio.text.toString())) {
            handleLogin(binding.emailInicio.text.toString(), binding.passwordInicio.text.toString())

        }
    }

    private fun accederAplicacion() {
        binding.emailInicio.text = null
        binding.passwordInicio.text = null

//        CAMBIAMOS A MENU PERFIL USUARIO
        val intent = Intent(requireContext(), MainInteriorAplicacion::class.java)
        startActivity(intent)
    }



    private fun accederPantallaRegistroGoogle(){
        requireActivity().supportFragmentManager.commit {
            replace<BienvenidaFragment>(R.id.contenedor_fragments)
            setReorderingAllowed(true)
            addToBackStack(null) // Name can be null
        }
    }
    private fun accederPantallaRegistro(){
        requireActivity().supportFragmentManager.commit {
            replace<RegistroCorreoFragment>(R.id.contenedor_fragments)
            setReorderingAllowed(true)
            addToBackStack(null) // Name can be null
        }
    }

    private fun accederPasswdRecovery(){
        requireActivity().supportFragmentManager.commit {
            replace<PasswdRecoveryFragment>(R.id.contenedor_fragments)
            setReorderingAllowed(true)
            addToBackStack(null) // Name can be null
        }
    }


    private fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener(requireActivity(), OnCompleteListener {
            if (it.isSuccessful) {
                Log.d("CERRAR SESION GOOGLE", "OK")
            }
            else {
                Log.d(" CERRAR SESION GOOGLE", "NO CERRADA")
            }

        })
    }


    //_______________________________________________________________________________________________________
    //CAMBIO DE IMAGENES AUTOMATICAMENTE PARA LA PANTALLA DE INICIO
    private val sliderHandler = Handler(Looper.getMainLooper())
    private val sliderRunnable = object : Runnable {
        override fun run() {

            //inicializa carouselAdapter

            // currentPosition se obtiene del ViewPager2
            val currentPosition = binding.viewPagerImages.currentItem
            // Calcula la siguiente posición
            val nextPosition =
                //accedemos a la clase creada para el adaptador para sacar el numero de items
                if (currentPosition + 1 >= carouselAdapter.itemCount) 0 else currentPosition + 1
            // Mueve el ViewPager2 a la siguiente posición
            binding.viewPagerImages.setCurrentItem(nextPosition, true)
            // Programa la próxima ejecución de este mismo Runnable
            sliderHandler.postDelayed(this, 3000) // Cambio cada 3 segundos
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Elimina los callbacks para evitar fugas de memoria
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    //METODO PARA CREAR LISTA CON IMAGENES + EL TITULO DE CADA UNA
    private fun listaCarouselItems(): List<CarouselItem> {
        return listOf(
            CarouselItem(
                R.drawable.carousel_item1,
                "Encuentra los productos que tu pelo necesita"
            ),
            CarouselItem(
                R.drawable.carousel_item2,
                "Conoce de que están compuestos los productos que usas en tu pelo"
            ),
            CarouselItem(
                R.drawable.carousel_item3,
                "Cuidar tu pelo nunca fue tan fácil y rápido"
            )
        )
    }
//_______________________________________________________________________________________________________









}