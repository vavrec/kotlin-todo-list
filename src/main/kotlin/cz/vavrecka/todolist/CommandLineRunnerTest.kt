package cz.vavrecka.todolist

import cz.vavrecka.todolist.domain.List
import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.domain.UserListCrossReference
import cz.vavrecka.todolist.repository.ListRepository
import cz.vavrecka.todolist.repository.UserListCrossReferenceRepository
import cz.vavrecka.todolist.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Profile("aa")
@Component
class CommandLineRunnerTest(
    private val listRepository: ListRepository,
    private val userListRepo: UserListCrossReferenceRepository,
    private val userRepository: UserRepository,
) : CommandLineRunner {


    @Transactional
    override fun run(vararg args: String?) {

//        val user = User(id = UUID.randomUUID(), name = "aaaa", email = "aaa@gmail.com", isNew = true)
//        val list = List(id = UUID.randomUUID(), name = "testbbbbbb", isNew = true)
//        val reference = UserListCrossReference(user.id, list.id, true)
//
//        userRepository.save(user)
//        listRepository.save(list)
//        userListRepo.save(reference)

        println("DONE")

    }
}