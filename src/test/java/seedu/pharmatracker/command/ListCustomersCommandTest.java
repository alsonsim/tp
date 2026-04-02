package seedu.pharmatracker.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for {@link ListCustomersCommand}.
 *
 * <p>Redirects {@code System.out} before each test and restores it after,
 * allowing assertion of printed output without coupling to the UI layer.
 */
public class ListCustomersCommandTest {
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
                new ListCustomersCommand().execute(null, new Ui(), new CustomerList()));
    }

    /**
     * Tests that passing a null Ui throws an AssertionError.
     */
    @Test
    public void execute_nullUi_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new ListCustomersCommand().execute(new Inventory(), null, new CustomerList()));
    }

    /**
     * Tests that passing a null CustomerList throws an AssertionError.
     */
    @Test
    public void execute_nullCustomerList_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new ListCustomersCommand().execute(new Inventory(), new Ui(), null));
    }

    // -------------------------------------------------------------------------
    // Empty customer list
    // -------------------------------------------------------------------------

    /**
     * Tests that executing {@link ListCustomersCommand} on an empty customer list
     * prints the no-customers message.
     */
    @Test
    public void execute_emptyCustomerList_printsEmptyMessage() {
        new ListCustomersCommand().execute(new Inventory(), new Ui(), new CustomerList());
        assertTrue(out.toString().contains("No customers registered yet."));
    }

    /**
     * Tests that the empty-customer-list path does not print the header
     * "PharmaTracker Customers:".
     */
    @Test
    public void execute_emptyCustomerList_noHeaderPrinted() {
        new ListCustomersCommand().execute(new Inventory(), new Ui(), new CustomerList());
        assertFalse(out.toString().contains("PharmaTracker Customers:"));
    }

    /**
     * Tests that the empty-customer-list path does not print any "Total Customers" line.
     */
    @Test
    public void execute_emptyCustomerList_noFooterPrinted() {
        new ListCustomersCommand().execute(new Inventory(), new Ui(), new CustomerList());
        assertFalse(out.toString().contains("Total Customers:"));
    }

    /**
     * Tests that the empty-customer-list output contains no customer-specific
     * fields such as a phone number or customer ID prefix.
     */
    @Test
    public void execute_emptyCustomerList_noCustomerFieldsInOutput() {
        new ListCustomersCommand().execute(new Inventory(), new Ui(), new CustomerList());
        String output = out.toString();
        assertFalse(output.contains("Phone:"));
        assertFalse(output.contains("ID:"));
    }

    // -------------------------------------------------------------------------
    // Single customer
    // -------------------------------------------------------------------------

    /**
     * Tests that executing {@link ListCustomersCommand} with a single customer
     * prints their ID, name, and phone number.
     */
    @Test
    public void execute_singleCustomer_printsCustomerDetails() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ListCustomersCommand().execute(new Inventory(), new Ui(), customerList);
        String output = out.toString();
        assertTrue(output.contains("C001"));
        assertTrue(output.contains("John Tan"));
        assertTrue(output.contains("99887766"));
    }

    /**
     * Tests that a single customer entry is prefixed with index "1.".
     */
    @Test
    public void execute_singleCustomer_printsIndexOne() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ListCustomersCommand().execute(new Inventory(), new Ui(), customerList);
        assertTrue(out.toString().contains("1."));
    }

    /**
     * Tests that a customer with a non-empty address has the address printed.
     */
    @Test
    public void execute_customerWithAddress_printsAddress() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", "123 Main St"));
        new ListCustomersCommand().execute(new Inventory(), new Ui(), customerList);
        assertTrue(out.toString().contains("123 Main St"));
    }

    /**
     * Tests that a customer with an empty address does not produce an
     * "Address:" line in the output.
     */
    @Test
    public void execute_customerWithEmptyAddress_noAddressInOutput() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ListCustomersCommand().execute(new Inventory(), new Ui(), customerList);
        assertFalse(out.toString().contains("Address:"));
    }

    /**
     * Tests that a customer with a null address does not produce an
     * "Address:" line and does not throw.
     */
    @Test
    public void execute_customerWithNullAddress_noAddressInOutput() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", null));
        new ListCustomersCommand().execute(new Inventory(), new Ui(), customerList);
        assertFalse(out.toString().contains("Address:"));
    }

    // -------------------------------------------------------------------------
    // Multiple customers
    // -------------------------------------------------------------------------

    /**
     * Tests that executing {@link ListCustomersCommand} with multiple customers
     * prints each entry with a 1-based index prefix.
     */
    @Test
    public void execute_multipleCustomers_printsAllWithIndex() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        customerList.addCustomer(new Customer("C002", "Mary Tan", "87654321", ""));
        customerList.addCustomer(new Customer("C003", "David Ng", "93456789", ""));
        new ListCustomersCommand().execute(new Inventory(), new Ui(), customerList);
        String output = out.toString();
        assertTrue(output.contains("1."));
        assertTrue(output.contains("2."));
        assertTrue(output.contains("3."));
        assertTrue(output.contains("John Tan"));
        assertTrue(output.contains("Mary Tan"));
        assertTrue(output.contains("David Ng"));
    }

    /**
     * Tests that indexes are sequential and do not exceed the customer count.
     */
    @Test
    public void execute_threeCustomers_noFourthIndex() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        customerList.addCustomer(new Customer("C002", "Mary Tan", "87654321", ""));
        customerList.addCustomer(new Customer("C003", "David Ng", "93456789", ""));
        new ListCustomersCommand().execute(new Inventory(), new Ui(), customerList);
        assertFalse(out.toString().contains("4."));
    }

    /**
     * Tests that all phone numbers are printed when multiple customers are listed.
     */
    @Test
    public void execute_multipleCustomers_printsAllPhoneNumbers() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        customerList.addCustomer(new Customer("C002", "Mary Tan", "87654321", ""));
        new ListCustomersCommand().execute(new Inventory(), new Ui(), customerList);
        String output = out.toString();
        assertTrue(output.contains("99887766"));
        assertTrue(output.contains("87654321"));
    }

    // -------------------------------------------------------------------------
    // Header and footer
    // -------------------------------------------------------------------------

    /**
     * Tests that executing {@link ListCustomersCommand} on a non-empty list
     * prints both the header and the total customer count footer.
     */
    @Test
    public void execute_nonEmptyCustomerList_printsHeaderAndFooter() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        new ListCustomersCommand().execute(new Inventory(), new Ui(), customerList);
        String output = out.toString();
        assertTrue(output.contains("PharmaTracker Customers:"));
        assertTrue(output.contains("Total Customers: 1"));
    }

    /**
     * Tests that the total customer count in the footer reflects the
     * exact number of customers added to the list.
     */
    @Test
    public void execute_multipleCustomers_correctTotalCount() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        customerList.addCustomer(new Customer("C002", "Mary Tan", "87654321", ""));
        customerList.addCustomer(new Customer("C003", "David Ng", "93456789", ""));
        new ListCustomersCommand().execute(new Inventory(), new Ui(), customerList);
        assertTrue(out.toString().contains("Total Customers: 3"));
    }

    // -------------------------------------------------------------------------
    // Idempotency
    // -------------------------------------------------------------------------

    /**
     * Tests that calling {@link ListCustomersCommand#execute} twice on the same
     * customer list produces identical output both times (command is side-effect free).
     */
    @Test
    public void execute_calledTwice_sameOutputBothTimes() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        ListCustomersCommand cmd = new ListCustomersCommand();
        Inventory inventory = new Inventory();
        Ui ui = new Ui();

        cmd.execute(inventory, ui, customerList);
        String firstOutput = out.toString();
        out.reset();

        cmd.execute(inventory, ui, customerList);
        String secondOutput = out.toString();

        assertEquals(firstOutput, secondOutput);
    }

    /**
     * Tests that {@link ListCustomersCommand} does not modify the customer list
     * size as a side effect of execution.
     */
    @Test
    public void execute_doesNotModifyCustomerList() {
        CustomerList customerList = new CustomerList();
        customerList.addCustomer(new Customer("C001", "John Tan", "99887766", ""));
        customerList.addCustomer(new Customer("C002", "Mary Tan", "87654321", ""));
        int sizeBefore = customerList.size();
        new ListCustomersCommand().execute(new Inventory(), new Ui(), customerList);
        assertEquals(sizeBefore, customerList.size());
    }

    /**
     * Tests that customers from a previous test run do not bleed into the
     * next test — each test uses its own fresh CustomerList instance.
     */
    @Test
    public void execute_freshCustomerList_noStaleData() {
        CustomerList freshList = new CustomerList();
        new ListCustomersCommand().execute(new Inventory(), new Ui(), freshList);
        assertFalse(out.toString().contains("John Tan"));
        assertFalse(out.toString().contains("Mary Tan"));
    }
}
