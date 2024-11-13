package cz.vavrecka.todolist.service

import cz.vavrecka.todolist.domain.List
import cz.vavrecka.todolist.model.NewList

interface ListService {
    fun createList(newList: NewList): List
}