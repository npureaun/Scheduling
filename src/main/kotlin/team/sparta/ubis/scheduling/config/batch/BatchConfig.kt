package team.sparta.ubis.scheduling.config.batch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableBatchProcessing
class BatchConfig: DefaultBatchConfiguration() {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    @Bean
    fun helloJob(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Job {
        return JobBuilder("HelloJob", jobRepository)
            .start(helloStep(jobRepository, transactionManager))
            .build()
    }


    @Bean
    fun helloStep(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("testStep", jobRepository)
            .tasklet(tasklet(), transactionManager)
            .build()
    }


    fun tasklet(): Tasklet {
        return Tasklet { _: StepContribution, _: ChunkContext ->
            logger.info("***** hello batch! *****")
            RepeatStatus.FINISHED
        }
    }
}