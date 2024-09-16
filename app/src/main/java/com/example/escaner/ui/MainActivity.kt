package com.example.escaner.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.escaner.R
import com.example.escaner.ui.inicioAcceso.InicioAccesoFragment
import com.example.escaner.ui.registro.Fragments.Fragments.LargoPeloFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<InicioAccesoFragment>(R.id.contenedor_fragments)
        }
    }
}



