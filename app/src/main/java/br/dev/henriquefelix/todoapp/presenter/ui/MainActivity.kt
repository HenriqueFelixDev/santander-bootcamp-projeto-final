package br.dev.henriquefelix.todoapp.presenter.ui

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import br.dev.henriquefelix.todoapp.R
import br.dev.henriquefelix.todoapp.databinding.ActivityMainBinding
import br.dev.henriquefelix.todoapp.domain.models.Task
import br.dev.henriquefelix.todoapp.presenter.adapters.TaskAdapter
import br.dev.henriquefelix.todoapp.presenter.events.TaskDetailsFragmentEvents
import br.dev.henriquefelix.todoapp.presenter.events.TaskListItemEventListener
import br.dev.henriquefelix.todoapp.presenter.viewmodels.ToDoViewModel
import java.util.*

class MainActivity : AppCompatActivity(), TaskListItemEventListener, TaskDetailsFragmentEvents{
    private lateinit var activityMainBinding : ActivityMainBinding
    private lateinit var taskAdapter : TaskAdapter
    private lateinit var todoViewModel : ToDoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setupComponents()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        todoViewModel.getTasks()
    }

    override fun onResume() {
        super.onResume()
        activityMainBinding.textSearch.clearFocus()
        activityMainBinding.recyclerTasks.requestFocus()
    }

    private fun setupComponents() {
        todoViewModel = ViewModelProvider.NewInstanceFactory().create(ToDoViewModel::class.java)

        taskAdapter = TaskAdapter(this)
        activityMainBinding.recyclerTasks.adapter = taskAdapter
    }

    private fun initListeners() {
        activityMainBinding.fabAddTask.setOnClickListener { onAddTaskClick() }

        activityMainBinding.textSearch.editText?.doOnTextChanged { text, _, _, _ ->
            todoViewModel.search.value = text.toString()
        }

        activityMainBinding.textSearch.setEndIconOnClickListener {
            todoViewModel.getTasks()
        }

        todoViewModel.taskList.observe ( this, { taskList ->
            activityMainBinding.includeEmptyLayout.root.visibility =
                if (taskList.isEmpty()) View.VISIBLE
                else View.GONE

            taskAdapter.updateTaskList(taskList)
        })
    }

    private fun onAddTaskClick() = openTaskDetails(null)

    override fun onItemClick(task: Task) = openTaskDetails(task)

    private fun openTaskDetails(task : Task?) {
        val taskDetailsFragment = TaskDetailsFragment.newInstance(task, this)
        taskDetailsFragment.show(supportFragmentManager, TaskDetailsFragment.TASK_DETAILS_TAG)
    }

    override fun onStatusChange(task: Task, status: Boolean) {
        todoViewModel.updateTaskStatus(task, status)
    }

    override fun onDeleteClick(task : Task) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_delete_title))
            .setMessage(getString(R.string.confirm_delete_message).replace("{0}", task.task))
            .setPositiveButton(getString(R.string.yes_button), DialogInterface.OnClickListener { _, _ ->
                todoViewModel.deleteTask(task)
                todoViewModel.getTasks()
            })
            .setNegativeButton(getString(R.string.cancel_button), null)
            .create()

        dialog.show()
    }

    override fun onCloseFragment() {
        todoViewModel.getTasks()
    }
}