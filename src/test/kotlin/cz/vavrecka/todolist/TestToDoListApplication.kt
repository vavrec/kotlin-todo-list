package cz.vavrecka.todolist

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<ToDoListApplication>().with(TestcontainersConfiguration::class).run(*args)
}
