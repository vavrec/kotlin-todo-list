package cz.vavrecka.todolist.service.impl

import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.exception.UserNotFound
import cz.vavrecka.todolist.model.NewUser
import cz.vavrecka.todolist.repository.UserRepository
import cz.vavrecka.todolist.service.UserService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun create(newUser: NewUser) = userRepository.save(newUser.toUser())

    override fun findById(id: UUID) = userRepository.findById(id).orElseThrow { UserNotFound("User: $id not found") }

    private fun NewUser.toUser(): User {
        return User(id = UUID.randomUUID(), name = name, email = email, isNew = true)
    }

}