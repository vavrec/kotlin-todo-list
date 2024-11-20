package cz.vavrecka.todolist.controller

import cz.vavrecka.todolist.controller.TaskController.Companion.PATH
import cz.vavrecka.todolist.domain.Task
import cz.vavrecka.todolist.exception.NotFoundException
import cz.vavrecka.todolist.model.NewTask
import cz.vavrecka.todolist.service.TaskService
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
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE as JSON

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping(PATH)
@Tag(name = "Task", description = "Task API")
class TaskController(private val taskService: TaskService) {

    companion object {
        const val PATH = "/api/v1/task"
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "400",
                description = "Request contains invalid data like empty name.",
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
    @PostMapping(produces = [JSON], consumes = [JSON])
    fun createTask(@RequestBody @Valid newTask: NewTask): ResponseEntity<Task> {
        taskService.createTask(newTask).let {
            return ResponseEntity(it, HttpStatus.CREATED)
        }
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFoundExceptionHandler(notFoundException: NotFoundException): ProblemDetail {
        logger.info { notFoundException }
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid data")
    }

}