package org.kupcimat.survey.restapi.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.kupcimat.survey.common.message.Queues
import org.kupcimat.survey.common.model.TaskStatus
import org.kupcimat.survey.common.model.UserTask
import org.kupcimat.survey.common.model.UserTaskMessage
import org.kupcimat.survey.common.repository.UserTaskRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.stereotype.Component

@Component
class UserTaskService(
    val amqpTemplate: AmqpTemplate,
    val userTaskRepository: UserTaskRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    suspend fun createTask(task: UserTask): Pair<String, UserTask> {
        // TODO can we store the same number multiple times
        val newTask = userTaskRepository.save(task.toEntity(TaskStatus.NEW))
        val newTaskId = newTask.id ?: throw IllegalStateException("Missing taskId") // db should always return id
        // TODO use async version of amqpTemplate
        amqpTemplate.convertAndSend(Queues.SENT_MESSAGES, newTaskId)
        logger.info("component=restapi action=task-created task-id=$newTaskId")
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

    suspend fun createMessage(taskId: String, message: UserTaskMessage): UserTask? {
        // TODO handle multiple messages case
        val task = userTaskRepository.findById(taskId) ?: return null
        val updatedTask = task.copy(status = TaskStatus.RECEIVED, message = message.value)
        userTaskRepository.save(updatedTask)
        // TODO use async version of amqpTemplate
        amqpTemplate.convertAndSend(Queues.RECEIVED_MESSAGES, taskId)
        logger.info("component=restapi action=message-received task-id=$taskId")
        return updatedTask.toUserTask()
    }
}
