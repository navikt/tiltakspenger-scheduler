package no.nav.tiltakspenger.scheduler.jobs

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.domain.EventsGenerator
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDate


val log = KotlinLogging.logger {}

class PassageOfTimeEventsPublishingJob(
    rapidsConnection: RapidsConnection,
    private val eventsGenerator: EventsGenerator = EventsGenerator(RapidEventPublisher(rapidsConnection))
) : Job {
    override fun execute(context: JobExecutionContext) {
        log.info { "Executing PassageOfTimeEventsPublishingJob" }
        eventsGenerator.generateEventsFor(LocalDate.now())
    }
}
