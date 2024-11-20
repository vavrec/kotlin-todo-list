package cz.vavrecka.todolist.service

import cz.vavrecka.todolist.domain.Task
import cz.vavrecka.todolist.model.NewTask

interface TaskService {
    fun createTask(newTask: NewTask): Task
}