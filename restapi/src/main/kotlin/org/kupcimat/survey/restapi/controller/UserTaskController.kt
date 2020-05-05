package org.kupcimat.survey.restapi.controller

import org.kupcimat.survey.common.model.Paging
import org.kupcimat.survey.common.model.UserTask
import org.kupcimat.survey.common.model.UserTaskList
import org.kupcimat.survey.restapi.service.UserTaskService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriTemplate

@RestController
class UserTaskController(val userTaskService: UserTaskService) {

    @PostMapping(UriTemplates.USER_TASKS)
    suspend fun createTask(@RequestBody task: UserTask): ResponseEntity<UserTask> {
        // TODO add input validation
        val (newTaskId, newTask) = userTaskService.createTask(task)
        return ResponseEntity
            .created(UriTemplate(UriTemplates.USER_TASK).expand(newTaskId))
            .body(newTask)
    }

    @GetMapping(UriTemplates.USER_TASKS)
    suspend fun getTasks(): ResponseEntity<UserTaskList> {
        val tasks = userTaskService.getAllTasks()
        return ResponseEntity.ok(UserTaskList(tasks, Paging()))
    }

    @GetMapping(UriTemplates.USER_TASK)
    suspend fun getTask(@PathVariable taskId: String): ResponseEntity<UserTask> {
        val task = userTaskService.getTask(taskId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(task)
    }
}
