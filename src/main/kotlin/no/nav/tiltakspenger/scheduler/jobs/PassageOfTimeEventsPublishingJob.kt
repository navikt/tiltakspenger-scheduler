package no.nav.tiltakspenger.scheduler.jobs

import mu.KotlinLogging
import org.quartz.Job
import org.quartz.JobExecutionContext

private val log = KotlinLogging.logger {}

class PassageOfTimeEventsPublishingJob : Job {
    override fun execute(context: JobExecutionContext) {
        log.info { "Executing PassageOfTimeEventsPublishingJob. This is deprected and will be removed. Does nothing." }
    }
}
