package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tasklist.ui.add_edit_task.AddEditTaskViewModel
import com.example.todolist.ui.add_edit_task.AddEditTaskScreen
import com.example.todolist.ui.task_list.TaskListScreen
import com.example.todolist.ui.task_list.TaskListViewModel
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                val navController = rememberNavController()
                val tasklistviewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
                val addEditTaskViewModel = ViewModelProvider(this).get(AddEditTaskViewModel::class.java)
                NavHost(
                    navController = navController,
                    startDestination = Routes.TODO_LIST
                ) {
                    composable(Routes.TODO_LIST) {
                        TaskListScreen(
                            onNavigate = {
                                navController.navigate(it.route)
                            },
                            viewModel = tasklistviewModel
                        )
                    }
                    composable(
                        route = Routes.ADD_EDIT_TODO + "?todoId={todoId}",
                        arguments = listOf(
                            navArgument(name = "todoId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        AddEditTaskScreen(
                            onPopBackStack = {
                                navController.popBackStack()
                            },
                            viewModel = addEditTaskViewModel 
                        )
                    }
                }
            }
        }
    }
}