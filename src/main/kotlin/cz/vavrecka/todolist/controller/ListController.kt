package cz.vavrecka.todolist.controller


import cz.vavrecka.todolist.controller.ListController.Companion.PATH
import cz.vavrecka.todolist.domain.List
import cz.vavrecka.todolist.exception.NotFound
import cz.vavrecka.todolist.model.NewList
import cz.vavrecka.todolist.service.ListService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE as JSON

@RestController
@RequestMapping(PATH)
@Transactional
class ListController(private val listService: ListService) {

    companion object {
        const val PATH = "/api/v1/list"
    }

    @PostMapping(consumes = [JSON], produces = [JSON])
    fun createList(@RequestBody @Valid newList: NewList): ResponseEntity<List> {
        listService.createList(newList).let {
            return ResponseEntity(it, HttpStatus.CREATED)
        }
    }

    @ExceptionHandler(NotFound::class)
    fun userNotFoundExceptionHandler(notFound: NotFound): ProblemDetail {
        // TODO add logger
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid data")
    }

}