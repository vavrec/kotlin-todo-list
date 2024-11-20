package cz.vavrecka.todolist.controller

import cz.vavrecka.todolist.controller.TaskController.Companion.PATH
import cz.vavrecka.todolist.domain.Task
import cz.vavrecka.todolist.exception.NotFound
import cz.vavrecka.todolist.model.NewTask
import cz.vavrecka.todolist.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE as JSON

@RestController
@RequestMapping(PATH)
class TaskController(private val taskService: TaskService) {

    companion object {
        const val PATH = "/api/v1/task"
    }

    @PostMapping(produces = [JSON], consumes = [JSON])
    fun createTask(@RequestBody @Valid newTask: NewTask): ResponseEntity<Task> {
        taskService.createTask(newTask).let {
            return ResponseEntity(it, HttpStatus.CREATED)
        }
    }

    @ExceptionHandler(NotFound::class)
    fun userNotFoundExceptionHandler(notFound: NotFound): ProblemDetail {
        // TODO add logger
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid data")
    }

}