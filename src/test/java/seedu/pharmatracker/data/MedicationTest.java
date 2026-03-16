package seedu.pharmatracker.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MedicationTest {

    @Test
    public void medication_getName_returnsCorrectName() {
        Medication med = new Medication("Panadol", "500mg", 100, "2026-12-31", "painkiller");
        assertEquals("Panadol", med.getName());
    }

    @Test
    public void medication_getDosage_returnsCorrectDosage() {
        Medication med = new Medication("Panadol", "500mg", 100, "2026-12-31", "painkiller");
        assertEquals("500mg", med.getDosage());
    }

    @Test
    public void medication_getQuantity_returnsCorrectQuantity() {
        Medication med = new Medication("Panadol", "500mg", 100, "2026-12-31", "painkiller");
        assertEquals(100, med.getQuantity());
    }

    @Test
    public void medication_setQuantity_updatesQuantity() {
        Medication med = new Medication("Panadol", "500mg", 100, "2026-12-31", "painkiller");
        med.setQuantity(50);
        assertEquals(50, med.getQuantity());
    }

    @Test
    public void medication_getExpiryDate_returnsCorrectDate() {
        Medication med = new Medication("Panadol", "500mg", 100, "2026-12-31", "painkiller");
        assertEquals("2026-12-31", med.getExpiryDate());
    }

    @Test
    public void medication_getTag_returnsCorrectTag() {
        Medication med = new Medication("Ibuprofen", "200mg", 30, "2027-08-15", "antibiotic");
        assertEquals("antibiotic", med.getTag());
    }
}
