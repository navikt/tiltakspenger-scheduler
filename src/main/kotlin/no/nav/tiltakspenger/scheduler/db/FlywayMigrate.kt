package no.nav.tiltakspenger.scheduler.db

import no.nav.tiltakspenger.scheduler.Configuration
import no.nav.tiltakspenger.scheduler.Profile
import org.flywaydb.core.Flyway

private fun flyway(): Flyway =
    when (Configuration.applicationProfile()) {
        Profile.LOCAL -> localFlyway()
        else -> gcpFlyway()
    }

private fun localFlyway() = Flyway
    .configure()
    .encoding("UTF-8")
    .locations("db/migration", "db/local-migration")
    .dataSource(DataSource.hikariDataSource)
    .cleanDisabled(false)
    .cleanOnValidationError(true)
    .load()

private fun gcpFlyway() = Flyway
    .configure()
    .encoding("UTF-8")
    .dataSource(DataSource.hikariDataSource)
    .cleanDisabled(false)
    .cleanOnValidationError(true)
    .load()


fun flywayMigrate() {
    flyway().migrate()
}

fun flywayCleanAndMigrate() {
    val flyway = flyway()
    flyway.clean()
    flyway.migrate()
}
