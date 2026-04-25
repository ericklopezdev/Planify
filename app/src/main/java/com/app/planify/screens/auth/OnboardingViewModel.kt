package com.app.planify.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {

    var name by mutableStateOf("")
        private set
    var career by mutableStateOf("")
        private set
    var university by mutableStateOf("")
        private set

    fun onNameChange(value: String)       { name = value }
    fun onCareerChange(value: String)     { career = value }
    fun onUniversityChange(value: String) { university = value }

    fun complete(onNavigate: () -> Unit) {
        viewModelScope.launch {
            // TODO: guardar perfil en Supabase (nombre, carrera, universidad)
            // TODO: PATCH /rest/v1/profiles { name, career, university }
            onNavigate()
        }
    }
}
