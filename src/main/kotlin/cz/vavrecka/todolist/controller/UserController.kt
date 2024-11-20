package cz.vavrecka.todolist.controller

import cz.vavrecka.todolist.controller.UserController.Companion.PATH
import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.exception.NotFound
import cz.vavrecka.todolist.model.NewUser
import cz.vavrecka.todolist.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE as JSON

@RestController
@RequestMapping(PATH)
class UserController(private val userService: UserService) {

    companion object {
        const val PATH = "/api/v1/user"
    }

    @PostMapping(consumes = [JSON], produces = [JSON])
    fun createUser(@RequestBody @Valid newUser: NewUser): ResponseEntity<User> {
        userService.create(newUser).let {
            return ResponseEntity(it, HttpStatus.CREATED)
        }
    }

    @GetMapping(path = ["/{id}"], produces = [JSON])
    fun finById(@PathVariable id: UUID): ResponseEntity<User> {
        userService.findById(id).let {
            return ResponseEntity(it, HttpStatus.OK)
        }
    }


    @ExceptionHandler(NotFound::class)
    fun notFoundExceptionHandler(notFound: NotFound): ProblemDetail {
        // TODO add logger
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid data")
    }

}