package no.nav.tiltakspenger.scheduler.domain

interface EventsPublisher {
    fun publishHourHasBegun(hourHasBegun: HourHasBegun)
    fun publishDayHasBegun(dayHasBegun: DayHasBegun)
    fun publishWeekHasBegun(weekHasBegun: WeekHasBegun)
    fun publishMonthHasBegun(monthHasBegun: MonthHasBegun)
    fun publishYearHasBegun(yearHasBegun: YearHasBegun)
}
