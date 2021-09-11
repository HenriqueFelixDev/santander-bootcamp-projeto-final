package br.dev.henriquefelix.todoapp.usecases

import br.dev.henriquefelix.todoapp.data.repositories.ITaskRepository
import br.dev.henriquefelix.todoapp.domain.models.Task

class UpdateTaskUseCase(private val taskRepository : ITaskRepository) {
    fun updateTask(task : Task) = taskRepository.updateTask(task)
}