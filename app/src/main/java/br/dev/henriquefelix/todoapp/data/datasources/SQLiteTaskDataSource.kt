package br.dev.henriquefelix.todoapp.data.datasources

import android.annotation.SuppressLint
import android.content.ContentValues
import br.dev.henriquefelix.todoapp.data.database.TaskTableDBHelper
import br.dev.henriquefelix.todoapp.data.database.ToDoDBHelper
import br.dev.henriquefelix.todoapp.domain.models.Task
import java.util.*

class SQLiteTaskDataSource(private val todoDbHelper : ToDoDBHelper) : ITaskDataSource {
    override fun createTask(task: Task): Long {
        val db = todoDbHelper.writableDatabase

        val id = db.insert(
            TaskTableDBHelper.TASK_TABLE_NAME,
            null,
            taskToContentValues(task)
        )

        db.close()

        return id
    }

    @SuppressLint("Range")
    override fun searchTasks(search: String): List<Task> {
        val db = todoDbHelper.readableDatabase

        val sql ="""
            SELECT * FROM ${TaskTableDBHelper.TASK_TABLE_NAME} WHERE
                ${TaskTableDBHelper.TASK_COLUMN_TASK} LIKE ?
        """.trimIndent()

        val cursor = db.rawQuery(sql, arrayOf("%$search%"))
        val taskList = arrayListOf<Task>()

        while(cursor.moveToNext()) {

            val id = cursor.getLong(cursor.getColumnIndex(TaskTableDBHelper.TASK_COLUMN_ID))
            val task = cursor.getString(cursor.getColumnIndex(TaskTableDBHelper.TASK_COLUMN_TASK))
            val startInMillis = cursor.getLong(cursor.getColumnIndex(TaskTableDBHelper.TASK_COLUMN_START))
            val start = when(startInMillis) {
                null, 0L -> null
                else -> Date(startInMillis)
            }

            val endInMillis = cursor.getLong(cursor.getColumnIndex(TaskTableDBHelper.TASK_COLUMN_END))
            val end = when(endInMillis) {
                null, 0L -> null
                else -> Date(endInMillis)
            }

            val isCompletedNumeric = cursor.getInt(cursor.getColumnIndex(TaskTableDBHelper.TASK_COLUMN_IS_COMPLETED))
            val isCompleted = when(isCompletedNumeric) {
                0 -> false
                else -> true
            }

            val taskObject = Task(id, task, start, end, isCompleted)

            taskList.add(taskObject)
        }

        db.close()

        return taskList
    }

    override fun updateTask(task: Task) {
        val db = todoDbHelper.writableDatabase

        db.update(
            TaskTableDBHelper.TASK_TABLE_NAME,
            taskToContentValues(task),
            "${TaskTableDBHelper.TASK_COLUMN_ID} = ?",
            arrayOf(task.id.toString())
        )

        db.close()
    }

    override fun deleteTask(task: Task) {
        val db = todoDbHelper.writableDatabase

        db.delete(
            TaskTableDBHelper.TASK_TABLE_NAME,
            "${TaskTableDBHelper.TASK_COLUMN_ID} = ?",
            arrayOf(task.id.toString())
        )

        db.close()
    }

    private fun taskToContentValues(task : Task) : ContentValues {
        val values = ContentValues()

        if (task.id > 0) {
            values.put(TaskTableDBHelper.TASK_COLUMN_ID, task.id)
        }

        values.put(TaskTableDBHelper.TASK_COLUMN_TASK, task.task)
        values.put(TaskTableDBHelper.TASK_COLUMN_START, task.start?.time)
        values.put(TaskTableDBHelper.TASK_COLUMN_END, task.end?.time)
        values.put(TaskTableDBHelper.TASK_COLUMN_IS_COMPLETED, task.isCompleted)

        return values
    }
}