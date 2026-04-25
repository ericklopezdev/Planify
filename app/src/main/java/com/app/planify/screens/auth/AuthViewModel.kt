package com.app.planify.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    var email by mutableStateOf("")
        private set

    fun onEmailChange(value: String) { email = value }

    fun continuar(onNavigate: (String) -> Unit) {
        viewModelScope.launch {
            // TODO: POST /auth/v1/otp { email }
            // TODO: el backend devuelve siempre la misma respuesta (anti-enumeración)
            onNavigate(email)
        }
    }
}
