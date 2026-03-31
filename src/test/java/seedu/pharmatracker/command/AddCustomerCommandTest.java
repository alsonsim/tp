package seedu.pharmatracker.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

public class AddCustomerCommandTest {

    @Test
    void constructor_validInputs_success() {
        assertDoesNotThrow(() -> new AddCustomerCommand("C001", "John Doe", "91234567", "123 Clementi Road"));
    }

    @Test
    void constructor_nullId_throwsAssertionError() {
        assertThrows(AssertionError.class,
                () -> new AddCustomerCommand(null, "John Doe", "91234567", "123 Clementi Road"));
    }

    @Test
    void constructor_emptyId_throwsAssertionError() {
        assertThrows(AssertionError.class,
                () -> new AddCustomerCommand("", "John Doe", "91234567", "123 Clementi Road"));
    }

    @Test
    void constructor_nullName_throwsAssertionError() {
        assertThrows(AssertionError.class,
                () -> new AddCustomerCommand("C001", null, "91234567", "123 Clementi Road"));
    }

    @Test
    void constructor_emptyName_throwsAssertionError() {
        assertThrows(AssertionError.class,
                () -> new AddCustomerCommand("C001", "", "91234567", "123 Clementi Road"));
    }

    @Test
    void constructor_nullPhone_throwsAssertionError() {
        assertThrows(AssertionError.class,
                () -> new AddCustomerCommand("C001", "John Doe", null, "123 Clementi Road"));
    }

    @Test
    void constructor_emptyPhone_throwsAssertionError() {
        assertThrows(AssertionError.class,
                () -> new AddCustomerCommand("C001", "John Doe", "", "123 Clementi Road"));
    }

    @Test
    void constructor_nullAddress_throwsAssertionError() {
        assertThrows(AssertionError.class,
                () -> new AddCustomerCommand("C001", "John Doe", "91234567", null));
    }

    @Test
    void execute_validParameters_customerAddedToCustomerList() {
        // Setup
        AddCustomerCommand command = new AddCustomerCommand("C001", "John Doe", "91234567", "123 Clementi Road");
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        CustomerList customerList = new CustomerList();

        command.execute(inventory, ui, customerList);

        assertEquals(1, customerList.getCustomerCount());

        Customer addedCustomer = customerList.getCustomer(0);
        assertEquals("C001", addedCustomer.getCustomerId());
        assertEquals("John Doe", addedCustomer.getName());
        assertEquals("91234567", addedCustomer.getPhone());
        assertEquals("123 Clementi Road", addedCustomer.getAddress());
    }

    @Test
    void execute_nullUi_throwsAssertionError() {
        AddCustomerCommand command = new AddCustomerCommand("C001", "John Doe", "91234567", "123 Clementi Road");
        Inventory inventory = new Inventory();
        CustomerList customerList = new CustomerList();

        assertThrows(AssertionError.class,
                () -> command.execute(inventory, null, customerList));
    }

    @Test
    void execute_nullCustomerList_throwsAssertionError() {
        AddCustomerCommand command = new AddCustomerCommand("C001", "John Doe", "91234567", "123 Clementi Road");
        Inventory inventory = new Inventory();
        Ui ui = new Ui();

        assertThrows(AssertionError.class,
                () -> command.execute(inventory, ui, null));
    }
}
