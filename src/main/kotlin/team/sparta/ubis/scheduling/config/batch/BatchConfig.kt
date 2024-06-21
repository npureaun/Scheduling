package team.sparta.ubis.scheduling.config.batch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class BatchConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    @Bean
    fun job(): Job {
        return JobBuilder("HelloJob", jobRepository)
            .start(step())
            .build()
    }

    @Bean
    fun step(): Step {
        return StepBuilder("testStep", jobRepository)
            .tasklet(tasklet(), transactionManager)
            .build()
    }

    @Bean
    fun tasklet(): Tasklet {
        return Tasklet { _, _ ->
            logger.info("***** hello batch! *****")
            RepeatStatus.FINISHED
        }
    }
}