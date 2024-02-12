package com.example.todolist.ui.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.Task
import com.example.todolist.data.TaskRepository
import com.example.todolist.util.Routes
import com.example.todolist.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {

    val tasks = repository.getTask()

    private val _uiEvent = Channel<UiEvent>() // Mutable Version
    val uiEvent = _uiEvent.receiveAsFlow() // Immutable version

    private var deletedTask: Task? = null

    fun onEvent(event: TaskListEvents) {
        when(event) {
            is TaskListEvents.OnTaskClick -> {
                sendUiEvent(UiEvent.Navigation(Routes.ADD_EDIT_TODO + "todoId=${event.task.id}"))
            }

            is TaskListEvents.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigation(Routes.ADD_EDIT_TODO))
            }

            is TaskListEvents.OnUndoDeleteClick -> {
                deletedTask?.let {task ->
                    viewModelScope.launch {
                        repository.insertTask(task)
                    }
                }
            }

            is TaskListEvents.OnDeleteTaskClick -> {
                viewModelScope.launch {
                    deletedTask = event.task
                    repository.deleteTask(event.task)
                    sendUiEvent(UiEvent.ShowSnackBar(
                        message = "Task Deleted!",
                        action = "Undo"
                    ))
                }
            }

            is TaskListEvents.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTask(
                        event.task.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}