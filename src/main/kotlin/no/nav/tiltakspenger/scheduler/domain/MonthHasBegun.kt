package no.nav.tiltakspenger.scheduler.domain

import java.time.YearMonth

data class MonthHasBegun private constructor(val month: YearMonth) {

    companion object {
        fun of(month: YearMonth): MonthHasBegun = MonthHasBegun(month)
    }
}
