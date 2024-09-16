package com.example.escaner.ui.InteriorAplicacion.sobreNosotros

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SobreNosotrosModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }

    val text: LiveData<String> = _text
}