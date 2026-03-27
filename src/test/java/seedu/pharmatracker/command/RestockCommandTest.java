package seedu.pharmatracker.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.logger.LoggerSetup;
import seedu.pharmatracker.ui.Ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestockCommandTest {

    private static final Logger logger = Logger.getLogger(RestockCommandTest.class.getName());

    private Inventory inventory;
    private Ui ui;
    private CustomerList customerList;

    @BeforeEach
    public void setUp() {
        LoggerSetup.init();
        inventory = new Inventory();
        ui = new Ui();
        customerList = new CustomerList();
        inventory.addMedication(new Medication("Paracetamol", "500mg", 130, "2026-12-31", "painkiller"));
        inventory.addMedication(new Medication("Ibuprofen", "200mg", 50, "2027-06-01", "painkiller"));
        inventory.addMedication(new Medication("Amoxicillin", "250mg", 20, "2026-03-01", "antibiotic"));
        logger.log(Level.INFO, "Test setUp complete: inventory loaded with 3 medications");
    }

    @Test
    public void execute_validRestock_stockIncreasesCorrectly() {
        logger.log(Level.INFO, "Test: validRestock on index 1 with qty 50");
        int before = inventory.getMedication(0).getQuantity();
        new RestockCommand(1, 50).execute(inventory, ui, customerList);
        int after = inventory.getMedication(0).getQuantity();
        assert after == before + 50 : "Stock should increase by exactly the restocked quantity";
        assertEquals(180, after);
        logger.log(Level.INFO, "Test passed: stock updated from {0} to {1}", new Object[]{before, after});
    }

    @Test
    public void execute_validRestockThirdMedication_stockIncreasesCorrectly() {
        logger.log(Level.INFO, "Test: validRestock on index 3 (Amoxicillin) with qty 100");
        int before = inventory.getMedication(2).getQuantity();
        new RestockCommand(3, 100).execute(inventory, ui, customerList);
        int after = inventory.getMedication(2).getQuantity();
        assert after == before + 100 : "Amoxicillin stock should increase by 100";
        assertEquals(120, after);
        logger.log(Level.INFO, "Test passed: Amoxicillin stock updated from {0} to {1}",
                new Object[]{before, after});
    }

    @Test
    public void execute_zeroQuantity_stockUnchanged() {
        logger.log(Level.INFO, "Test: zero quantity restock should be rejected");
        int before = inventory.getMedication(0).getQuantity();
        new RestockCommand(1, 0).execute(inventory, ui, customerList);
        int after = inventory.getMedication(0).getQuantity();
        assert before == after : "Stock must remain unchanged when quantity is 0";
        assertEquals(130, after);
        logger.log(Level.INFO, "Test passed: stock unchanged at {0}", after);
    }

    @Test
    public void execute_negativeQuantity_stockUnchanged() {
        logger.log(Level.INFO, "Test: negative quantity restock should be rejected");
        int before = inventory.getMedication(0).getQuantity();
        new RestockCommand(1, -10).execute(inventory, ui, customerList);
        int after = inventory.getMedication(0).getQuantity();
        assert before == after : "Stock must remain unchanged when quantity is negative";
        assertEquals(130, after);
        logger.log(Level.INFO, "Test passed: stock unchanged at {0}", after);
    }

    @Test
    public void execute_indexTooLarge_stockUnchanged() {
        logger.log(Level.INFO, "Test: out-of-bounds index 99 should be rejected");
        new RestockCommand(99, 50).execute(inventory, ui, customerList);
        assertEquals(130, inventory.getMedication(0).getQuantity());
        assertEquals(50, inventory.getMedication(1).getQuantity());
        assertEquals(20, inventory.getMedication(2).getQuantity());
        logger.log(Level.INFO, "Test passed: all stocks unchanged after invalid index");
    }

    @Test
    public void execute_indexZero_stockUnchanged() {
        logger.log(Level.INFO, "Test: index 0 (below valid range) should be rejected");
        int before = inventory.getMedication(0).getQuantity();
        new RestockCommand(0, 50).execute(inventory, ui, customerList);
        int after = inventory.getMedication(0).getQuantity();
        assert before == after : "Stock must remain unchanged when index is 0";
        assertEquals(130, after);
        logger.log(Level.INFO, "Test passed: stock unchanged at {0}", after);
    }

    @Test
    public void execute_indexNegative_stockUnchanged() {
        logger.log(Level.INFO, "Test: negative index should be rejected");
        int before = inventory.getMedication(0).getQuantity();
        new RestockCommand(-1, 50).execute(inventory, ui, customerList);
        int after = inventory.getMedication(0).getQuantity();
        assert before == after : "Stock must remain unchanged when index is negative";
        assertEquals(130, after);
        logger.log(Level.INFO, "Test passed: stock unchanged at {0}", after);
    }

    @Test
    public void execute_restockDoesNotAffectOtherMedications() {
        logger.log(Level.INFO, "Test: restock index 1 should not affect index 2 or 3");
        new RestockCommand(1, 50).execute(inventory, ui, customerList);
        assertEquals(50, inventory.getMedication(1).getQuantity());
        assertEquals(20, inventory.getMedication(2).getQuantity());
        logger.log(Level.INFO, "Test passed: other medications unaffected");
    }

    @Test
    public void execute_multipleRestocks_stockAccumulatesCorrectly() {
        logger.log(Level.INFO, "Test: two consecutive restocks should accumulate");
        new RestockCommand(1, 50).execute(inventory, ui, customerList);
        new RestockCommand(1, 30).execute(inventory, ui, customerList);
        int finalStock = inventory.getMedication(0).getQuantity();
        assert finalStock == 210 : "Stock should be 130 + 50 + 30 = 210";
        assertEquals(210, finalStock);
        logger.log(Level.INFO, "Test passed: accumulated stock is {0}", finalStock);
    }
}
