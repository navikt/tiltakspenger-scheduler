package no.nav.tiltakspenger.scheduler.jobs

import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.domain.*
import java.time.LocalDateTime
import java.util.UUID

class RapidEventPublisher(private val rapidsConnection: RapidsConnection) : EventsPublisher {
    override fun publishEvent(dayHasBegun: DayHasBegun) {
        publish("dayHasBegun" to dayHasBegun)
    }

    override fun publishEvent(weekHasBegun: WeekHasBegun) {
        publish("weekHasBegun" to weekHasBegun)
    }

    override fun publishEvent(monthHasBegun: MonthHasBegun) {
        publish("monthHasBegun" to MonthHasBegun)
    }

    override fun publishEvent(yearHasBegun: YearHasBegun) {
        publish("yearHasBegun" to YearHasBegun)
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
                rapidsConnection.publish(uuid, message.toJson())
            }
    }
}
