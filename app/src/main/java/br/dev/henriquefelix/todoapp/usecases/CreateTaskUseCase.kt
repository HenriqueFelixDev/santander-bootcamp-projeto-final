package br.dev.henriquefelix.todoapp.usecases

import br.dev.henriquefelix.todoapp.data.repositories.ITaskRepository
import br.dev.henriquefelix.todoapp.domain.models.Task

class CreateTaskUseCase(private val taskRepository : ITaskRepository) {
    fun create(task : Task) : Long = taskRepository.createTask(task)
}