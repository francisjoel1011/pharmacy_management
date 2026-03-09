package com.pharmacy.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsConfig - Cross-Origin Resource Sharing Configuration.
 *
 * CORS is a browser security feature that blocks requests from a different
 * origin (protocol + host + port) than the server.
 *
 * Since our frontend (e.g., file:// or http://localhost:5500) and backend
 * (http://localhost:8080) run on different origins, we need to explicitly
 * allow cross-origin requests.
 *
 * This configuration:
 *   - Allows GET, POST, PUT, DELETE, OPTIONS methods
 *   - Accepts requests from any origin (*) - restrict in production
 *   - Allows all headers
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")          // Apply to all /api/ endpoints
                        .allowedOriginPatterns("*")     // Allow all origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")            // Allow all headers
                        .allowCredentials(false);       // No cookies/sessions needed
            }
        };
    }
}
