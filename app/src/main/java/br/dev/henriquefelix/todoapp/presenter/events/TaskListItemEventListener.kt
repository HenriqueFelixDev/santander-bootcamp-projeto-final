package br.dev.henriquefelix.todoapp.presenter.events

import br.dev.henriquefelix.todoapp.domain.models.Task

interface TaskListItemEventListener {
    fun onItemClick(task : Task)
    fun onStatusChange(task : Task, status : Boolean)
    fun onDeleteClick(task : Task)
}