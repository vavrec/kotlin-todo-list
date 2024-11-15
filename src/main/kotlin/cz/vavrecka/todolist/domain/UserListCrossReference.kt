package cz.vavrecka.todolist.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*


/**
 * This entity represents a cross-reference table. JDBC does not support composite keys, the user ID is marked as the primary key.
 * This workaround may impact the Repository bean. CRUD Repository require entity id.
 */

@Table("list_user")
data class UserListCrossReference(
    @Id @Column("user_id") val userId: UUID,
    @Column("list_id") val listId: UUID,
    @JvmField @Transient @JsonIgnore val isNew: Boolean
) : Persistable<UUID> {

    override fun getId() = userId

    override fun isNew() = isNew
}