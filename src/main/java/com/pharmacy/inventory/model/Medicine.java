package com.pharmacy.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Medicine Entity - mapped to the "medicines" table in MySQL.
 *
 * JPA Annotations:
 *   @Entity        - marks this class as a JPA entity (database table)
 *   @Table         - specifies the table name
 *   @Id            - marks the primary key
 *   @GeneratedValue- auto-generates the primary key value
 *   @Column        - maps field to a specific database column
 *
 * Lombok Annotations:
 *   @Data          - generates getters, setters, toString, equals, hashCode
 *   @NoArgsConstructor - generates a no-argument constructor
 *   @AllArgsConstructor - generates a constructor with all fields
 */
@Entity
@Table(name = "medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {

    /**
     * Primary Key - auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicineId;

    /**
     * Medicine name - mandatory, cannot be blank.
     */
    @NotBlank(message = "Medicine name is mandatory")
    @Column(name = "medicine_name", nullable = false, length = 150)
    private String medicineName;

    /**
     * Medical category (e.g., Antibiotics, Analgesics) - mandatory.
     */
    @NotBlank(message = "Category is mandatory")
    @Column(name = "category", nullable = false, length = 100)
    private String category;

    /**
     * Name of the manufacturer/pharma company.
     */
    @Column(name = "manufacturer", length = 150)
    private String manufacturer;

    /**
     * Selling price - mandatory and must be a positive value.
     */
    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be a positive value")
    @Column(name = "price", nullable = false)
    private Double price;

    /**
     * Current stock quantity - mandatory and must be positive.
     */
    @NotNull(message = "Stock quantity is mandatory")
    @Positive(message = "Stock quantity must be a positive value")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    /**
     * Expiry date of the medicine.
     */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    /**
     * Timestamp when the record was first created.
     * Set automatically before the entity is persisted.
     */
    @Column(name = "created_date_time", updatable = false)
    private LocalDateTime createdDateTime;

    /**
     * Lifecycle callback - automatically sets createdDateTime before saving.
     */
    @PrePersist
    protected void onCreate() {
        this.createdDateTime = LocalDateTime.now();
    }
}
