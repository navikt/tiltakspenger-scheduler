package no.nav.tiltakspenger.scheduler.domain

import java.time.LocalDate

data class DayHasBegun private constructor(val date: LocalDate) {

    companion object {
        fun of(date: LocalDate): DayHasBegun = DayHasBegun(date)
    }
}
