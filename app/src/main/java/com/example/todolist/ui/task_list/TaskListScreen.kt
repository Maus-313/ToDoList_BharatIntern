package com.example.todolist.ui.task_list

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material.*
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.util.UiEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed

@Composable
fun TaskListScreen(
    onNavigate: (UiEvent.Navigation) -> Unit,
    viewModel: TaskListViewModel
){
    val task = viewModel.tasks.collectAsState(initial = emptyList())
//    val scaffoldState = rememberScaffoldState()
    val scaffoldState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect{event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    val result = scaffoldState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if(result == SnackbarResult.ActionPerformed){
                        viewModel.onEvent(TaskListEvents.OnUndoDeleteClick)
                    }
                }
                is UiEvent.Navigation -> onNavigate(event)
                else -> Unit
            }
        }
    }

//    Scaffold(
//        snackbarHost = { SnackbarHost(scaffoldState) },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                viewModel.onEvent(TaskListEvents.OnAddTodoClick)
//            }) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "Add"
//                )
//            }
//        }
//    ){
//        LazyColumn(
//            modifier = Modifier.fillMaxSize()
//        ){
//           items(task.value){task ->
//               TaskItem(
//                   task = task,
//                   onEvents = viewModel::onEvent,
//                   modifier = Modifier
//                       .fillMaxWidth()
//                       .clickable {
//                           viewModel.onEvent(TaskListEvents.OnTaskClick(task))
//                       }
//                       .padding(16.dp)
//               )
//           }
//        }
//    }

    Scaffold(
        snackbarHost = { SnackbarHost(scaffoldState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TaskListEvents.OnAddTodoClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ){
            padding -> // Add this line
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // And this line
        ){
            items(task.value){task ->
                TaskItem(
                    task = task,
                    onEvents = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onEvent(TaskListEvents.OnTaskClick(task))
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}