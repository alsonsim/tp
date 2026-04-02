package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.logger.LoggerSetup;
import seedu.pharmatracker.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestockCommandTest {

    private static final Logger logger = Logger.getLogger(RestockCommandTest.class.getName());

    private Inventory inventory;
    private Ui ui;
    private CustomerList customerList;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream original = System.out;

    @BeforeEach
    public void setUp() {
        LoggerSetup.init();
        System.setOut(new PrintStream(out));
        inventory = new Inventory();
        ui = new Ui();
        customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 130, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 50, "2027-06-01", "painkiller"));
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 20, "2026-03-01", "antibiotic"));
        logger.log(Level.INFO, "Test setUp complete: inventory loaded with 3 medications");
    }

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
                new RestockCommand(1, 50).execute(null, ui, customerList));
    }

    /**
     * Tests that passing a null Ui throws an AssertionError.
     */
    @Test
    public void execute_nullUi_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new RestockCommand(1, 50).execute(inventory, null, customerList));
    }

    /**
     * Tests that passing a null CustomerList throws an AssertionError.
     */
    @Test
    public void execute_nullCustomerList_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new RestockCommand(1, 50).execute(inventory, ui, null));
    }

    // -------------------------------------------------------------------------
    // Valid restock
    // -------------------------------------------------------------------------

    @Test
    public void execute_validRestock_stockIncreasesCorrectly() {
        logger.log(Level.INFO, "Test: validRestock on index 1 with qty 50");
        int before = inventory.getMedication(0).getQuantity();
        new RestockCommand(1, 50).execute(inventory, ui, customerList);
        int after = inventory.getMedication(0).getQuantity();
        assertEquals(before + 50, after);
        assertEquals(180, after);
    }

    @Test
    public void execute_validRestockThirdMedication_stockIncreasesCorrectly() {
        logger.log(Level.INFO, "Test: validRestock on index 3 (Amoxicillin) with qty 100");
        int before = inventory.getMedication(2).getQuantity();
        new RestockCommand(3, 100).execute(inventory, ui, customerList);
        int after = inventory.getMedication(2).getQuantity();
        assertEquals(before + 100, after);
        assertEquals(120, after);
    }

    /**
     * Tests that restocking the last valid index works correctly.
     */
    @Test
    public void execute_validRestockLastIndex_stockIncreasesCorrectly() {
        int before = inventory.getMedication(2).getQuantity();
        new RestockCommand(3, 5).execute(inventory, ui, customerList);
        assertEquals(before + 5, inventory.getMedication(2).getQuantity());
    }

    /**
     * Tests that a restock of exactly 1 unit is accepted as a boundary value.
     */
    @Test
    public void execute_minimumValidQuantity_stockIncreasesByOne() {
        int before = inventory.getMedication(0).getQuantity();
        new RestockCommand(1, 1).execute(inventory, ui, customerList);
        assertEquals(before + 1, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that a successful restock prints a confirmation message.
     */
    @Test
    public void execute_validRestock_printsSuccessMessage() {
        new RestockCommand(1, 50).execute(inventory, ui, customerList);
        assertTrue(out.toString().contains("Paracetamol") || out.toString().contains("restock")
                || out.toString().contains("Restock") || out.toString().contains("success")
                || out.toString().contains("updated") || out.toString().contains("180"));
    }

    // -------------------------------------------------------------------------
    // Invalid quantity
    // -------------------------------------------------------------------------

    @Test
    public void execute_zeroQuantity_stockUnchanged() {
        int before = inventory.getMedication(0).getQuantity();
        new RestockCommand(1, 0).execute(inventory, ui, customerList);
        assertEquals(before, inventory.getMedication(0).getQuantity());
    }

    @Test
    public void execute_negativeQuantity_stockUnchanged() {
        int before = inventory.getMedication(0).getQuantity();
        new RestockCommand(1, -10).execute(inventory, ui, customerList);
        assertEquals(before, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that an invalid quantity prints an error message.
     */
    @Test
    public void execute_zeroQuantity_printsErrorMessage() {
        new RestockCommand(1, 0).execute(inventory, ui, customerList);
        String output = out.toString();
        assertTrue(output.contains("Invalid") || output.contains("invalid")
                || output.contains("least 1") || output.contains("must be"));
    }

    /**
     * Tests that a negative quantity prints an error message.
     */
    @Test
    public void execute_negativeQuantity_printsErrorMessage() {
        new RestockCommand(1, -10).execute(inventory, ui, customerList);
        String output = out.toString();
        assertTrue(output.contains("Invalid") || output.contains("invalid")
                || output.contains("least 1") || output.contains("must be"));
    }

    // -------------------------------------------------------------------------
    // Invalid index
    // -------------------------------------------------------------------------

    @Test
    public void execute_indexTooLarge_stockUnchanged() {
        new RestockCommand(99, 50).execute(inventory, ui, customerList);
        assertEquals(130, inventory.getMedication(0).getQuantity());
        assertEquals(50, inventory.getMedication(1).getQuantity());
        assertEquals(20, inventory.getMedication(2).getQuantity());
    }

    @Test
    public void execute_indexZero_stockUnchanged() {
        int before = inventory.getMedication(0).getQuantity();
        new RestockCommand(0, 50).execute(inventory, ui, customerList);
        assertEquals(before, inventory.getMedication(0).getQuantity());
    }

    @Test
    public void execute_indexNegative_stockUnchanged() {
        int before = inventory.getMedication(0).getQuantity();
        new RestockCommand(-1, 50).execute(inventory, ui, customerList);
        assertEquals(before, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that an out-of-bounds index prints an error message.
     */
    @Test
    public void execute_indexTooLarge_printsErrorMessage() {
        new RestockCommand(99, 50).execute(inventory, ui, customerList);
        String output = out.toString();
        assertTrue(output.contains("Invalid") || output.contains("invalid")
                || output.contains("valid") || output.contains("index"));
    }

    /**
     * Tests that index one above the valid range is also rejected.
     */
    @Test
    public void execute_indexOnePastEnd_stockUnchanged() {
        new RestockCommand(4, 50).execute(inventory, ui, customerList);
        assertEquals(130, inventory.getMedication(0).getQuantity());
        assertEquals(50, inventory.getMedication(1).getQuantity());
        assertEquals(20, inventory.getMedication(2).getQuantity());
    }

    // -------------------------------------------------------------------------
    // Isolation and accumulation
    // -------------------------------------------------------------------------

    @Test
    public void execute_restockDoesNotAffectOtherMedications() {
        new RestockCommand(1, 50).execute(inventory, ui, customerList);
        assertEquals(50, inventory.getMedication(1).getQuantity());
        assertEquals(20, inventory.getMedication(2).getQuantity());
    }

    @Test
    public void execute_multipleRestocks_stockAccumulatesCorrectly() {
        new RestockCommand(1, 50).execute(inventory, ui, customerList);
        new RestockCommand(1, 30).execute(inventory, ui, customerList);
        assertEquals(210, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that restocking different medications independently updates each correctly.
     */
    @Test
    public void execute_restockDifferentMedications_eachUpdatesIndependently() {
        new RestockCommand(1, 10).execute(inventory, ui, customerList);
        new RestockCommand(2, 20).execute(inventory, ui, customerList);
        new RestockCommand(3, 30).execute(inventory, ui, customerList);
        assertEquals(140, inventory.getMedication(0).getQuantity());
        assertEquals(70, inventory.getMedication(1).getQuantity());
        assertEquals(50, inventory.getMedication(2).getQuantity());
    }

    /**
     * Tests that an invalid restock in a sequence does not affect subsequent valid restocks.
     */
    @Test
    public void execute_invalidThenValid_validRestockSucceeds() {
        new RestockCommand(99, 50).execute(inventory, ui, customerList);
        new RestockCommand(1, 20).execute(inventory, ui, customerList);
        assertEquals(150, inventory.getMedication(0).getQuantity());
    }

    /**
     * Tests that a valid restock followed by an invalid one leaves the inventory
     * in the correctly updated state from the valid restock only.
     */
    @Test
    public void execute_validThenInvalid_onlyValidRestockApplied() {
        new RestockCommand(1, 20).execute(inventory, ui, customerList);
        new RestockCommand(0, 50).execute(inventory, ui, customerList);
        assertEquals(150, inventory.getMedication(0).getQuantity());
    }

    // -------------------------------------------------------------------------
    // Empty inventory
    // -------------------------------------------------------------------------

    /**
     * Tests that restocking against an empty inventory prints an error
     * and does not throw.
     */
    @Test
    public void execute_emptyInventory_printsError() {
        Inventory emptyInventory = new Inventory();
        new RestockCommand(1, 50).execute(emptyInventory, ui, customerList);
        String output = out.toString();
        assertTrue(output.contains("Invalid") || output.contains("empty")
                || output.contains("valid") || output.contains("index"));
    }
}
