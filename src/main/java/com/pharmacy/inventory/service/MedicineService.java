package com.pharmacy.inventory.service;

import com.pharmacy.inventory.model.Medicine;

import java.util.List;

/**
 * MedicineService Interface - Service Layer Contract.
 *
 * Defines all business operations for Medicine management.
 * The implementation class (MedicineServiceImpl) provides the actual logic.
 *
 * Using an interface promotes:
 *   - Loose coupling between controller and implementation
 *   - Easier unit testing via mocking
 *   - Open/Closed Principle (extend without modifying)
 */
public interface MedicineService {

    /** Add a new medicine to the inventory */
    Medicine addMedicine(Medicine medicine);

    /** Retrieve all medicines in the inventory */
    List<Medicine> getAllMedicines();

    /** Retrieve a single medicine by its ID */
    Medicine getMedicineById(Long id);

    /** Update an existing medicine's information */
    Medicine updateMedicine(Long id, Medicine medicineDetails);

    /** Remove a medicine from the inventory */
    void deleteMedicine(Long id);

    /** Search medicines by name (partial, case-insensitive) */
    List<Medicine> searchByName(String name);

    /** Filter medicines by category */
    List<Medicine> getByCategory(String category);
}
