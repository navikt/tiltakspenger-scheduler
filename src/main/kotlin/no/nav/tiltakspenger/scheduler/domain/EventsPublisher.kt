package no.nav.tiltakspenger.scheduler.domain

interface EventsPublisher {
    fun publishEvent(dayHasBegun: DayHasBegun)

    fun publishEvent(weekHasBegun: WeekHasBegun)

    fun publishEvent(monthHasBegun: MonthHasBegun)

    fun publishEvent(yearHasBegun: YearHasBegun)
}
