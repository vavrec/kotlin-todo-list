package cz.vavrecka.todolist.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import cz.vavrecka.TestTags
import cz.vavrecka.todolist.domain.Task
import cz.vavrecka.todolist.exception.NotFound
import cz.vavrecka.todolist.model.NewTask
import cz.vavrecka.todolist.service.TaskService
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ProblemDetail
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID


@Tag(TestTags.COMPONENT_TEST)
@WebMvcTest(TaskController::class)
class TaskControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var taskService: TaskService


    @Test
    fun `create task - new task name validation`() {
        val newTask = objectMapper.writeValueAsString(
            mapOf(
                "name" to "",
                "listId" to UUID.fromString("dd8aac75-10b5-4f92-ba2e-4a7bddc003ce"),
            )
        )

        this.mockMvc.perform(
            post(TaskController.PATH)
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(newTask)
        )
            .andExpect(status().isBadRequest)
            .andExpect {
                val response = objectMapper.readValue<ProblemDetail>(it.response.contentAsString)
                assertThat(response).isExactlyInstanceOf(ProblemDetail::class.java)
                assertThat(response.detail).isEqualTo("Invalid request content.");
            }
    }

    @Test
    fun `create task - list does not exists, 400`() {
        val taskName = "test"
        val listId = UUID.randomUUID()
        val newTask = NewTask(taskName, listId)

        whenever(taskService.createTask(eq(newTask))).thenAnswer {
            throw NotFound("List: $listId not found")
        }

        this.mockMvc.perform(
            post(TaskController.PATH)
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newTask))
        ).andExpect(status().isBadRequest)
            .andExpect {
                val response = objectMapper.readValue<ProblemDetail>(it.response.contentAsString)
                assertThat(response).isExactlyInstanceOf(ProblemDetail::class.java)
                assertThat(response.detail).isEqualTo("Invalid data")
            }
    }

    @Test
    fun `create task - 200`() {
        val taskName = "test"
        val listId = UUID.randomUUID()
        val newTask = NewTask(taskName, listId)
        val expectedTask = Task(UUID.randomUUID(), taskName, UUID.randomUUID())

        whenever(taskService.createTask(eq(newTask))).thenReturn(expectedTask)

        this.mockMvc.perform(
            post(TaskController.PATH)
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newTask))
        ).andExpect(status().isCreated)
            .andExpect {
                val response = objectMapper.readValue<Task>(it.response.contentAsString)
                assertThat(response).isEqualTo(expectedTask)
            }

    }
}