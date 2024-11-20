package cz.vavrecka.todolist.service

import cz.vavrecka.todolist.domain.List
import cz.vavrecka.todolist.model.NewList
import java.util.*

interface ListService {

    fun findById(id: UUID): List

    fun createList(newList: NewList): List
}