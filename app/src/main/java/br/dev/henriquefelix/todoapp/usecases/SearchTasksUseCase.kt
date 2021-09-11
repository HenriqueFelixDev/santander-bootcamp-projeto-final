package br.dev.henriquefelix.todoapp.usecases

import br.dev.henriquefelix.todoapp.data.repositories.ITaskRepository

class SearchTasksUseCase(private val taskRepository : ITaskRepository) {
    fun searchTasks(search : String) = taskRepository.searchTasks(search)
}