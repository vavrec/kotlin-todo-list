package cz.vavrecka.todolist.controller

import cz.vavrecka.todolist.controller.UserController.Companion.PATH
import cz.vavrecka.todolist.domain.User
import cz.vavrecka.todolist.exception.NotFoundException
import cz.vavrecka.todolist.model.NewUser
import cz.vavrecka.todolist.service.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE as JSON

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping(PATH)
@Tag(name = "User", description = "User API")
class UserController(private val userService: UserService) {

    companion object {
        const val PATH = "/api/v1/user"
    }


    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "400",
                description = "Request contains invalid data like invalid email.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ProblemDetail::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ProblemDetail::class)
                )]
            )]
    )
    @PostMapping(consumes = [JSON], produces = [JSON])
    fun createUser(@RequestBody @Valid newUser: NewUser): ResponseEntity<User> {
        userService.create(newUser).let {
            return ResponseEntity(it, HttpStatus.CREATED)
        }
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "400",
                description = "Request contains invalid data like invalid user id.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ProblemDetail::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ProblemDetail::class)
                )]
            )]
    )
    @GetMapping(path = ["/{id}"], produces = [JSON])
    fun finById(@PathVariable id: UUID): ResponseEntity<User> {
        userService.findById(id).let {
            return ResponseEntity(it, HttpStatus.OK)
        }
    }


    @ExceptionHandler(NotFoundException::class)
    fun notFoundExceptionHandler(notFoundException: NotFoundException): ProblemDetail {
        logger.info { notFoundException }
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid data")
    }

}