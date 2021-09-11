package br.dev.henriquefelix.todoapp.presenter.adapters

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.dev.henriquefelix.todoapp.R
import br.dev.henriquefelix.todoapp.databinding.TaskListItemBinding
import br.dev.henriquefelix.todoapp.domain.extensions.format
import br.dev.henriquefelix.todoapp.domain.models.Task
import br.dev.henriquefelix.todoapp.presenter.events.TaskListItemEventListener
import kotlin.reflect.typeOf

class TaskAdapter(private val taskListItemEventListener : TaskListItemEventListener) : RecyclerView.Adapter<TaskViewHolder>() {
    private val tasksList = mutableListOf<Task>()
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = TaskListItemBinding.inflate(inflater, parent, false)
        return TaskViewHolder(view, taskListItemEventListener)
    }

    override fun onBindViewHolder(holder : TaskViewHolder, position : Int) {
        holder.bind(tasksList[position])
    }

    override fun getItemCount() : Int = tasksList.size

    fun updateTaskList(list : List<Task>) {
        tasksList.clear()
        tasksList.addAll(list)
        notifyDataSetChanged()
    }
}

class TaskViewHolder(
    private val taskListItemBinding : TaskListItemBinding,
    private val taskListItemEventListener : TaskListItemEventListener
) : RecyclerView.ViewHolder(taskListItemBinding.root) {

    fun bind(task : Task) {
        taskListItemBinding.textTask.text = task.task

        val textDuration =
            if(task.start == null && task.end == null) {
                getString(R.string.task_no_duration)
            } else {
                "${task.start?.format() ?: getString(R.string.task_no_start)} - " +
                "${task.end?.format() ?: getString(R.string.task_no_end)}"
            }

        taskListItemBinding.textDuration.text = textDuration

        taskListItemBinding.switchStatus.isChecked = task.isCompleted
        changeTaskStatusIcon(task.isCompleted)

        taskListItemBinding.root.setOnClickListener {
            taskListItemEventListener.onItemClick(task)
        }

        taskListItemBinding.switchStatus.setOnCheckedChangeListener { _, status ->
            changeTaskStatusIcon(status)
            taskListItemEventListener.onStatusChange(task, status)
        }

        taskListItemBinding.btnDelete.setOnClickListener {
            taskListItemEventListener.onDeleteClick(task)
        }
    }

    private fun getString(id : Int) : String {
        return taskListItemBinding.root.resources.getString(id)
    }

    private fun changeTaskStatusIcon(status : Boolean) {
        val successIcon = taskListItemBinding.root.context.resources.getDrawable(R.drawable.ic_baseline_check_24)
        val pendingIcon = taskListItemBinding.root.context.resources.getDrawable(R.drawable.ic_baseline_access_time_24)
        val icon = when(status) {
            true -> successIcon
            else -> pendingIcon
        }

        val successBackground = taskListItemBinding.root.context.resources.getDrawable(R.drawable.circle_avatar)
        val pendingBackground = taskListItemBinding.root.context.resources.getDrawable(R.drawable.circle_avatar_pending)
        val background = when(status) {
            true -> successBackground
            else -> pendingBackground
        }

        taskListItemBinding.imageStatus.setImageDrawable(icon)
        taskListItemBinding.imageStatus.background = background
    }
}