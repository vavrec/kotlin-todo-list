package cz.vavrecka.todolist.service

import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.model.CreateUser

interface UserService {

    fun create(user: CreateUser) : User

}