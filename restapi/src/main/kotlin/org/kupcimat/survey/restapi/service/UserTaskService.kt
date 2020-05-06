package org.kupcimat.survey.restapi.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.kupcimat.survey.common.model.TaskStatus
import org.kupcimat.survey.common.model.UserTask
import org.kupcimat.survey.restapi.UserTaskRepository
import org.springframework.stereotype.Component

@Component
class UserTaskService(val userTaskRepository: UserTaskRepository) {

    suspend fun createTask(task: UserTask): Pair<String, UserTask> {
        // TODO can we store the same number multiple times
        val newTask = userTaskRepository.save(task.toEntity(TaskStatus.NEW))
        val newTaskId = newTask.id ?: throw IllegalStateException("Missing taskId") // db should always return id
        return newTaskId to newTask.toUserTask()
    }

    suspend fun getTask(taskId: String): UserTask? {
        val task = userTaskRepository.findById(taskId)
        return task?.toUserTask()
    }

    suspend fun getAllTasks(): List<UserTask> {
        val tasks = userTaskRepository.findAll()
        return tasks.map { it.toUserTask() }.toList()
    }
}
