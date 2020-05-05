package org.kupcimat.survey.restapi

import org.kupcimat.survey.common.model.UserTaskEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

// TODO move to common
interface UserTaskRepository : CoroutineCrudRepository<UserTaskEntity, String>
