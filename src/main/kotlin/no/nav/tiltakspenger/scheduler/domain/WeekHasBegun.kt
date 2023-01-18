package no.nav.tiltakspenger.scheduler.domain

import org.threeten.extra.YearWeek


data class WeekHasBegun private constructor(val yearWeek: YearWeek) {

    companion object {
        fun of(yearWeek: YearWeek): WeekHasBegun = WeekHasBegun(yearWeek)
    }
}
