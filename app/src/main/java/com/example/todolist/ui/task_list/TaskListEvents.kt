package com.example.todolist.ui.task_list

import com.example.todolist.data.Task

sealed class TaskListEvents {

    data class OnDeleteTaskClick(val task: Task): TaskListEvents()
    data class OnDoneChange(val task: Task, val isDone: Boolean): TaskListEvents()
    object OnUndoDeleteClick: TaskListEvents()
    data class OnTaskClick(val task: Task): TaskListEvents()
    object OnAddTodoClick: TaskListEvents()

}