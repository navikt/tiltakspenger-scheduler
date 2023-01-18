package no.nav.tiltakspenger.scheduler.domain

import java.time.Year


data class YearHasBegun private constructor(val year: Year) {

    companion object {
        fun of(year: Year): YearHasBegun = YearHasBegun(year)
    }
}
