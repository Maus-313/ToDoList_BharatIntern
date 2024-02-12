package com.example.tasklist.ui.add_edit_task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.Task
import com.example.todolist.data.TaskRepository
import com.example.todolist.ui.add_edit_task.AddEditTaskEvent
import com.example.todolist.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var task by mutableStateOf<Task?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    init {
//        val taskId = savedStateHandle.get<Int>("taskId")!!
//        if(taskId != -1) {
//            viewModelScope.launch {
//                repository.getTaskById(taskId)?.let { task ->
//                    title = task.title
//                    description = task.description ?: ""
//                    this@AddEditTaskViewModel.task = task
//                }
//            }
//        }
        val taskId = savedStateHandle.get<Int>("taskId")
        if(taskId != null && taskId != -1) {
            viewModelScope.launch {
                repository.getTaskById(taskId)?.let { task ->
                    title = task.title
                    description = task.description ?: ""
                    this@AddEditTaskViewModel.task = task
                }
            }
        }
    }

    fun onEvent(event: AddEditTaskEvent) {
        when(event) {
            is AddEditTaskEvent.OnTitleChange -> {
                title = event.title
            }
            is AddEditTaskEvent.OnDescriptionChange -> {
                description = event.description
            }
            is AddEditTaskEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if(title.isBlank()) {
                        sendUiEvent(UiEvent.ShowSnackBar(
                            message = "The title can't be empty"
                        ))
                        return@launch
                    }
                    repository.insertTask(
                        Task(
                            title = title,
                            description = description,
                            isDone = task?.isDone ?: false,
                            id = task?.id
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
    }

    fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}