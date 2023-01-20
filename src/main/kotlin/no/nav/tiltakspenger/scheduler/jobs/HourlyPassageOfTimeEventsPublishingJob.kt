package no.nav.tiltakspenger.scheduler.jobs

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.domain.HourlyEventsGenerator
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDateTime
import java.time.ZoneId

private val LOG = KotlinLogging.logger {}

class HourlyPassageOfTimeEventsPublishingJob(
    rapidsConnection: RapidsConnection,
    private val hourlyEventsGenerator: HourlyEventsGenerator = HourlyEventsGenerator(
        RapidEventPublisher(rapidsConnection)
    )
) : Job {
    override fun execute(context: JobExecutionContext) {
        LOG.info { "Executing HourlyPassageOfTimeEventsPublishingJob" }
        val localDateTime = findScheduledTime(context)
        hourlyEventsGenerator.generateEventsFor(localDateTime)
    }

    private fun findScheduledTime(context: JobExecutionContext): LocalDateTime {
        LOG.info { "LocalDateTime.now: ${LocalDateTime.now()}" }
        val scheduledFireTime = context.scheduledFireTime
        LOG.info { "scheduledFireTime $scheduledFireTime" }
        val localDateTime = LocalDateTime.ofInstant(
            scheduledFireTime.toInstant(),
            ZoneId.systemDefault()
        )
        LOG.info { "scheduledFireTime converted to LocalDateTime $localDateTime" }
        return localDateTime
    }
}
