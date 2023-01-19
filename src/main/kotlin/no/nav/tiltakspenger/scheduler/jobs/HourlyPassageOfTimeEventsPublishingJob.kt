package no.nav.tiltakspenger.scheduler.jobs

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.domain.HourlyEventsGenerator
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDateTime
import java.time.ZoneId


private val log = KotlinLogging.logger {}

class HourlyPassageOfTimeEventsPublishingJob(
    rapidsConnection: RapidsConnection,
    private val hourlyEventsGenerator: HourlyEventsGenerator = HourlyEventsGenerator(
        RapidEventPublisher(rapidsConnection)
    )
) : Job {
    override fun execute(context: JobExecutionContext) {
        log.info { "Executing HourlyPassageOfTimeEventsPublishingJob" }
        log.info { "LocalDateTime.now(): ${LocalDateTime.now()}" }
        val startTime = context.trigger.startTime
        log.info { "context.trigger.starttime $startTime" }
        val localDateTime = LocalDateTime.ofInstant(
            startTime.toInstant(),
            ZoneId.systemDefault()
        )
        log.info { "starttime converted $localDateTime" }
        hourlyEventsGenerator.generateEventsFor(localDateTime)
    }
}
