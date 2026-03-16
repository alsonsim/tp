package seedu.pharmatracker.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class InventoryTest {
    @Test
    public void inventory_initiallyEmpty() {
        Inventory inventory = new Inventory();
        assertTrue(inventory.getMedications().isEmpty());
    }

    @Test
    public void inventory_addMedication_increasesSize() {
        Inventory inventory = new Inventory();
        Medication med = new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller");
        inventory.getMedications().add(med);
        assertEquals(1, inventory.getMedications().size());
    }

    @Test
    public void inventory_getMedication_returnsCorrectMedication() {
        Inventory inventory = new Inventory();
        Medication med = new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller");
        inventory.getMedications().add(med);
        assertEquals("Aspirin", inventory.getMedication(0).getName());
    }

    @Test
    public void inventory_multipleMedications_sizeIsCorrect() {
        Inventory inventory = new Inventory();
        inventory.getMedications().add(new Medication("MedA", "10mg", 10, "2026-01-01", "tag"));
        inventory.getMedications().add(new Medication("MedB", "20mg", 20, "2026-06-01", "tag"));
        inventory.getMedications().add(new Medication("MedC", "30mg", 30, "2027-01-01", "tag"));
        assertEquals(3, inventory.getMedications().size());
    }

    @Test
    public void sortByExpiryDate_sortsEarliestFirst() {
        Inventory inventory = new Inventory();
        inventory.getMedications().add(new Medication("MedC", "10mg", 10, "2028-06-01", "tag"));
        inventory.getMedications().add(new Medication("MedA", "10mg", 10, "2026-03-20", "tag"));
        inventory.getMedications().add(new Medication("MedB", "10mg", 10, "2027-01-15", "tag"));
        inventory.sortByExpiryDate();
        assertEquals("MedA", inventory.getMedication(0).getName());
        assertEquals("MedB", inventory.getMedication(1).getName());
        assertEquals("MedC", inventory.getMedication(2).getName());
    }

    @Test
    public void sortByExpiryDate_singleMedication_remainsUnchanged() {
        Inventory inventory = new Inventory();
        inventory.getMedications().add(new Medication("Solo", "5mg", 5, "2026-12-01", "tag"));
        inventory.sortByExpiryDate();
        assertEquals("Solo", inventory.getMedication(0).getName());
        assertEquals(1, inventory.getMedications().size());
    }

    @Test
    public void sortByExpiryDate_invalidDatePlacedAtEnd() {
        Inventory inventory = new Inventory();
        inventory.getMedications().add(new Medication("BadDate", "10mg", 10, "not-a-date", "tag"));
        inventory.getMedications().add(new Medication("GoodDate", "10mg", 10, "2026-05-01", "tag"));
        inventory.sortByExpiryDate();
        assertEquals("GoodDate", inventory.getMedication(0).getName());
        assertEquals("BadDate", inventory.getMedication(1).getName());
    }
}
