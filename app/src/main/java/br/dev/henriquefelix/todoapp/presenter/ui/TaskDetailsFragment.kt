package br.dev.henriquefelix.todoapp.presenter.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import br.dev.henriquefelix.todoapp.R
import br.dev.henriquefelix.todoapp.application.ToDoApplication
import br.dev.henriquefelix.todoapp.databinding.ActivityTaskDetailsBinding
import br.dev.henriquefelix.todoapp.domain.extensions.format
import br.dev.henriquefelix.todoapp.domain.extensions.toDate
import br.dev.henriquefelix.todoapp.domain.models.Task
import br.dev.henriquefelix.todoapp.presenter.events.TaskDetailsFragmentEvents
import br.dev.henriquefelix.todoapp.usecases.CreateTaskUseCase
import br.dev.henriquefelix.todoapp.usecases.UpdateTaskUseCase
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.absoluteValue

enum class DetailsType {
    CREATE, EDIT
}

class TaskDetailsFragment(private val taskDetailsFragmentEvents: TaskDetailsFragmentEvents) : DialogFragment(), DialogInterface.OnClickListener {
    private lateinit var activityTaskDetails : ActivityTaskDetailsBinding
    private var detailsType = DetailsType.CREATE
    private var task : Task? = null
    private val taskRepository = ToDoApplication.instance.taskRepository
    private val createTaskUseCase = CreateTaskUseCase(taskRepository)
    private val updateTaskUseCase = UpdateTaskUseCase(taskRepository)

    companion object {
        const val TASK_DETAILS_TAG = "task_details"
        const val DATE_PICKER_TAG = "datepicker"
        const val EXTRA_TASK = "extra_task"

        fun newInstance(task : Task?, taskDetailsFragmentEvents: TaskDetailsFragmentEvents) : TaskDetailsFragment {
            val taskDetailsFragment = TaskDetailsFragment(taskDetailsFragmentEvents)

            if (task != null) {
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_TASK, task)
                taskDetailsFragment.arguments = bundle
            }

            return taskDetailsFragment
        }
    }

    private val title : String
    get() = when(detailsType) {
        DetailsType.CREATE -> getString(R.string.create_task_title)
        else -> getString(R.string.edit_task_title)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activityTaskDetails = ActivityTaskDetailsBinding.inflate(layoutInflater)

        if (arguments != null && arguments?.getParcelable<Task>(EXTRA_TASK) != null) {
            detailsType = DetailsType.EDIT
            task = arguments?.getParcelable<Task>(EXTRA_TASK)
        }

        setupComponents()
        initializeListeners()

        return AlertDialog.Builder(activity)
            .setView(activityTaskDetails.root)
            .setTitle(title)
            .setPositiveButton(getString(R.string.save_button) ?: "Save", this)
            .setNegativeButton(getString(R.string.cancel_button) ?: "Cancel", this)
            .create()
    }

    private fun setupComponents() {
        activityTaskDetails.textTask.editText?.setText(task?.task ?: "")
        activityTaskDetails.textStart.editText?.setText(task?.start?.format() ?: "")
        activityTaskDetails.textEnd.editText?.setText(task?.end?.format() ?: "")
    }

    private fun initializeListeners() {
        activityTaskDetails.textStart.editText?.setOnClickListener {
            showDateTimePickerDialog { date ->
                activityTaskDetails.textStart.editText?.setText(date)
            }
        }

        activityTaskDetails.textEnd.editText?.setOnClickListener {
            showDateTimePickerDialog { date ->
                activityTaskDetails.textEnd.editText?.setText(date)
            }
        }
    }

    override fun onClick(dialogInterface: DialogInterface?, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            task = Task(
                task?.id ?: 0,
                activityTaskDetails.textTask.editText?.text?.toString() ?: "",
                activityTaskDetails.textStart.editText?.text?.toString()?.toDate(),
                activityTaskDetails.textEnd.editText?.text?.toString()?.toDate(),
                false
            )
            createOrUpdateTask(task!!)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        taskDetailsFragmentEvents.onCloseFragment()
    }

    private fun createOrUpdateTask(task : Task) {
        if (detailsType == DetailsType.EDIT) {
            updateTaskUseCase.updateTask(task)
            return
        }

        createTaskUseCase.create(task)
    }

    private fun showDateTimePickerDialog(onDateTimeSelected : (date : String) -> Unit) {
        showDatePickerDialog { timeInMillis ->
            val timezone = TimeZone.getDefault()
            val offset = timezone.getOffset(Date().time) * -1
            val date = Date(timeInMillis + abs(offset))
            showTimePickerDialog { timePicker ->
                val hour = timePicker.hour.toString().padStart(2, '0')
                val minute = timePicker.minute.toString().padStart(2, '0')

                val datetime = "${date.format("dd/MM/yyyy")} $hour:$minute"
                onDateTimeSelected(datetime)
            }
        }
    }

    private fun showDatePickerDialog(onPositiveButtonClickListener: MaterialPickerOnPositiveButtonClickListener<Long>) {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.addOnPositiveButtonClickListener(onPositiveButtonClickListener)
        datePicker.show(requireActivity().supportFragmentManager, DATE_PICKER_TAG)
    }

    private fun showTimePickerDialog(onPositiveButtonClickListener: (MaterialTimePicker) -> Unit) {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build();

        timePicker.addOnPositiveButtonClickListener {
            onPositiveButtonClickListener(timePicker)
        }
        timePicker.show(requireActivity().supportFragmentManager, DATE_PICKER_TAG)
    }
}