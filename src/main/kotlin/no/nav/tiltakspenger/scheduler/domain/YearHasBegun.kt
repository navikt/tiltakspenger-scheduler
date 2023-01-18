package no.nav.tiltakspenger.scheduler.domain

import java.time.LocalDate
import java.time.Month
import java.time.Year


@Suppress("MagicNumber")
data class YearHasBegun private constructor(private val year: Year) {

    val startDate: LocalDate
        get() = LocalDate.of(year.value, Month.JANUARY, 1)

    val endDate: LocalDate
        get() = LocalDate.of(year.value, Month.DECEMBER, 31)


    companion object {
        fun of(year: Year): YearHasBegun = YearHasBegun(year)

        fun of(year: Int): YearHasBegun = of(Year.of(year))
    }
}
