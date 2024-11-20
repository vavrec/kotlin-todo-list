package cz.vavrecka.todolist.repository

import cz.vavrecka.todolist.domain.Task
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TaskRepository : CrudRepository<Task, UUID> {
}