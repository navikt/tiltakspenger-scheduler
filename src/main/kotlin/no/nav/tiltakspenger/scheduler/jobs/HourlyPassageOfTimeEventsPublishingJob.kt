package no.nav.tiltakspenger.scheduler.jobs

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.domain.HourlyEventsGenerator
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date


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
        val startTime = context.trigger.startTime // TODO: Det viser seg at denne ikke er korrekt dessverre!
        log.info { "context.trigger.starttime $startTime" }

        val nextFireTime = context.trigger.nextFireTime
        val subsequentFireTime = context.trigger.getFireTimeAfter(nextFireTime);
        val interval = subsequentFireTime.time - nextFireTime.time;

        val thisFireTime = Date(nextFireTime.time - interval)
        log.info { "thisFireTime $thisFireTime" }

        val localDateTime = LocalDateTime.ofInstant(
            thisFireTime.toInstant(),
            ZoneId.systemDefault()
        )
        log.info { "thisFireTime converted $localDateTime" }
        hourlyEventsGenerator.generateEventsFor(localDateTime)
    }
}
