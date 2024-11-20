package cz.vavrecka.todolist.service.impl

import cz.vavrecka.todolist.domain.Task
import cz.vavrecka.todolist.model.NewTask
import cz.vavrecka.todolist.repository.TaskRepository
import cz.vavrecka.todolist.service.ListService
import cz.vavrecka.todolist.service.TaskService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class TaskServiceImpl(private val listService: ListService, private val taskRepository: TaskRepository) : TaskService {


    override fun createTask(newTask: NewTask): Task {
        // verifying that the list exists
        listService.findById(newTask.listId)
        return taskRepository.save(newTask.toTask())
    }

    fun NewTask.toTask() = Task(UUID.randomUUID(), name, listId, true)
}