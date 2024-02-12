package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(

    private val dao: TaskDao

): TaskRepository {

    override suspend fun insertTask(task: Task) {
        dao.insertTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task)
    }

    override suspend fun getTaskById(id: Int): Task? {
        return dao.getTodoById(id)
    }

    override fun getTask(): Flow<List<Task>> {
        return dao.getTask()
    }
}