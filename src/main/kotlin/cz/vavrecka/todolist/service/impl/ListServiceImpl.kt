package cz.vavrecka.todolist.service.impl

import cz.vavrecka.todolist.domain.List
import cz.vavrecka.todolist.exception.NotFound
import cz.vavrecka.todolist.model.NewList
import cz.vavrecka.todolist.repository.ListRepository
import cz.vavrecka.todolist.service.ListService
import cz.vavrecka.todolist.service.UserListCrossReferenceService
import cz.vavrecka.todolist.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ListServiceImpl(
    private val listRepository: ListRepository,
    private val userService: UserService,
    private val userListCrossReferenceService: UserListCrossReferenceService
) : ListService {

    override fun findById(id: UUID): List = listRepository.findById(id).orElseThrow { NotFound("List: $id not found") }

    override fun createList(newList: NewList): List {
        // verifying that the user exists
        val user = userService.findById(newList.userId)

        val list = newList.toList();

        val savedList = listRepository.save(list)
        userListCrossReferenceService.createCrossReference(savedList.id,user.id)
        return savedList
    }

    private fun NewList.toList() = List(id = UUID.randomUUID(), name = this.name, true)
}