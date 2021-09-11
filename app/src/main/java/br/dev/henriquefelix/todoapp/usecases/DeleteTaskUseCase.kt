package br.dev.henriquefelix.todoapp.usecases

import br.dev.henriquefelix.todoapp.data.repositories.ITaskRepository
import br.dev.henriquefelix.todoapp.domain.models.Task

class DeleteTaskUseCase(private val taskRepository : ITaskRepository) {
    fun deleteTask(task : Task) = taskRepository.deleteTask(task)
}