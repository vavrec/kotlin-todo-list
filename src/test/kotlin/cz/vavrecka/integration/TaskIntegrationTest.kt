package cz.vavrecka.integration

import cz.vavrecka.TestTags
import cz.vavrecka.todolist.ToDoListApplication
import cz.vavrecka.todolist.controller.ListController
import cz.vavrecka.todolist.controller.TaskController
import cz.vavrecka.todolist.controller.UserController
import cz.vavrecka.todolist.domain.List
import cz.vavrecka.todolist.domain.Task
import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.model.NewList
import cz.vavrecka.todolist.model.NewTask
import cz.vavrecka.todolist.model.NewUser
import cz.vavrecka.todolist.repository.ListRepository
import cz.vavrecka.todolist.repository.TaskRepository
import cz.vavrecka.todolist.repository.UserListCrossReferenceRepository
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
class TaskIntegrationTest {

    @Autowired
    lateinit var listRepository: ListRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var crossReferenceRepository: UserListCrossReferenceRepository

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @BeforeEach
    @AfterEach
    fun doEveryTime() {
        taskRepository.deleteAll()
        crossReferenceRepository.deleteAll()
        userRepository.deleteAll()
        listRepository.deleteAll()
    }


    @Test
    fun `create task`() {
        val newUser = NewUser("test", "hello@hello.com")

        val user = testRestTemplate.postForEntity(UserController.PATH, newUser, User::class.java).body!!

        val newList = NewList("test-list", user.id)
        val listResponse = testRestTemplate.postForEntity(ListController.PATH, newList, List::class.java)

        val newTask = NewTask("test-task", listResponse.body!!.id)
        val taskResponse = testRestTemplate.postForEntity(TaskController.PATH, newTask, Task::class.java)

        //assertions
        assertThat(listResponse)
            .matches { it.statusCode == HttpStatus.CREATED }
            .matches { it.body!!.name == newList.name }

        assertThat(listRepository.count()).isOne()
        assertThat(listRepository.findAll().first())
            .matches { it.id == listResponse.body!!.id }
            .matches { it.name == listResponse.body!!.name }

        assertThat(crossReferenceRepository.count()).isOne()
        assertThat(crossReferenceRepository.findAll().first())
            .matches { it.userId == user.id }
            .matches { it.listId == listResponse.body!!.id }

        assertThat(taskRepository.count()).isOne()
        assertThat(taskRepository.findAll().first())
            .matches { it.id == taskResponse.body!!.id }
            .matches { it.name == taskResponse.body!!.name }
            .matches { it.listId == taskResponse.body!!.listId }
    }
}