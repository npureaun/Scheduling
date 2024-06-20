package team.sparta.ubis.scheduling.config.scheduling

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@EnableScheduling
class Scheduler(
    private val jobLauncher: JobLauncher,
    private val jobRegistry: JobRegistry
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Bean
    @ConditionalOnMissingBean
    //@ConditionalOnProperty(prefix = "spring.batch.job", name = arrayOf("enabled") , havingValue = "true", matchIfMissing = true)
    fun jobRegistryBeanPostProcessor(): JobRegistryBeanPostProcessor {
        val jobProcessor = JobRegistryBeanPostProcessor()
        jobProcessor.setJobRegistry(jobRegistry)
        return jobProcessor
    }

    @Scheduled(fixedDelay = 10000)
    fun runJob() {
        val time = LocalDateTime.now().toString()
        try {
            val job = jobRegistry.getJob("HelloJob")
            val jobParam = JobParametersBuilder().addString("time", time)
            jobLauncher.run(job, jobParam.toJobParameters())
        } catch (e: Exception) {
            logger.error("Job execution failed", e)
        }
    }
}