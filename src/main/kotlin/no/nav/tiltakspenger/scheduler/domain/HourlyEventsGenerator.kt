package no.nav.tiltakspenger.scheduler.domain

import mu.KotlinLogging
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private val log = KotlinLogging.logger {}

class HourlyEventsGenerator(private val events: EventsPublisher) {

    fun generateEventsFor(time: LocalDateTime) {
        val wholeHour: LocalDateTime = time.truncatedTo(ChronoUnit.HOURS)

        log.info { "Genererer events for $wholeHour" }

        // Time
        events.publishHourHasBegun(HourHasBegun(wholeHour))
    }
}
