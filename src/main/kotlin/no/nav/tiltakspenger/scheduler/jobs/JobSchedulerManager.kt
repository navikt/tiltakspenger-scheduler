package no.nav.tiltakspenger.scheduler.jobs

import no.nav.tiltakspenger.scheduler.Configuration
import org.quartz.Scheduler
import org.quartz.SchedulerFactory
import org.quartz.impl.StdSchedulerFactory
import org.quartz.spi.JobFactory
import java.util.Properties

class JobSchedulerManager(
    private val databaseConfig: Configuration.DatabaseConfig,
    private val jobFactory: JobFactory,
) {

    fun scheduler(): Scheduler {
        val props = Properties()

        props["org.quartz.scheduler.instanceName"] = "PassageOfTimeScheduler"
        props["org.quartz.scheduler.instanceId"] = "AUTO"

        props["org.quartz.threadPool.class"] = "org.quartz.simpl.SimpleThreadPool"
        props["org.quartz.threadPool.threadCount"] = "3"

        props["org.quartz.jobStore.class"] = "org.quartz.impl.jdbcjobstore.JobStoreTX"
        props["org.quartz.jobStore.driverDelegateClass"] = "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate"
        props["org.quartz.jobStore.dataSource"] = "quartzDS"
        props["org.quartz.jobStore.tablePrefix"] = "QRTZ_"

        props["org.quartz.dataSource.quartzDS.driver"] = "org.postgresql.Driver"
        props["org.quartz.dataSource.quartzDS.URL"] =
            "jdbc:postgresql://${databaseConfig.host}:${databaseConfig.port}/${databaseConfig.database}"
        props["org.quartz.dataSource.quartzDS.user"] = "${databaseConfig.user}"
        props["org.quartz.dataSource.quartzDS.password"] = "${databaseConfig.password}"
        props["org.quartz.dataSource.quartzDS.maxConnections"] = "10"

        // props["org.quartz.plugin.triggHistory.class"] = "org.quartz.plugins.history.LoggingTriggerHistoryPlugin"
        // props["org.quartz.plugin.triggHistory.triggerFiredMessage"] =
        //     """Trigger {1}.{0} fired job {6}.{5} at: {4, date, HH:mm:ss MM/dd/yyyy}"""
        // props["org.quartz.plugin.triggHistory.triggerCompleteMessage"] =
        //     """Trigger {1}.{0} completed firing job {6}.{5} at {4, date, HH:mm:ss MM/dd/yyyy}"""

        val schedulerFactory: SchedulerFactory = StdSchedulerFactory(props)
        val scheduler = schedulerFactory.scheduler
        scheduler.setJobFactory(jobFactory)
        return scheduler
    }
}
