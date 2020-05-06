package org.kupcimat.survey.worker

import org.kupcimat.survey.common.model.CloudFunction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger("CloudFunctions")

private val mapping: Map<CloudFunction, (String) -> Unit> = mapOf(
    CloudFunction.EMAIL to ::emailFunction,
    CloudFunction.TWITTER to ::twitterFunction
)

fun executeFunction(functionId: CloudFunction, message: String) {
    val function = mapping[functionId]
    if (function != null) {
        function(message)
    } else {
        logger.warn("component=worker action=execute-function Unknown cloud function $functionId")
    }
}

fun emailFunction(message: String) {
    logger.info("component=worker action=email-function message=$message")
}

fun twitterFunction(message: String) {
    logger.info("component=worker action=twitter-function message=$message")
}
