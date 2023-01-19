package no.nav.tiltakspenger

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.RapidApplication
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.Configuration
import no.nav.tiltakspenger.scheduler.db.flywayMigrate
import no.nav.tiltakspenger.scheduler.jobs.SchedulingService

fun main() {
    System.setProperty("logback.configurationFile", Configuration.logbackConfigurationFile())

    val log = KotlinLogging.logger {}
    val securelog = KotlinLogging.logger("tjenestekall")

    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        log.error { "Uncaught exception logget i securelog" }
        securelog.error(e) { e.message }
    }

    RapidApplication.create(Configuration.rapidsAndRivers).apply {

        val scheduler = SchedulingService(
            databaseConfig = Configuration.databaseConfig(),
            rapidsConnection = this
        )

        register(object : RapidsConnection.StatusListener {
            override fun onStartup(rapidsConnection: RapidsConnection) {
                log.info { "Starting tiltakspenger-scheduler" }
                flywayMigrate()
                log.info("Har kjørt flyway migrering")
                scheduler.scheduleJob()
                scheduler.start()
                log.info("Har startet schduler")
            }

            override fun onShutdown(rapidsConnection: RapidsConnection) {
                log.info { "Stopping tiltakspenger-skjerming" }
                scheduler.stop()
                super.onShutdown(rapidsConnection)
            }
        })
    }.start()
}
