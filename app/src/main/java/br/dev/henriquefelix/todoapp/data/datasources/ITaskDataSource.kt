package br.dev.henriquefelix.todoapp.data.datasources

import br.dev.henriquefelix.todoapp.domain.models.Task

interface ITaskDataSource {
    fun createTask(task : Task) : Long
    fun searchTasks(search : String) : List<Task>
    fun updateTask(task : Task) : Unit
    fun deleteTask(task : Task) : Unit
}