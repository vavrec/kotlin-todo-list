package cz.vavrecka.todolist.service.impl

import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.model.CreateUser
import cz.vavrecka.todolist.repository.UserRepository
import cz.vavrecka.todolist.service.UserService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(val userRepository: UserRepository) : UserService {

    override fun create(newUser: CreateUser) = userRepository.save(newUser.toUser())

    private fun CreateUser.toUser(): User {
        return User(id = UUID.randomUUID(), name = name, email = email, isNew = true)
    }

}