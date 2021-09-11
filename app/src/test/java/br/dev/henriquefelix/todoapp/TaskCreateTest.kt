package br.dev.henriquefelix.todoapp

import br.dev.henriquefelix.todoapp.data.repositories.ITaskRepository
import br.dev.henriquefelix.todoapp.domain.models.Task
import br.dev.henriquefelix.todoapp.usecases.CreateTaskUseCase
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class TaskCreateTest {
    private val taskRepository : ITaskRepository
    private val createUseCase : CreateTaskUseCase

    init {
        taskRepository = object : ITaskRepository {
            private val tasks = mutableListOf<Task>()

            override fun createTask(task: Task): Long {
                val newId = tasks.lastOrNull()?.id ?: 1L
                tasks.add(task.copy(id = newId))
                return newId
            }

            override fun searchTasks(search: String): List<Task> {
                return tasks
            }

            override fun updateTask(task: Task) {
                val filteredTask = findById(task)
                if (filteredTask != null) {
                    tasks.set(tasks.indexOf(filteredTask), task)
                }
            }

            override fun deleteTask(task: Task) {
                val filteredTask = findById(task)
                if (filteredTask != null) {
                    tasks.removeAt(tasks.indexOf(filteredTask))
                }
            }

            private fun findById(task : Task) : Task? {
                return tasks.firstOrNull{  it.id == task.id }
            }

        }

        createUseCase = CreateTaskUseCase(taskRepository)
    }
    @Test
    fun `Cria uma tarefa e a adiciona a uma lista vazia, ficando com tamanho 1`() {
        // Arrange
        val task = Task(
            id = 0,
            task = "Learn Kotlin",
            start = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2020-10-01T22:15:00"),
            end = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2020-10-01T23:00:00"),
            isCompleted = false
        )

        // Act
        createUseCase.create(task)
        val tasksResult = taskRepository.searchTasks("")

        // Assert
        assertEquals(tasksResult.size, 1)
        assertNotNull(tasksResult[0])
        assertEquals(tasksResult[0].id, 1)
    }
}