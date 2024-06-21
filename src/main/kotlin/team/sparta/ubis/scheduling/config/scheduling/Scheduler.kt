package team.sparta.ubis.scheduling.config.scheduling

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import team.sparta.ubis.scheduling.config.batch.BatchConfig
import java.time.LocalDateTime

@Component
@EnableScheduling
class Scheduler(
    private val jobLauncher: JobLauncher,
    private val batchConfig: BatchConfig,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedDelay = 10000)
    fun runJob() {
        val time = LocalDateTime.now().toString()
        try {
            val jobParam = JobParametersBuilder().addString("time", time)
            jobLauncher.run(batchConfig.job(), jobParam.toJobParameters())
        } catch (e: Exception) {
            logger.error("Job execution failed", e)
        }
    }
}