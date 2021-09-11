package br.dev.henriquefelix.todoapp.presenter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.dev.henriquefelix.todoapp.application.ToDoApplication
import br.dev.henriquefelix.todoapp.domain.models.Task
import br.dev.henriquefelix.todoapp.usecases.DeleteTaskUseCase
import br.dev.henriquefelix.todoapp.usecases.SearchTasksUseCase
import br.dev.henriquefelix.todoapp.usecases.ToggleTaskStatusUseCase
import br.dev.henriquefelix.todoapp.usecases.UpdateTaskUseCase

class ToDoViewModel : ViewModel() {

    private val taskRepository = ToDoApplication.instance.taskRepository
    private val searchTaskUseCase = SearchTasksUseCase(taskRepository)
    private val deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
    private val toggleTaskStatusUseCase = ToggleTaskStatusUseCase(taskRepository)

    val search = MutableLiveData<String>()

    private val mutableTaskList = MutableLiveData<List<Task>>()
    val taskList : LiveData<List<Task>>
    get() = mutableTaskList

    fun getTasks() {
        val thread = Thread {
            val list = searchTaskUseCase.searchTasks(search.value ?: "")
            mutableTaskList.postValue(list)
        }.start()
    }

    fun deleteTask(task: Task) {
        deleteTaskUseCase.deleteTask(task)
    }

    fun updateTaskStatus(task : Task, newStatus : Boolean) {
        toggleTaskStatusUseCase.changeTaskStatus(task.copy(isCompleted = newStatus))
    }

}