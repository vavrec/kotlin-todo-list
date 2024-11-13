package cz.vavrecka.todolist.repository

import cz.vavrecka.todolist.domain.List
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ListRepository : CrudRepository<List, UUID> {
}