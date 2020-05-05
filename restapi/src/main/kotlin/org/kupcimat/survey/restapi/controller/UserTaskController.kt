package org.kupcimat.survey.restapi.controller

import org.kupcimat.survey.common.model.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriTemplate

@RestController
class UserTaskController {

    @PostMapping(UriTemplates.USER_TASKS)
    suspend fun createTask(@RequestBody task: UserTask): ResponseEntity<UserTask> {
        // TODO add input validation
        return ResponseEntity
            .created(UriTemplate(UriTemplates.USER_TASK).expand("taskId"))
            .body(task.copy(status = TaskStatus.NEW))
    }

    @GetMapping(UriTemplates.USER_TASKS)
    suspend fun getTasks(): ResponseEntity<UserTaskList> {
        return ResponseEntity.ok(
            UserTaskList(
                items = listOf(
                    UserTask(phone = "1234", functionId = CloudFunction.EMAIL),
                    UserTask(phone = "6789", functionId = CloudFunction.TWITTER)
                ),
                paging = Paging()
            )
        )
    }

    @GetMapping(UriTemplates.USER_TASK)
    suspend fun getTask(@PathVariable taskId: String): ResponseEntity<UserTask> {
        return ResponseEntity.ok(UserTask(phone = "1234", functionId = CloudFunction.EMAIL))
    }
}
