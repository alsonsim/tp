package seedu.pharmatracker.alert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

public class RestockAlertServiceTest {

    @Test
    public void evaluateInventory_belowThreshold_generatesActiveAlert() {
        Inventory inventory = new Inventory();
        Medication med = new Medication("Ibuprofen", "200mg", 5, "2027-01-01", "painkiller");
        med.setMinimumStockThreshold(10);
        inventory.addMedication(med);

        RestockAlertService service = new RestockAlertService(null);
        service.evaluateInventory(inventory);

        assertEquals(1, service.getActiveAlerts().size());
        assertEquals(1, service.getAlertHistory().size());
        assertEquals("Ibuprofen", service.getActiveAlerts().get(0).getMedicationName());
    }

    @Test
    public void acknowledgeActiveAlert_validIndex_acknowledgesAndRemovesFromActive() {
        Inventory inventory = new Inventory();
        Medication med = new Medication("Ibuprofen", "200mg", 5, "2027-01-01", "painkiller");
        med.setMinimumStockThreshold(10);
        inventory.addMedication(med);

        RestockAlertService service = new RestockAlertService(null);
        service.evaluateInventory(inventory);

        RestockAlert acknowledged = service.acknowledgeActiveAlert(1);
        assertNotNull(acknowledged);
        assertTrue(acknowledged.isAcknowledged());
        assertEquals(0, service.getActiveAlerts().size());
        assertEquals(1, service.getAlertHistory().size());
    }

    @Test
    public void evaluateInventory_stockRecovers_autoResolvesActiveAlert() {
        Inventory inventory = new Inventory();
        Medication med = new Medication("Ibuprofen", "200mg", 5, "2027-01-01", "painkiller");
        med.setMinimumStockThreshold(10);
        inventory.addMedication(med);

        RestockAlertService service = new RestockAlertService(null);
        service.evaluateInventory(inventory);

        med.setQuantity(15);
        service.evaluateInventory(inventory);

        assertEquals(0, service.getActiveAlerts().size());
        assertTrue(service.getAlertHistory().get(0).isAcknowledged());
    }

    @Test
    public void evaluateInventory_medicationDeleted_autoResolvesOrphanedAlert() {
        Inventory inventory = new Inventory();
        Medication med = new Medication("Ibuprofen", "200mg", 5, "2027-01-01", "painkiller");
        med.setMinimumStockThreshold(10);
        inventory.addMedication(med);

        RestockAlertService service = new RestockAlertService(null);
        service.evaluateInventory(inventory);

        // Delete medication and re-evaluate to trigger orphaned-alert cleanup.
        inventory.removeMedication(med);
        service.evaluateInventory(inventory);

        assertEquals(0, service.getActiveAlerts().size());
        assertEquals(1, service.getAlertHistory().size());
        assertTrue(service.getAlertHistory().get(0).isAcknowledged());
        assertEquals("Auto-resolved: medication deleted from inventory.",
                service.getAlertHistory().get(0).getAcknowledgmentNote());
    }
}
