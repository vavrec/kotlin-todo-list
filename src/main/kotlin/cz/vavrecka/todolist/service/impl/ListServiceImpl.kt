package cz.vavrecka.todolist.service.impl

import cz.vavrecka.todolist.domain.List
import cz.vavrecka.todolist.model.NewList
import cz.vavrecka.todolist.repository.ListRepository
import cz.vavrecka.todolist.service.ListService
import cz.vavrecka.todolist.service.UserListCrossReferenceService
import cz.vavrecka.todolist.service.UserService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ListServiceImpl(
    private val listRepository: ListRepository,
    private val userService: UserService,
    private val userListCrossReferenceService: UserListCrossReferenceService
) : ListService {

    override fun createList(newList: NewList): List {
        val user = userService.findById(newList.userId)
        val savedList = listRepository.save(newList.toList())
        userListCrossReferenceService.createCrossReference(savedList.id,user.id)
        return savedList
    }

    private fun NewList.toList() = List(id = UUID.randomUUID(), name = this.name)
}