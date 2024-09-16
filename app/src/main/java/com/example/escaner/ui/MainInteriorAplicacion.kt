package com.example.escaner.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.escaner.Model.Productos.ProductoManager
import com.example.escaner.Model.Usuarios.UserDAO
import com.example.escaner.R
import com.example.escaner.databinding.ActivityInteriorAplicacionBinding
import com.example.escaner.ui.inicioAcceso.InicioAccesoFragment
import com.example.escaner.ui.registro.Fragments.Fragments.EditarFotoFragment
import com.example.escaner.ui.scanner.ScanActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainInteriorAplicacion : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityInteriorAplicacionBinding

    private lateinit var context: Context

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context = this

        ProductoManager.cargarProductos()

        binding = ActivityInteriorAplicacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarInteriorAplicacion.toolbar)

        initListeners()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_interior_aplicacion)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_perfil_user, R.id.nav_sobre_nosotros
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid.toString()

        val headerView = navView.getHeaderView(0)
        val nombreTextView = headerView.findViewById<TextView>(R.id.nombreUserMenuLateral)
        val fotoImageView = headerView.findViewById<ImageView>(R.id.fotoPerfilMenuLateral)

        lifecycleScope.launch {
            val user: UserDAO = buscarPorUID(userId)

            nombreTextView.text = "${user.nombre} ${user.apellido}"

            if(user.fotoPerfil) {
                val bitmap = obtenerFotoPerfil()
                if (bitmap != null) {
                    fotoImageView.setImageBitmap(bitmap)
                } else {
                    Log.d("USER", "LA FOTO DE PERFIL NO EXISTE")

                    fotoImageView.setImageResource(R.mipmap.ic_sin_foto_perfil)
                }
            } else {
                Log.d("USER", "NO FOTO DE PERFIL")

                fotoImageView.setImageResource(R.mipmap.ic_sin_foto_perfil)
            }
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_perfil_user -> {
                    navController.navigate(R.id.nav_perfil_user)
                    true
                }
                R.id.nav_sobre_nosotros -> {
                    navController.navigate(R.id.nav_sobre_nosotros)
                    true
                }
                R.id.nav_salir -> {
                    navController.navigate(R.id.nav_salir)
                    cerrarSesion()
                    true
                }
                else -> super.onOptionsItemSelected(menuItem)
            }
        }
    }

    fun onProfileImageClick(view: View) {
        supportFragmentManager.commit {
            replace<EditarFotoFragment>(R.id.nav_host_fragment_content_interior_aplicacion)
            addToBackStack(null)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.interior_aplicacion, menu)
        return true
    }

    private fun obtenerFotoPerfil(): Bitmap? {
        return EditarFotoFragment.obtenerImagenDesdeAlmacenamientoInterno(context, "FotoPerfilCamara.jpg")
            ?: EditarFotoFragment.obtenerImagenDesdeAlmacenamientoInterno(context, "FotoPerfilGaleria.jpg")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_interior_aplicacion)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initListeners() {
        binding.appBarInteriorAplicacion.fab.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        }
    }

    suspend fun buscarPorUID(iud: String): UserDAO = withContext(Dispatchers.IO) {
        val provider: FirebaseFirestore = FirebaseFirestore.getInstance()

        try {
            val document = provider.collection("usuarios").document(iud).get().await()
            if (document.exists()) {
                document.toObject(UserDAO::class.java) ?: UserDAO()
            } else {
                Log.d("USER OBTENIDO REPOSITORIO", "No existe el usuario con UID: $iud")
                UserDAO()
            }
        } catch (e: Exception) {
            Log.d("ERROR AL BUSCAR ID USER", "${e.message}")
            UserDAO()
        }
    }

    private fun cerrarSesion() {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()

        val intent = Intent(this, InicioAccesoFragment::class.java)
        startActivity(intent)

        // Cerrar el drawer si está abierto
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }
}