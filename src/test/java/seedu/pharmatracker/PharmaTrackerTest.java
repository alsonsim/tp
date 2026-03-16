package seedu.pharmatracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.command.FindCommand;
import seedu.pharmatracker.parser.Parser;

public class PharmaTrackerTest {

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

    @Test
    public void medication_getTag_returnsCorrectTag() {
        Medication med = new Medication("Ibuprofen", "200mg", 30, "2027-08-15", "antibiotic");
        assertEquals("antibiotic", med.getTag());
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
    public void findCommand_matchingKeyword_findsMedication() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 50, "2026-06-01", "antibiotic"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 30, "2027-08-15", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("Amox").execute(inventory);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("1 matching medication(s)"));
        assertTrue(output.contains("Amoxicillin"));
    }

    @Test
    public void findCommand_caseInsensitive_findsMedication() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("paracetamol").execute(inventory);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("1 matching medication(s)"));
        assertTrue(output.contains("Paracetamol"));
    }

    @Test
    public void findCommand_noMatch_printsNotFound() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 100, "2026-12-31", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("Aspirin").execute(inventory);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("No medications found matching: Aspirin"));
    }

    @Test
    public void findCommand_multipleMatches_findsAll() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Paracetamol 500mg", "500mg", 100, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Paracetamol Extra", "1000mg", 50, "2027-01-15", "painkiller"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 30, "2027-08-15", "painkiller"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("Paracetamol").execute(inventory);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("2 matching medication(s)"));
        assertTrue(output.contains("Paracetamol 500mg"));
        assertTrue(output.contains("Paracetamol Extra"));
    }

    @Test
    public void findCommand_emptyInventory_printsNotFound() {
        Inventory inventory = new Inventory();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("anything").execute(inventory);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("No medications found matching: anything"));
    }

    @Test
    public void findCommand_partialKeyword_findsMedication() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 50, "2026-06-01", "antibiotic"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new FindCommand("cillin").execute(inventory);
        System.setOut(System.out);

        String output = outContent.toString();
        assertTrue(output.contains("1 matching medication(s)"));
        assertTrue(output.contains("Amoxicillin"));
    }

    @Test
    public void parser_findCommand_returnsCorrectCommandType() {
        Command c = Parser.parse("find Paracetamol");
        assertTrue(c instanceof FindCommand);
    }

    @Test
    public void parser_findCommandNoKeyword_returnsNull() {
        Command c = Parser.parse("find");
        assertEquals(null, c);
    }
}
