package org.kupcimat.survey.common.repository

import org.kupcimat.survey.common.model.UserTaskEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserTaskRepository : CoroutineCrudRepository<UserTaskEntity, String>
