package cz.vavrecka.todolist.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.exception.UserNotFound
import cz.vavrecka.todolist.model.NewUser
import cz.vavrecka.todolist.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.ProblemDetail
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE as JSON


@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var userService: UserService;

    @ParameterizedTest
    @CsvSource(
        delimiter = '|', ignoreLeadingAndTrailingWhitespace = false, nullValues = ["null"], textBlock = """
                |   test@test.cz 
        null    |   test@test.cz
        name    |   hello       
        name    |                """
    )
    fun `crete new user - invalid input data`(name: String, email: String) {

        val newUser = objectMapper.writeValueAsString(
            mapOf(
                "name" to name,
                "email" to email,
            )
        )

        this.mockMvc.perform(
            post(UserController.PATH)
                .accept(JSON)
                .contentType(JSON)
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
    fun `create new user - missing input data, returns 400`() {
        this.mockMvc.perform(
            post(UserController.PATH)
                .accept(JSON)
                .contentType(JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect {
                val response = objectMapper.readValue<ProblemDetail>(it.response.contentAsString)
                assertThat(response).isExactlyInstanceOf(ProblemDetail::class.java)
                assertThat(response.detail).isEqualTo("Failed to read request");
            }

        verify(userService, never()).create(any())
    }

    @Test
    fun `create new user - returns 200`() {
        val uuid = UUID.fromString("dd8aac75-10b5-4f92-ba2e-4a7bddc003ce")
        val name = "test"
        val email = "test@test.cz"
        val newUser = NewUser(name, email)
        val expectedUser = User(uuid, name, email)

        whenever(userService.create(eq(newUser))).thenReturn(expectedUser)

        this.mockMvc.perform(
            post(UserController.PATH)
                .accept(JSON)
                .contentType(JSON)
                .content(objectMapper.writeValueAsString(newUser))
        )
            .andExpect(status().isOk)
            .andExpect {
                val response = objectMapper.readValue<User>(it.response.contentAsString)
                assertThat(response).isEqualTo(expectedUser)
            }
    }

    @Test
    fun `get user by ID - user not found - returns 400 and error message`() {
        val uuid = UUID.fromString("dd8aac75-10b5-4f92-ba2e-4a7bddc003ce")

        whenever(userService.findById(eq(uuid))).thenAnswer {
            throw UserNotFound("User: $uuid not found")
        }

        this.mockMvc.perform(get("${UserController.PATH}/$uuid").accept(JSON).contentType(JSON))
            .andExpect(status().isBadRequest)
            .andExpect {
                val response = objectMapper.readValue<ProblemDetail>(it.response.contentAsString)
                assertThat(response).isExactlyInstanceOf(ProblemDetail::class.java)
                assertThat(response.detail).isEqualTo("Invalid data")
            }
    }

    @Test
    fun `get user by ID  - when valid input then return 200 and user`() {
        val uuid = UUID.fromString("dd8aac75-10b5-4f92-ba2e-4a7bddc003ce")
        val expectedUser = User(uuid, "test", "test", false)

        whenever(userService.findById(eq(uuid))).thenReturn(expectedUser)

        this.mockMvc.perform(
            get("${UserController.PATH}/$uuid").accept(JSON).contentType(JSON)
        )
            .andExpect(status().isOk)
            .andExpect {
                val expectedUserAsString = objectMapper.writeValueAsString(expectedUser)
                content().json(expectedUserAsString, true)
            }
    }
}