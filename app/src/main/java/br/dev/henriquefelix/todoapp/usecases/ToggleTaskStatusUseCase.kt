package br.dev.henriquefelix.todoapp.usecases

import br.dev.henriquefelix.todoapp.data.repositories.ITaskRepository
import br.dev.henriquefelix.todoapp.domain.models.Task

class ToggleTaskStatusUseCase(private val taskRepository : ITaskRepository) {
    fun changeTaskStatus(task: Task) = taskRepository.updateTask(task)
}