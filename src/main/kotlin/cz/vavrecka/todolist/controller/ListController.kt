package cz.vavrecka.todolist.controller


import cz.vavrecka.todolist.controller.ListController.Companion.PATH
import cz.vavrecka.todolist.domain.List
import cz.vavrecka.todolist.exception.NotFoundException
import cz.vavrecka.todolist.model.NewList
import cz.vavrecka.todolist.service.ListService
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

@RestController
@RequestMapping(PATH)
@Tag(name = "List", description = "List API")
class ListController(private val listService: ListService) {

    companion object {
        const val PATH = "/api/v1/list"
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "400",
                description = "Request contains invalid data like invalid list id.",
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
    fun createList(@RequestBody @Valid newList: NewList): ResponseEntity<List> {
        listService.createList(newList).let {
            return ResponseEntity(it, HttpStatus.CREATED)
        }
    }

    @ExceptionHandler(NotFoundException::class)
    fun userNotFoundExceptionHandler(notFoundException: NotFoundException): ProblemDetail {
        // TODO add logger
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid data")
    }

}