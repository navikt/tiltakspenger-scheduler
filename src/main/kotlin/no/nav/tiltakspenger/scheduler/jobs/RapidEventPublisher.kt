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
        publish("dayHasBegun" to dayHasBegun.date.toString())
    }

    override fun publishWeekHasBegun(weekHasBegun: WeekHasBegun) {
        log.info { "Publising weekHasBegun" }
        publish("weekHasBegun" to weekHasBegun.yearWeek.toString())
    }

    override fun publishMonthHasBegun(monthHasBegun: MonthHasBegun) {
        log.info { "Publising monthHasBegun" }
        publish("monthHasBegun" to monthHasBegun.month.toString())
    }

    override fun publishYearHasBegun(yearHasBegun: YearHasBegun) {
        log.info { "Publising yearHasBegun" }
        publish("yearHasBegun" to yearHasBegun.year.toString())
    }

    private fun publish(messagePair: Pair<String, Any>) {
        val uuid = UUID.randomUUID().toString()

        mutableMapOf(
            "@event_name" to "passage_of_time",
            "@opprettet" to LocalDateTime.now(),
            "@id" to uuid,
        )
            .plus(messagePair)
            .let { JsonMessage.newMessage(it) }
            .also { message ->
                log.info { "Publishing ${message.toJson()}" }
                rapidsConnection.publish(uuid, message.toJson())
            }
    }
}
