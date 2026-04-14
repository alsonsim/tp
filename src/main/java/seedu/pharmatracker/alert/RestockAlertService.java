package seedu.pharmatracker.alert;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;

/**
 * Generates and tracks automatic restock alerts based on per-medication thresholds.
 */
public class RestockAlertService {
    private final Map<String, RestockAlert> activeAlertsByMedicationKey;
    private final ArrayList<RestockAlert> alertHistory;

    /**
     * Constructs an alert service with persisted history.
     *
     * @param persistedHistory Alert history from storage.
     */
    public RestockAlertService(List<RestockAlert> persistedHistory) {
        this.activeAlertsByMedicationKey = new LinkedHashMap<>();
        this.alertHistory = new ArrayList<>();

        if (persistedHistory != null) {
            this.alertHistory.addAll(persistedHistory);
            for (RestockAlert alert : persistedHistory) {
                if (!alert.isAcknowledged()) {
                    activeAlertsByMedicationKey.put(alert.getMedicationKey(), alert);
                }
            }
        }
    }

    /**
     * Scans inventory against configured thresholds and updates active alerts.
     *
     * This method performs two operations:
     * 1. Updates active alerts based on current medication quantities:
     *    - Creates new alerts for medications below threshold
     *    - Updates existing alerts with current stock and threshold values
     *    - Auto-resolves alerts when stock recovers above threshold
     * 2. Cleans up orphaned alerts:
     *    - Removes alerts for medications that have been deleted from inventory
     *    - Marks removed alerts as acknowledged with auto-resolution reason
     *
     * @param inventory Current inventory. If null, method returns without performing any updates.
     */
    public void evaluateInventory(Inventory inventory) {
        if (inventory == null) {
            return;
        }

        for (Medication medication : inventory.getMedications()) {
            String key = buildMedicationKey(medication);
            int quantity = medication.getQuantity();
            int threshold = medication.getMinimumStockThreshold();

            if (quantity < threshold) {
                RestockAlert activeAlert = activeAlertsByMedicationKey.get(key);
                if (activeAlert == null) {
                    RestockAlert newAlert = new RestockAlert(key, medication.getName(), quantity, threshold);
                    activeAlertsByMedicationKey.put(key, newAlert);
                    alertHistory.add(newAlert);
                } else {
                    activeAlert.updateCurrentStock(quantity);
                    activeAlert.updateThreshold(threshold);
                }
            } else {
                RestockAlert activeAlert = activeAlertsByMedicationKey.get(key);
                if (activeAlert != null && !activeAlert.isAcknowledged()) {
                    activeAlert.acknowledge("Auto-resolved: stock recovered above threshold.");
                    activeAlertsByMedicationKey.remove(key);
                }
            }
        }

        // Clean up active alerts for medications that no longer exist in inventory
        cleanupOrphanedAlerts(inventory);
    }

    /**
     * Removes alerts for medications that have been deleted from the inventory.
     *
     * Iterates through all active alerts and checks if the corresponding medication
     * still exists in the current inventory. If a medication is not found, its alert
     * is automatically removed from the active alerts map and marked as acknowledged
     * with the reason "Auto-resolved: medication deleted from inventory."
     *
     * This ensures that when users delete medications, any associated restock alerts
     * are automatically cleaned up and do not appear in the active alerts list.
     *
     * @param inventory The current inventory to verify medication existence against.
     */
    private void cleanupOrphanedAlerts(Inventory inventory) {
        ArrayList<String> medicationKeysToRemove = new ArrayList<>();
        
        // Identify keys that correspond to medications no longer in inventory
        for (String medicationKey : activeAlertsByMedicationKey.keySet()) {
            boolean medicationExists = false;
            for (Medication medication : inventory.getMedications()) {
                if (buildMedicationKey(medication).equals(medicationKey)) {
                    medicationExists = true;
                    break;
                }
            }
            if (!medicationExists) {
                medicationKeysToRemove.add(medicationKey);
            }
        }

        // Remove alerts for deleted medications and mark as auto-resolved
        for (String medicationKey : medicationKeysToRemove) {
            RestockAlert alert = activeAlertsByMedicationKey.remove(medicationKey);
            if (alert != null && !alert.isAcknowledged()) {
                alert.acknowledge("Auto-resolved: medication deleted from inventory.");
            }
        }
    }

    /**
     * Acknowledges an active alert by one-based display index.
     *
     * @param index One-based index.
     * @return Acknowledged alert or null if index invalid.
     */
    public RestockAlert acknowledgeActiveAlert(int index) {
        ArrayList<RestockAlert> active = getActiveAlerts();
        if (index < 1 || index > active.size()) {
            return null;
        }
        RestockAlert selected = active.get(index - 1);
        selected.acknowledge("Acknowledged by user.");
        activeAlertsByMedicationKey.remove(selected.getMedicationKey());
        return selected;
    }

    /**
     * Returns current active alerts.
     *
     * @return List of active alerts in display order.
     */
    public ArrayList<RestockAlert> getActiveAlerts() {
        return new ArrayList<>(activeAlertsByMedicationKey.values());
    }

    /**
     * Returns full alert history.
     *
     * @return Persistable history list.
     */
    public ArrayList<RestockAlert> getAlertHistory() {
        return new ArrayList<>(alertHistory);
    }

    private String buildMedicationKey(Medication medication) {
        return medication.getName() + "::" + medication.getDosage();
    }
}
