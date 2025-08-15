package com.library.config;

import brave.Tracing;
import brave.sampler.Sampler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration for distributed tracing with Zipkin and Brave
 */
@Configuration
public class TracingConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${management.tracing.sampling.probability:0.1}")
    private float samplingProbability;

    /**
     * Configure Brave tracing with custom sampling
     */
    @Bean
    public Tracing tracing(Environment environment) {
        return Tracing.newBuilder()
                .localServiceName(applicationName)
                .sampler(Sampler.create(samplingProbability))
                .traceId128Bit(true)
                .supportsJoin(false)
                .build();
    }

    /**
     * Enable method-level observations with @Observed annotation
     */
    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
}
