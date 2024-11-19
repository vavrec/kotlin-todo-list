package cz.vavrecka.todolist.service.impl

import cz.vavrecka.TestTags
import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.exception.NotFound
import cz.vavrecka.todolist.model.NewUser
import cz.vavrecka.todolist.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import java.util.*

@Tag(TestTags.UNIT_TEST)
class UserServiceImplTest {

    val userRepository = mock<UserRepository>()

    val userService = UserServiceImpl(userRepository)

    @Test
    fun `find user by id - user does not exist`() {
        doAnswer { Optional.ofNullable(null) }.whenever(userRepository).findById(any())

        assertThatThrownBy { userService.findById(UUID.randomUUID()) }
            .isExactlyInstanceOf(NotFound::class.java)
            .hasMessageStartingWith("User")
            .hasMessageEndingWith("not found")
    }

    @Test
    fun `create user - newUser toUser mapping test`() {

        val newUser = NewUser("newUser", "newUser@email.com")
        val uuid = UUID.fromString("ee115ae7-2ce9-4309-86e3-c9b7aac96560")
        val expectedUser = User(uuid, "newUser", "newUser@email.com", true)

        Mockito.mockStatic(UUID::class.java).use { mockedUUID ->
            mockedUUID.`when`<Any> { UUID.randomUUID() }.thenReturn(uuid)
            doAnswer { expectedUser }.whenever(userRepository).save(eq(expectedUser))
            assertThat(userService.create(newUser)).isEqualTo(expectedUser)
        }
    }

}