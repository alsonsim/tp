package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.ui.Ui;
import seedu.pharmatracker.customer.CustomerList;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for {@link ListCommand}.
 *
 * <p>Redirects {@code System.out} before each test and restores it after,
 * allowing assertion of printed output without coupling to the UI layer.
 */
public class ListCommandTest {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream original = System.out;

    /**
     * Redirects {@code System.out} to an in-memory stream before each test.
     */
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(out));
    }

    /**
     * Restores {@code System.out} to its original stream after each test.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(original);
    }

    // -------------------------------------------------------------------------
    // Null / assertion guard tests
    // -------------------------------------------------------------------------

    /**
     * Tests that passing a null inventory throws an AssertionError.
     */
    @Test
    public void execute_nullInventory_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new ListCommand().execute(null, new Ui(), new CustomerList()));
    }

    /**
     * Tests that passing a null Ui throws an AssertionError.
     */
    @Test
    public void execute_nullUi_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new ListCommand().execute(new Inventory(), null, new CustomerList()));
    }

    /**
     * Tests that passing a null CustomerList throws an AssertionError.
     */
    @Test
    public void execute_nullCustomerList_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new ListCommand().execute(new Inventory(), new Ui(), null));
    }

    // -------------------------------------------------------------------------
    // Empty inventory
    // -------------------------------------------------------------------------

    /**
     * Tests that executing {@link ListCommand} on an empty inventory
     * prints the empty-inventory message.
     */
    @Test
    public void execute_emptyInventory_printsEmptyMessage() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();
        new ListCommand().execute(inventory, ui, customerList);
        assertTrue(out.toString().contains("Inventory is empty."));
    }

    /**
     * Tests that the empty-inventory output does not contain any
     * medication-specific fields such as "Dosage" or "Qty".
     */
    @Test
    public void execute_emptyInventory_noMedicationFieldsInOutput() {
        Inventory inventory = new Inventory();
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertFalse(output.contains("Dosage:"));
        assertFalse(output.contains("Qty:"));
        assertFalse(output.contains("Expiry:"));
    }

    // -------------------------------------------------------------------------
    // Single medication
    // -------------------------------------------------------------------------

    /**
     * Tests that executing {@link ListCommand} with a single medication
     * prints all fields: name, dosage, quantity, expiry date, and tag.
     */
    @Test
    public void execute_singleMedication_printsMedicationDetails() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("Aspirin"));
        assertTrue(output.contains("100mg"));
        assertTrue(output.contains("50"));
        assertTrue(output.contains("2027-01-01"));
        assertTrue(output.contains("pain"));
    }

    /**
     * Tests that a single medication entry is prefixed with index "1.".
     */
    @Test
    public void execute_singleMedication_printsIndexOne() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("1."));
    }

    // -------------------------------------------------------------------------
    // Multiple medications
    // -------------------------------------------------------------------------

    /**
     * Tests that executing {@link ListCommand} with multiple medications
     * prints each entry with a 1-based index prefix.
     */
    @Test
    public void execute_multipleMedications_printsAllWithIndex() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 20, "2026-06-01", "fever"));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("1."));
        assertTrue(output.contains("2."));
        assertTrue(output.contains("Aspirin"));
        assertTrue(output.contains("Paracetamol"));
    }

    /**
     * Tests that indexes are sequential and do not skip or repeat
     * when three medications are present.
     */
    @Test
    public void execute_threeMedications_sequentialIndexes() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 20, "2026-06-01", "fever"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 30, "2026-03-01", "pain"));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("1."));
        assertTrue(output.contains("2."));
        assertTrue(output.contains("3."));
        assertFalse(output.contains("4."));
    }

    /**
     * Tests that the total medication count in the footer reflects the
     * exact number of medications added to the inventory.
     */
    @Test
    public void execute_multipleMedications_correctTotalCount() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 20, "2026-06-01", "fever"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 3, "2026-03-01", "pain"));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("Total Medications: 3"));
    }

    // -------------------------------------------------------------------------
    // Header and footer
    // -------------------------------------------------------------------------

    /**
     * Tests that executing {@link ListCommand} on a non-empty inventory
     * prints both the inventory header and the total medication count footer.
     */
    @Test
    public void execute_nonEmptyInventory_printsHeaderAndFooter() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("PharmaTracker Inventory:"));
        assertTrue(output.contains("Total Medications: 1"));
    }

    /**
     * Tests that the empty-inventory path does not print the header
     * "PharmaTracker Inventory:".
     */
    @Test
    public void execute_emptyInventory_noHeaderPrinted() {
        new ListCommand().execute(new Inventory(), new Ui(), new CustomerList());
        assertFalse(out.toString().contains("PharmaTracker Inventory:"));
    }

    /**
     * Tests that the empty-inventory path does not print any "Total Medications" line.
     */
    @Test
    public void execute_emptyInventory_noFooterPrinted() {
        new ListCommand().execute(new Inventory(), new Ui(), new CustomerList());
        assertFalse(out.toString().contains("Total Medications:"));
    }

    // -------------------------------------------------------------------------
    // Tag handling
    // -------------------------------------------------------------------------

    /**
     * Tests that a medication with an empty tag string does not produce
     * a "Tag:" line in the output.
     */
    @Test
    public void execute_medicationWithEmptyTag_noTagInOutput() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", ""));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        assertFalse(out.toString().contains("| Tag:"));
    }

    /**
     * Tests that a medication with a null tag does not produce
     * a "Tag:" line in the output and does not throw.
     */
    @Test
    public void execute_medicationWithNullTag_noTagInOutput() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", null));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        assertFalse(out.toString().contains("| Tag:"));
    }

    /**
     * Tests that a medication with a non-empty tag prints the tag value.
     */
    @Test
    public void execute_medicationWithTag_printsTag() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "painkiller"));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("painkiller"));
    }

    // -------------------------------------------------------------------------
    // Low stock
    // -------------------------------------------------------------------------

    /**
     * Tests that a medication below the low-stock threshold is flagged
     * with {@code [LOW STOCK]} in the output.
     */
    @Test
    public void execute_lowStockMedication_printsLowStockFlag() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 5, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("[LOW STOCK]"));
    }

    /**
     * Tests that a medication above the low-stock threshold does not
     * produce a {@code [LOW STOCK]} flag in the output.
     */
    @Test
    public void execute_normalStockMedication_noLowStockFlag() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        assertFalse(out.toString().contains("[LOW STOCK]"));
    }

    /**
     * Tests that a medication with exactly zero quantity is flagged
     * with {@code [LOW STOCK]}.
     */
    @Test
    public void execute_zeroStockMedication_printsLowStockFlag() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 0, "2027-01-01", "pain"));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        assertTrue(out.toString().contains("[LOW STOCK]"));
    }

    /**
     * Tests that only the low-stock medication is flagged when the inventory
     * contains a mix of normal-stock and low-stock medications.
     */
    @Test
    public void execute_mixedStockLevels_onlyLowStockFlagged() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 3, "2026-06-01", "fever"));
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        String output = out.toString();
        assertTrue(output.contains("[LOW STOCK]"));
        // Only one LOW STOCK flag should appear
        assertEquals(1, countOccurrences(output, "[LOW STOCK]"));
    }

    // -------------------------------------------------------------------------
    // Idempotency
    // -------------------------------------------------------------------------

    /**
     * Tests that calling {@link ListCommand#execute} twice on the same inventory
     * produces identical output both times (command is side-effect free).
     */
    @Test
    public void execute_calledTwice_sameOutputBothTimes() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        ListCommand cmd = new ListCommand();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();

        cmd.execute(inventory, ui, customerList);
        String firstOutput = out.toString();
        out.reset();

        cmd.execute(inventory, ui, customerList);
        String secondOutput = out.toString();

        assertEquals(firstOutput, secondOutput);
    }

    /**
     * Tests that {@link ListCommand} does not modify the inventory size
     * as a side effect of execution.
     */
    @Test
    public void execute_doesNotModifyInventory() {
        Inventory inventory = new Inventory();
        inventory.addMedication(new Medication("Aspirin", "100mg", 50, "2027-01-01", "pain"));
        inventory.addMedication(new Medication("Paracetamol", "500mg", 20, "2026-06-01", "fever"));
        int sizeBefore = inventory.getMedications().size();
        new ListCommand().execute(inventory, new Ui(), new CustomerList());
        assertEquals(sizeBefore, inventory.getMedications().size());
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private int countOccurrences(String text, String target) {
        int count = 0;
        int idx = 0;
        while ((idx = text.indexOf(target, idx)) != -1) {
            count++;
            idx += target.length();
        }
        return count;
    }
}
