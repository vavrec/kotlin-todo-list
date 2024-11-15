package cz.vavrecka.todolist.service.impl

import cz.vavrecka.TestTags
import cz.vavrecka.todolist.domain.UserListCrossReference
import cz.vavrecka.todolist.repository.UserListCrossReferenceRepository
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.UUID

@Tag(TestTags.UNIT_TEST)
class UserListCrossReferenceServiceTest {

    val userListReferenceRepository = mock<UserListCrossReferenceRepository>()

    val userListCrossReferenceService = UserListCrossReferenceServiceImpl(userListReferenceRepository)

    @Test
    fun `create cross reference - mapping test`() {
        val listId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val userListReference = UserListCrossReference(userId, listId, true)

        doAnswer { userListReference }.whenever(userListReferenceRepository).save(eq(userListReference))

        userListCrossReferenceService.createCrossReference(listId,userId)

        verify(userListReferenceRepository).save(eq(userListReference))
    }

}