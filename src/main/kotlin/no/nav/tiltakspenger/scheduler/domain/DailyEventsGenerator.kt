package no.nav.tiltakspenger.scheduler.domain

import mu.KotlinLogging
import org.threeten.extra.YearWeek
import java.time.*


private val log = KotlinLogging.logger {}

class DailyEventsGenerator(private val events: EventsPublisher) {

    fun generateEventsFor(date: LocalDate) {
        log.info { "Genererer events for $date" }
        val year: Year = Year.from(date)

        // Dag
        events.publishDayHasBegun(DayHasBegun(date))

        // Uke, basert på ISO8601
        val currentYearWeek = YearWeek.from(date)
        val yesterdaysYearWeek = YearWeek.from(date.minusDays(1))
        if (currentYearWeek.week != yesterdaysYearWeek.week) {
            events.publishWeekHasBegun(WeekHasBegun(currentYearWeek))
        }

        // Måned
        if (date.dayOfMonth == 1) {
            events.publishMonthHasBegun(MonthHasBegun(YearMonth.from(date)))
        }

        // År
        if (MonthDay.from(date).equals(JAN_1)) {
            events.publishYearHasBegun(YearHasBegun(year))
        }
    }

    companion object {
        private val JAN_1: MonthDay = MonthDay.of(Month.JANUARY, 1)
    }
}
