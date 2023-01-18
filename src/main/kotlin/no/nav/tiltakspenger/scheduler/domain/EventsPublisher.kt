package no.nav.tiltakspenger.scheduler.domain

interface EventsPublisher {
    fun publishDayHasBegun(dayHasBegun: DayHasBegun)
    fun publishWeekHasBegun(weekHasBegun: WeekHasBegun)
    fun publishMonthHasBegun(monthHasBegun: MonthHasBegun)
    fun publishYearHasBegun(yearHasBegun: YearHasBegun)
}
