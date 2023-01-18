package no.nav.tiltakspenger.scheduler

import mu.KotlinLogging
import no.nav.helse.rapids_rivers.RapidApplication
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.tiltakspenger.scheduler.db.flywayMigrate
import no.nav.tiltakspenger.scheduler.jobs.SchedulingService

val log = KotlinLogging.logger {}
val securelog = KotlinLogging.logger("tjenestekall")

internal class ApplicationBuilder : RapidsConnection.StatusListener {
    val rapidsConnection: RapidsConnection = RapidApplication.Builder(
        RapidApplication.RapidApplicationConfig.fromEnv(Configuration.rapidsAndRivers)
    )
        .withKtorModule {
        }
        .build()

    val scheduler = SchedulingService(
        databaseConfig = Configuration.databaseConfig(),
        rapidsConnection = rapidsConnection
    )

    init {
        rapidsConnection.register(this)
    }

    fun start() {
        rapidsConnection.start()
    }

    override fun onShutdown(rapidsConnection: RapidsConnection) {
        log.info("Shutdown")
        scheduler.stop()
    }

    override fun onStartup(rapidsConnection: RapidsConnection) {
        log.info("Skal kjøre flyway migrering")
        flywayMigrate()
        log.info("Har kjørt flyway migrering")
        scheduler.scheduleJob()
        scheduler.start()
    }
}
