package cz.vavrecka.todolist.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import cz.vavrecka.TestTags
import cz.vavrecka.todolist.domain.List
import cz.vavrecka.todolist.exception.NotFoundException
import cz.vavrecka.todolist.model.NewList
import cz.vavrecka.todolist.service.ListService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ProblemDetail
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@Tag(TestTags.COMPONENT_TEST)
@WebMvcTest(ListController::class)
class ListControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var listService: ListService

    @Test
    fun `crete new user - invalid input data`() {
        val newUser = objectMapper.writeValueAsString(
            mapOf(
                "name" to "",
                "userId" to UUID.fromString("dd8aac75-10b5-4f92-ba2e-4a7bddc003ce"),
            )
        )

        this.mockMvc.perform(
            post(ListController.PATH)
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(newUser)
        )
            .andExpect(status().isBadRequest)
            .andExpect {
                val response = objectMapper.readValue<ProblemDetail>(it.response.contentAsString)
                assertThat(response).isExactlyInstanceOf(ProblemDetail::class.java)
                assertThat(response.detail).isEqualTo("Invalid request content.");
            }
    }


    @Test
    fun `create new list - missing input data, returns 400`() {
        this.mockMvc.perform(
            post(ListController.PATH)
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
        )
            .andExpect(status().isBadRequest)
            .andExpect {
                val response = objectMapper.readValue<ProblemDetail>(it.response.contentAsString)
                assertThat(response).isExactlyInstanceOf(ProblemDetail::class.java)
                assertThat(response.detail).isEqualTo("Failed to read request");
            }

        verify(listService, never()).createList(any())
    }

    @Test
    fun `create list - user not found - returns 400 and error message`() {
        val uuid = UUID.fromString("dd8aac75-10b5-4f92-ba2e-4a7bddc003ce")
        val name = "test"
        val newList = NewList(name, uuid)

        whenever(listService.createList(eq(newList))).thenAnswer {
            throw NotFoundException("User: $uuid not found")
        }

        this.mockMvc.perform(
            post(ListController.PATH).accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newList))
        )
            .andExpect(status().isBadRequest)
            .andExpect {
                val response = objectMapper.readValue<ProblemDetail>(it.response.contentAsString)
                assertThat(response).isExactlyInstanceOf(ProblemDetail::class.java)
                assertThat(response.detail).isEqualTo("Invalid data")
            }
    }

    @Test
    fun `create new list - returns 200`() {
        val uuid = UUID.fromString("dd8aac75-10b5-4f92-ba2e-4a7bddc003ce")
        val name = "test"
        val newList = NewList(name, uuid)
        val expectedList = List(uuid, name)

        whenever(listService.createList(eq(newList))).thenReturn(expectedList)

        this.mockMvc.perform(
            post(ListController.PATH)
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newList))
        )
            .andExpect(status().isCreated)
            .andExpect {
                val response = objectMapper.readValue<List>(it.response.contentAsString)
                assertThat(response).isEqualTo(expectedList)
            }
    }
}