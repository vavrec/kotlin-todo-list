package cz.vavrecka.todolist.service.impl

import cz.vavrecka.todolist.domain.UserListCrossReference
import cz.vavrecka.todolist.repository.UserListCrossReferenceRepository
import cz.vavrecka.todolist.service.UserListCrossReferenceService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserListCrossReferenceServiceImpl(private val userListReferenceRepository: UserListCrossReferenceRepository) :
    UserListCrossReferenceService {

    override fun createCrossReference(listId: UUID, userId: UUID) {
        userListReferenceRepository.save(
            UserListCrossReference(
                listId = listId,
                userId = userId,
                isNew = true
            )
        )
    }
}