package com.pharmacy.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Pharmacy Inventory Management System.
 *
 * @SpringBootApplication enables:
 *   - @Configuration     : marks this class as a source of bean definitions
 *   - @EnableAutoConfiguration : auto-configures Spring based on classpath
 *   - @ComponentScan     : scans this package and sub-packages for components
 */
@SpringBootApplication
public class PharmacyInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmacyInventoryApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  Pharmacy Inventory System is running!");
        System.out.println("  API Base URL: http://localhost:8080/api/medicines");
        System.out.println("========================================\n");
    }
}
