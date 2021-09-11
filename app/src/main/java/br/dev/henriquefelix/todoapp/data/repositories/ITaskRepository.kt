package br.dev.henriquefelix.todoapp.data.repositories

import br.dev.henriquefelix.todoapp.domain.models.Task

interface ITaskRepository {
    fun createTask(task : Task) : Long
    fun searchTasks(search : String) : List<Task>
    fun updateTask(task : Task) : Unit
    fun deleteTask(task : Task) : Unit
}