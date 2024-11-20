package cz.vavrecka.todolist.model

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class NewTask(@field:NotBlank val name : String, val listId : UUID) {
}