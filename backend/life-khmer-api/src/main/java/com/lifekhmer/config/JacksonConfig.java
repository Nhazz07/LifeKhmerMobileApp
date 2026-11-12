package com.lifekhmer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides the mapper used by security handlers before an MVC controller is
 * invoked. Registering available modules also supports Java time values in
 * the shared API error response.
 */
@Configuration
public class JacksonConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }
}
