package cz.vavrecka.todolist.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class NewUser(
    @field:NotBlank val name: String,
    @field:Email val email: String
)
