package no.nav.tiltakspenger.scheduler.domain

import mu.KotlinLogging
import java.time.*
import java.time.temporal.WeekFields
import java.util.Locale


val log = KotlinLogging.logger {}

class EventsGenerator(private val events: EventsPublisher) {

    fun generateEventsFor(date: LocalDate) {
        log.info { "Genererer events for $date" }
        val year: Year = Year.from(date)

        // Dag
        events.publishEvent(DayHasBegun.of(date))

        // Uke
        val locale = Locale(NORSK_BOKMÅL)
        val currentWeekOfYear = date.get(WeekFields.of(locale).weekOfYear())
        val yesterdaysWeekOfYear = date.minusDays(1).get(WeekFields.of(locale).weekOfYear())
        if (currentWeekOfYear != yesterdaysWeekOfYear) {
            events.publishEvent(WeekHasBegun.of(year, currentWeekOfYear))
        }

        // Måned
        if (date.dayOfMonth == 1) {
            events.publishEvent(MonthHasBegun.of(YearMonth.from(date)))
        }

        // År
        if (MonthDay.from(date).equals(JAN_1)) {
            events.publishEvent(YearHasBegun.of(year))
        }
    }

    companion object {
        private val JAN_1: MonthDay = MonthDay.of(Month.JANUARY, 1)
        private const val NORSK_BOKMÅL = "nb"
    }
}
