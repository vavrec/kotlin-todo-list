package cz.vavrecka.todolist.service

import java.util.*

interface UserListCrossReferenceService {

    fun createCrossReference(listId: UUID, userId: UUID)
}