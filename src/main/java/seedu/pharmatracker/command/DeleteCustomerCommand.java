package seedu.pharmatracker.command;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

/**
 * Represents a command to remove a customer from the database.
 * The customer to be removed is identified by his 1-based index as shown in the customer list.
 */
public class DeleteCustomerCommand extends Command {

    public static final String COMMAND_WORD = "delete-customer";

    private final String description;

    public DeleteCustomerCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        int index = Integer.parseInt(description);
        int zeroBasedIndex = index - 1;

        Customer customer = customerList.getCustomer(zeroBasedIndex);
        customerList.removeCustomer(customer);
        ui.printDeletedCustomerMessage(customer, customerList);
    }
}
