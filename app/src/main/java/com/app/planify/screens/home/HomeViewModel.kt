package com.app.planify.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    // TODO: obtener nombre desde el perfil guardado en Supabase (TokenManager / ProfileRepository)
    var userName by mutableStateOf("Erick")
        private set

    // TODO: reemplazar con TasksRepository.getTasks().count { !it.isDone }
    var pendingTasksCount by mutableStateOf(3)
        private set

    // TODO: obtener últimas 2 tareas desde TasksRepository
    var recentTasks by mutableStateOf(
        listOf(
            "Estudiar parcial de cálculo",
            "Entregar laboratorio de física"
        )
    )
        private set
}
