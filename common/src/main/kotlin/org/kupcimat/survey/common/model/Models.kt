package org.kupcimat.survey.common.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import com.fasterxml.jackson.annotation.JsonTypeName
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@JsonIgnoreProperties(ignoreUnknown = true)
data class Paging(
    val nextUri: String? = null
)

@JsonTypeName("userTask")
@JsonTypeInfo(use = NAME, include = WRAPPER_OBJECT)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
data class UserTask(
    val phone: String,
    val functionId: CloudFunction,
    val status: TaskStatus? = null
) {
    fun toEntity(status: TaskStatus) = UserTaskEntity(phone, functionId, status)
}

@JsonTypeName("userTaskList")
@JsonTypeInfo(use = NAME, include = WRAPPER_OBJECT)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
data class UserTaskList(
    val items: List<UserTask>,
    val paging: Paging
)

@JsonTypeName("userTaskMessage")
@JsonTypeInfo(use = NAME, include = WRAPPER_OBJECT)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
data class UserTaskMessage(
    val value: String
)

@Document(collection = "userTask")
@TypeAlias("userTaskEntity")
data class UserTaskEntity(
    val phone: String,
    val functionId: CloudFunction,
    val status: TaskStatus,
    @Id
    val id: String? = null,
    val message: String? = null
) {
    fun toUserTask() = UserTask(phone, functionId, status)
}
