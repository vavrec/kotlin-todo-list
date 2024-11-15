package cz.vavrecka.integration

import cz.vavrecka.TestTags
import cz.vavrecka.todolist.ToDoListApplication
import cz.vavrecka.todolist.controller.UserController
import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.model.NewUser
import cz.vavrecka.todolist.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

@Tag(TestTags.INTEGRATION_TEST)
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(
    classes = [TestcontainersConfiguration::class, ToDoListApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class UserIntegrationTest {

    val name = "test"

    val email = "test@test.cz"

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @BeforeEach
    @AfterEach
    fun doEveryTime() {
        userRepository.deleteAll()
    }

    @Test
    fun `find user by id`(){
        val newUser = NewUser(name, email)

        val createdUser = testRestTemplate.postForEntity(UserController.PATH, newUser, User::class.java).body!!

        val userResponse = testRestTemplate.getForEntity("${UserController.PATH}/${createdUser.id}", User::class.java)

        //assertions
        assertThat(userResponse)
            .matches { userResponse.statusCode == HttpStatus.OK }
            .extracting { userResponse.body!! }
            .isEqualTo(createdUser)
    }

    @Test
    fun `create new user`() {
        val newUser = NewUser(name, email)

        val response = testRestTemplate.postForEntity(UserController.PATH, newUser, User::class.java)

        // assertions
        assertThat(response)
            .matches { it.statusCode == HttpStatus.CREATED }
            .isNotNull
            .extracting { response.body!! }
            .matches { it.name == name && it.email == email }


        val dbUser = userRepository.findById(response.body!!.id).get()
        assertThat(dbUser).isNotNull
            .matches { it.name == name }
            .matches { it.email == email }

        val count = userRepository.count()
        assertThat(count).isOne()
    }
}