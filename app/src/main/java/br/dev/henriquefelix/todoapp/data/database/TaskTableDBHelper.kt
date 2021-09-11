package br.dev.henriquefelix.todoapp.data.database

class TaskTableDBHelper {
    companion object {
        const val TASK_TABLE_NAME = "tasks"
        const val TASK_COLUMN_ID = "id"
        const val TASK_COLUMN_TASK = "task"
        const val TASK_COLUMN_START = "start"
        const val TASK_COLUMN_END = "end"
        const val TASK_COLUMN_IS_COMPLETED = "is_completed"

        const val TASK_TABLE_CREATE_SQL = """
            CREATE TABLE IF NOT EXISTS $TASK_TABLE_NAME (
                $TASK_COLUMN_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                $TASK_COLUMN_TASK TEXT NOT NULL,
                $TASK_COLUMN_START NUMERIC,
                $TASK_COLUMN_END NUMERIC,
                $TASK_COLUMN_IS_COMPLETED NUMERIC NOT NULL
            )
        """
    }
}