package com.playtomic.tests.wallet.application.config;

import jakarta.ws.rs.container.TimeoutHandler;
import jakarta.ws.rs.core.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean to Configure Jersey. *
 */
@Configuration
public class RestConfig {

    /**
     * Bean to define default timeout handler. *
     */
    @Bean
    public TimeoutHandler defaultTimeHandler() {
        return asyncResponse ->
            asyncResponse.resume(
                Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("Operation timed out")
                    .build());
    }

    /**
     * Commented cause with J11 and SpringBoot 2.3 cause mismatch dependencies exception
     */
    /*@Bean
    public Consumer<ResourceConfig> customJerseyConfig() {
        return resourceConfig -> resourceConfig
            .register(WalletResource.class);
    }*/
}
