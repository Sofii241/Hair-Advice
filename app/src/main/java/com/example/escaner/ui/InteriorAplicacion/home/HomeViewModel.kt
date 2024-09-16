package com.example.escaner.ui.InteriorAplicacion.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escaner.Firebase.user.UserRepositoryImplement
import com.example.escaner.Model.Usuarios.UserDAO
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(

): ViewModel() {



    private val _usuarioBuscado: MutableLiveData<UserDAO> = MutableLiveData()
    val usuarioBuscado: LiveData<UserDAO> get() = _usuarioBuscado






}