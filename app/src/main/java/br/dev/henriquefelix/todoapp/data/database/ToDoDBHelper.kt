package br.dev.henriquefelix.todoapp.data.database

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ToDoDBHelper(
    context: Context?
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION, null) {

    companion object {
        const val DB_NAME = "todoapp_db"
        const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(TaskTableDBHelper.TASK_TABLE_CREATE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, newerVersion: Int, olderVersion: Int) {

    }
}