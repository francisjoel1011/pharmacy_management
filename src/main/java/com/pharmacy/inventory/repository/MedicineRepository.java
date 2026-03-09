package com.pharmacy.inventory.repository;

import com.pharmacy.inventory.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MedicineRepository - Data Access Layer.
 *
 * Extends JpaRepository which provides built-in methods:
 *   - save(entity)         : INSERT or UPDATE
 *   - findById(id)         : SELECT by primary key
 *   - findAll()            : SELECT all records
 *   - deleteById(id)       : DELETE by primary key
 *   - count()              : COUNT total records
 *   - existsById(id)       : check existence by primary key
 *
 * Custom query methods use Spring Data JPA's method name convention.
 */
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    /**
     * Search medicines by name (case-insensitive partial match).
     * Translates to: SELECT * FROM medicines WHERE medicine_name LIKE %name%
     *
     * @param name partial medicine name to search
     * @return list of matching medicines
     */
    List<Medicine> findByMedicineNameContainingIgnoreCase(String name);

    /**
     * Filter medicines by category (case-insensitive exact match).
     * Translates to: SELECT * FROM medicines WHERE category = category
     *
     * @param category the category to filter by
     * @return list of medicines in that category
     */
    List<Medicine> findByCategoryIgnoreCase(String category);
}
