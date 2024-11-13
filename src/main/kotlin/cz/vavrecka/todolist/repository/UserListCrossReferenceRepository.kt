package cz.vavrecka.todolist.repository

import cz.vavrecka.todolist.domain.UserListCrossReference
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * This class ensure CRUD operations for the user list cross-reference table.
 * Spring JDBC does not support composite keys, this limitation may affect certain methods, such as findById."
 */
@Repository
interface UserListCrossReferenceRepository: CrudRepository<UserListCrossReference, UUID> {
}