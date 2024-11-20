package cz.vavrecka.todolist.service.impl

import cz.vavrecka.TestTags
import cz.vavrecka.todolist.domain.List
import cz.vavrecka.todolist.domain.Task
import cz.vavrecka.todolist.model.NewTask
import cz.vavrecka.todolist.repository.TaskRepository
import cz.vavrecka.todolist.service.ListService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.*
import java.util.*

@Tag(TestTags.UNIT_TEST)
class TaskServiceImplTest {

    val listServiceMock = mock<ListService>()

    val taskRepositoryMock = mock<TaskRepository>()

    val taskService = TaskServiceImpl(listServiceMock, taskRepositoryMock)

    @Test
    fun `create task - new task to task mapping test`() {
        val listId = UUID.randomUUID()
        val taskId = UUID.randomUUID()
        val newTask = NewTask("test", listId)
        val task = Task(taskId, "test", listId, true)

        doAnswer { List(listId, "name") }.whenever(listServiceMock).findById(eq(listId))
        doAnswer { task }.whenever(taskRepositoryMock).save(eq(task))

        Mockito.mockStatic(UUID::class.java).use { mockedUUID ->
            mockedUUID.`when`<Any> { UUID.randomUUID() }.thenReturn(taskId)
            assertThat(taskService.createTask(newTask)).isEqualTo(task)
        }
    }

    @Test
    fun `create task - verify calls`() {
        val listId = UUID.randomUUID()
        val newTask = NewTask("test", listId)

        doAnswer { List(listId, "name") }.whenever(listServiceMock).findById(eq(listId))
        doAnswer { Task(UUID.randomUUID(), "name", listId) }.whenever(taskRepositoryMock).save(any())

        taskService.createTask(newTask)

        verify(listServiceMock).findById(eq(listId))
        verify(taskRepositoryMock).save(any())
    }
}