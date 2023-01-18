package no.nav.tiltakspenger.scheduler.domain

import java.time.Year


data class WeekHasBegun private constructor(
    private val year: Year,
    private val week: Int,
) {
    companion object {
        fun of(year: Year, week: Int): WeekHasBegun =
            WeekHasBegun(year, week)
    }
}
