package no.nav.tiltakspenger.scheduler.jobs

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.domain.EventsGenerator
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId


private val log = KotlinLogging.logger {}

class PassageOfTimeEventsPublishingJob(
    rapidsConnection: RapidsConnection,
    private val eventsGenerator: EventsGenerator = EventsGenerator(RapidEventPublisher(rapidsConnection))
) : Job {
    override fun execute(context: JobExecutionContext) {
        log.info { "Executing PassageOfTimeEventsPublishingJob" }
        log.info { "LocalDate.now(): ${LocalDate.now()}" }
        val startTime = context.trigger.startTime
        log.info { "context.trigger.starttime $startTime" }
        val localDate = LocalDateTime.ofInstant(
            startTime.toInstant(),
            ZoneId.systemDefault()
        ).toLocalDate()
        log.info { "starttime converted $localDate" }
        eventsGenerator.generateEventsFor(localDate)
    }
}
