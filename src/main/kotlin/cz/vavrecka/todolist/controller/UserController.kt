package cz.vavrecka.todolist.controller

import cz.vavrecka.todolist.controller.UserController.Companion.PATH
import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.exception.UserNotFound
import cz.vavrecka.todolist.model.NewUser
import cz.vavrecka.todolist.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.UUID
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE as JSON

@RestController
@RequestMapping(PATH)
@Transactional
class UserController(private val userService: UserService) {

    companion object {
        const val PATH = "/api/v1/user"
    }

    @PostMapping(consumes = [JSON], produces = [JSON])
    fun createUser(@RequestBody newUser: NewUser): ResponseEntity<User> {
        userService.create(newUser).let {
            return ResponseEntity(it, HttpStatus.OK)
        }
    }

    @GetMapping(path = ["/{id}"], consumes = [JSON], produces = [JSON])
    fun finById(@PathVariable id: UUID): ResponseEntity<User> {
        userService.findById(id).let {
            return ResponseEntity(it, HttpStatus.OK)
        }
    }


    @ExceptionHandler(UserNotFound::class)
    fun userNotFoundExceptionHandler(userNotFound: UserNotFound): ProblemDetail {
        // TODO add logger
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid data")
    }

}