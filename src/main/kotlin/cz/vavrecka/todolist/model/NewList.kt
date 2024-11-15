package cz.vavrecka.todolist.model

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class NewList(@field:NotBlank val name: String, val userId: UUID)
