package no.nav.tiltakspenger.scheduler.jobs

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.domain.*
import java.time.LocalDateTime
import java.util.UUID


private val log = KotlinLogging.logger {}

class RapidEventPublisher(private val rapidsConnection: RapidsConnection) : EventsPublisher {
    override fun publishDayHasBegun(dayHasBegun: DayHasBegun) {
        log.info { "Publising dayHasBegun" }
        publish("dayHasBegunEvent", "dayHasBegun" to dayHasBegun.date.toString())
    }

    override fun publishWeekHasBegun(weekHasBegun: WeekHasBegun) {
        log.info { "Publising weekHasBegun" }
        publish("weekHasBegunEvent", "weekHasBegun" to weekHasBegun.yearWeek.toString())
    }

    override fun publishMonthHasBegun(monthHasBegun: MonthHasBegun) {
        log.info { "Publising monthHasBegun" }
        publish("monthHasBegunEvent", "monthHasBegun" to monthHasBegun.month.toString())
    }

    override fun publishYearHasBegun(yearHasBegun: YearHasBegun) {
        log.info { "Publising yearHasBegun" }
        publish("yearHasBegunEvent", "yearHasBegun" to yearHasBegun.year.toString())
    }

    override fun publishHourHasBegun(hourHasBegun: HourHasBegun) {
        log.info { "Publising hourHasBegun" }
        publish("hourHasBegunEvent", "hourHasBegun" to hourHasBegun.time.toString())
    }

    private fun publish(eventName: String, messagePair: Pair<String, Any>) {
        val uuid = UUID.randomUUID().toString()

        mutableMapOf(
            "@event_name" to eventName,
            "@opprettet" to LocalDateTime.now(),
            "@id" to uuid,
        )
            .plus(messagePair)
            .let { JsonMessage.newMessage(it) }
            .also { message ->
                log.info { "Publishing PassageOfTime event ${message.toJson()}" }
                rapidsConnection.publish(uuid, message.toJson())
            }
    }
}
