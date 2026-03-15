package seedu.pharmatracker.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;

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

    public void sortByExpiryDate() {
        if (medications.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        medications.sort(Comparator.comparing(med -> {
            try {
                return LocalDate.parse(med.getExpiryDate(), formatter);
            } catch (DateTimeParseException e) {
                return LocalDate.MAX;
            }
        }));
        System.out.println("Medications sorted by expiry date:");
        listMedications();
    }
}
