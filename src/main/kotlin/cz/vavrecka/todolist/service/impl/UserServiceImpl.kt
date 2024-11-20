package cz.vavrecka.todolist.service.impl

import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.exception.NotFoundException
import cz.vavrecka.todolist.model.NewUser
import cz.vavrecka.todolist.repository.UserRepository
import cz.vavrecka.todolist.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun create(newUser: NewUser) = userRepository.save(newUser.toUser())

    override fun findById(id: UUID): User = userRepository.findById(id).orElseThrow { NotFoundException("User: $id not found") }

    private fun NewUser.toUser(): User {
        return User(id = UUID.randomUUID(), name = name, email = email, isNew = true)
    }

}