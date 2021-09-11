package br.dev.henriquefelix.todoapp.data.repositories

import br.dev.henriquefelix.todoapp.data.datasources.ITaskDataSource
import br.dev.henriquefelix.todoapp.domain.models.Task

class TaskRepositoryImplementation(private val taskDataSource: ITaskDataSource) : ITaskRepository {
    override fun createTask(task: Task): Long {
        return taskDataSource.createTask(task)
    }

    override fun searchTasks(search: String): List<Task> {
        return taskDataSource.searchTasks(search)
    }

    override fun updateTask(task: Task) {
        taskDataSource.updateTask(task)
    }

    override fun deleteTask(task: Task) {
        taskDataSource.deleteTask(task)
    }
}