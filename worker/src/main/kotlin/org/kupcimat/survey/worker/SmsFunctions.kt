package org.kupcimat.survey.worker

import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger("CloudFunctions")

fun sendSms(phone: String, message: String) {
    logger.info("component=worker action=send-sms phone=$phone message=$message")
    // TODO replace with real SMS API
}
