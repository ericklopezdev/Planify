package com.app.planify.screens.tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// TODO: mover a api/models/TaskModels.kt cuando se conecte el backend
data class Task(
    val id: Int,
    val title: String,
    val dueDate: String,
    val priority: String,
    val isDone: Boolean = false
)

class TasksViewModel : ViewModel() {

    var state by mutableStateOf<TasksState>(TasksState.Loading)
        private set

    init { loadTasks() }

    fun loadTasks() {
        viewModelScope.launch {
            state = TasksState.Loading
            try {
                // TODO: reemplazar con TasksRepository.getTasks()
                // TODO: GET /rest/v1/tasks?user_id=eq.{userId}&select=*
                val fake = listOf(
                    Task(1, "Estudiar parcial de cálculo",    "10 May",  "Alta"),
                    Task(2, "Entregar laboratorio de física", "12 May",  "Media"),
                    Task(3, "Leer capítulo 4 de historia",   "15 May",  "Baja"),
                    Task(4, "Preparar exposición grupal",     "18 May",  "Alta"),
                    Task(5, "Resolver ejercicios de álgebra", "20 May", "Media")
                )
                state = TasksState.Success(fake)
            } catch (e: Exception) {
                state = TasksState.Error("No se pudieron cargar las tareas")
            }
        }
    }
}

sealed class TasksState {
    object Loading                            : TasksState()
    data class Success(val tasks: List<Task>) : TasksState()
    data class Error(val message: String)     : TasksState()
}
