package no.nav.tiltakspenger.scheduler.jobs

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.domain.DayHasBegun
import no.nav.tiltakspenger.scheduler.domain.EventsPublisher
import no.nav.tiltakspenger.scheduler.domain.HourHasBegun
import no.nav.tiltakspenger.scheduler.domain.MonthHasBegun
import no.nav.tiltakspenger.scheduler.domain.WeekHasBegun
import no.nav.tiltakspenger.scheduler.domain.YearHasBegun
import java.time.LocalDateTime
import java.util.UUID

private val LOG = KotlinLogging.logger {}

class RapidEventPublisher(private val rapidsConnection: RapidsConnection) : EventsPublisher {
    override fun publishDayHasBegun(dayHasBegun: DayHasBegun) {
        LOG.info { "Publising dayHasBegun" }
        publish("dayHasBegunEvent", "dayHasBegun" to dayHasBegun.date.toString())
    }

    override fun publishWeekHasBegun(weekHasBegun: WeekHasBegun) {
        LOG.info { "Publising weekHasBegun" }
        publish("weekHasBegunEvent", "weekHasBegun" to weekHasBegun.yearWeek.toString())
    }

    override fun publishMonthHasBegun(monthHasBegun: MonthHasBegun) {
        LOG.info { "Publising monthHasBegun" }
        publish("monthHasBegunEvent", "monthHasBegun" to monthHasBegun.month.toString())
    }

    override fun publishYearHasBegun(yearHasBegun: YearHasBegun) {
        LOG.info { "Publising yearHasBegun" }
        publish("yearHasBegunEvent", "yearHasBegun" to yearHasBegun.year.toString())
    }

    override fun publishHourHasBegun(hourHasBegun: HourHasBegun) {
        LOG.info { "Publising hourHasBegun" }
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
                LOG.info { "Publishing PassageOfTime event ${message.toJson()}" }
                rapidsConnection.publish(uuid, message.toJson())
            }
    }
}
