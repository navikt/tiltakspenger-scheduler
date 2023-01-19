package no.nav.tiltakspenger.scheduler.domain

import java.time.LocalDateTime

data class HourHasBegun private constructor(val time: LocalDateTime) {

    companion object {
        fun of(time: LocalDateTime): HourHasBegun = HourHasBegun(time)
    }
}
