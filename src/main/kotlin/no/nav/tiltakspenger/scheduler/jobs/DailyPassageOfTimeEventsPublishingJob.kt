package no.nav.tiltakspenger.scheduler.jobs

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.domain.DailyEventsGenerator
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDateTime
import java.time.ZoneId

private val LOG = KotlinLogging.logger {}

class DailyPassageOfTimeEventsPublishingJob(
    rapidsConnection: RapidsConnection,
    private val dailyEventsGenerator: DailyEventsGenerator = DailyEventsGenerator(RapidEventPublisher(rapidsConnection))
) : Job {
    override fun execute(context: JobExecutionContext) {
        LOG.info { "Executing DailyPassageOfTimeEventsPublishingJob" }
        LOG.info { "LocalDateTime.now: ${LocalDateTime.now()}" }
        val scheduledFireTime = context.scheduledFireTime
        LOG.info { "scheduledFireTime $scheduledFireTime" }
        val localDate = LocalDateTime.ofInstant(
            scheduledFireTime.toInstant(),
            ZoneId.systemDefault()
        ).toLocalDate()
        LOG.info { "scheduledFireTime converted to LocalDate: $localDate" }
        dailyEventsGenerator.generateEventsFor(localDate)
    }
}
