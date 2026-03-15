package seedu.pharmatracker.data;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Medication> medications;

    public Inventory() {
        this.medications = new ArrayList<>();
    }

    public ArrayList<Medication> getMedications() {
        return this.medications;
    }

    public Medication getMedication(int index) {
        return this.medications.get(index);
    }

    public void listMedications() {
        if (medications.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        for (int i = 0; i < medications.size(); i++) {
            Medication med = medications.get(i);
            System.out.println((i + 1) + ". " + med.getName()
                    + " | Dosage: " + med.getDosage()
                    + " | Qty: " + med.getQuantity()
                    + " | Expiry: " + med.getExpiryDate()
                    + " | Tag: " + med.getTag());
        }
    }
}
