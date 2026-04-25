package com.app.planify.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class OtpViewModel : ViewModel() {

    var code by mutableStateOf("")
        private set

    fun onCodeChange(value: String) {
        if (value.length <= 6 && value.all { it.isDigit() }) code = value
    }

    fun verify(email: String, onNewUser: () -> Unit, onExistingUser: () -> Unit) {
        viewModelScope.launch {
            // TODO: POST /auth/v1/verify { email, token: code, type: "email" }
            // TODO: response contiene { access_token, isNewUser }
            // TODO: TokenManager.saveToken(response.accessToken)
            // TODO: if (response.isNewUser) onNewUser() else onExistingUser()
            onNewUser() // T1: simula siempre usuario nuevo → va a Onboarding
        }
    }

    fun resend(email: String) {
        viewModelScope.launch {
            // TODO: POST /auth/v1/otp { email } — reenviar código
        }
    }
}
