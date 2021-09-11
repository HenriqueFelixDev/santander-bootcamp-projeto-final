package br.dev.henriquefelix.todoapp.application

import android.app.Application
import br.dev.henriquefelix.todoapp.data.database.ToDoDBHelper
import br.dev.henriquefelix.todoapp.data.datasources.ITaskDataSource
import br.dev.henriquefelix.todoapp.data.datasources.SQLiteTaskDataSource
import br.dev.henriquefelix.todoapp.data.repositories.ITaskRepository
import br.dev.henriquefelix.todoapp.data.repositories.TaskRepositoryImplementation

class ToDoApplication : Application() {

    private lateinit var toDoDBHelper : ToDoDBHelper
    private lateinit var taskDataSource : ITaskDataSource
    lateinit var taskRepository : ITaskRepository

    companion object {
        lateinit var instance : ToDoApplication
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        toDoDBHelper = ToDoDBHelper(this)
        taskDataSource = SQLiteTaskDataSource(toDoDBHelper)
        taskRepository = TaskRepositoryImplementation(taskDataSource)
    }
}
