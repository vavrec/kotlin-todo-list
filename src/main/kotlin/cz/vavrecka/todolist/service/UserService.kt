package cz.vavrecka.todolist.service

import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.model.NewUser
import java.util.*

interface UserService {

    fun create(newUser: NewUser) : User

    fun findById(id: UUID): User
}