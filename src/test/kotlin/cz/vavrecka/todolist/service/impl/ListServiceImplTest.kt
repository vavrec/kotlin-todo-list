package cz.vavrecka.todolist.service.impl

import cz.vavrecka.TestTags
import cz.vavrecka.todolist.domain.List
import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.exception.NotFoundException
import cz.vavrecka.todolist.model.NewList
import cz.vavrecka.todolist.repository.ListRepository
import cz.vavrecka.todolist.service.ListService
import cz.vavrecka.todolist.service.UserListCrossReferenceService
import cz.vavrecka.todolist.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.*
import java.util.*

@Tag(TestTags.UNIT_TEST)
class ListServiceImplTest {

    val userServiceMock = mock<UserService>()

    val userListCrossReferenceServiceMock = mock<UserListCrossReferenceService>()

    val listRepositoryMock = mock<ListRepository>()

    val listservice: ListService = ListServiceImpl(
        listRepositoryMock, userServiceMock,
        userListCrossReferenceServiceMock
    )

    @Test
    fun `find by id - list does not exist`() {
        doAnswer { Optional.ofNullable(null) }.whenever(listRepositoryMock).findById(any())

        assertThatThrownBy { listservice.findById(UUID.randomUUID()) }
            .isExactlyInstanceOf(NotFoundException::class.java)
            .hasMessageStartingWith("List")
            .hasMessageEndingWith("not found")
    }

    @Test
    fun `find by id`(){
        val listId = UUID.randomUUID()
        val list = List(listId, "test", false )

        doAnswer { Optional.of(list) }.whenever(listRepositoryMock).findById(eq(listId))

        val result = listservice.findById(listId)

        assertThat(result).isEqualTo(list)
        verify(listRepositoryMock).findById(listId)
    }



    @Test
    fun `create list - verify calls`() {

        val user = User(UUID.fromString("dd8aac75-10b5-4f92-ba2e-4a7bddc003ce"), "test", "test@test.cz")
        val newList = NewList("test", user.id)

        doAnswer { user }.whenever(userServiceMock).findById(eq(user.id))

        doAnswer { List(UUID.randomUUID(), "test-list") }.whenever(listRepositoryMock).save(any())
        doNothing().whenever(userListCrossReferenceServiceMock).createCrossReference(any(), eq(user.id))

        listservice.createList(newList)

        inOrder(userServiceMock, userListCrossReferenceServiceMock, listRepositoryMock) {
            verify(userServiceMock, calls(1)).findById(user.id)
            verify(listRepositoryMock, calls(1)).save(any())
            verify(userListCrossReferenceServiceMock, calls(1)).createCrossReference(any(), eq(user.id))
        }
    }


    @Test
    fun `create list mapping test - new list to list`() {
        val user = User(UUID.fromString("dd8aac75-10b5-4f92-ba2e-4a7bddc003ce"), "test", "test@test.cz")
        val newList = NewList("test", user.id)
        val listUUID = UUID.fromString("ee115ae7-2ce9-4309-86e3-c9b7aac96560")

        val list = List(listUUID, "test", true)

        doAnswer { user }.whenever(userServiceMock).findById(any())
        doNothing().whenever(userListCrossReferenceServiceMock).createCrossReference(any(), any())
        doAnswer { list }.whenever(listRepositoryMock).save(eq(list))

        Mockito.mockStatic(UUID::class.java).use { mockedUUID ->
            mockedUUID.`when`<Any> { UUID.randomUUID() }.thenReturn(listUUID)
            assertThat(listservice.createList(newList)).isEqualTo(list)
        }
    }

}
