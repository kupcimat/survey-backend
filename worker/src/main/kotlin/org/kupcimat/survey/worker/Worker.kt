package org.kupcimat.survey.worker

import kotlinx.coroutines.runBlocking
import org.kupcimat.survey.common.message.Queues
import org.kupcimat.survey.common.model.TaskStatus
import org.kupcimat.survey.common.repository.UserTaskRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class Worker(val userTaskRepository: UserTaskRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @RabbitListener(queues = [Queues.SENT_MESSAGES])
    fun sendSms(taskId: String) = runBlocking<Unit> {
        val task = userTaskRepository.findById(taskId)
            ?: throw IllegalArgumentException("Task $taskId does not exist")
        // TODO send SMS
        logger.info("component=worker action=send-sms task-id=$taskId")
        userTaskRepository.save(task.copy(status = TaskStatus.SENT))
    }
}
