package seedu.pharmatracker.command;

import seedu.pharmatracker.customer.Customer;
import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Command to find customers by name keyword.
 */
public class FindCustomerCommand extends Command {
    public static final String COMMAND_WORD = "find-customer";
    private static final String MESSAGE_NO_MATCHES = "No customers found matching: ";
    private static final String MESSAGE_MATCHES_FOUND = "Customers matching \"";
    private static final Logger logger = Logger.getLogger(FindCustomerCommand.class.getName());

    private final String keyword;

    /**
     * Constructs a FindCustomerCommand with the given search keyword.
     *
     * @param keyword The name keyword to search for.
     */
    public FindCustomerCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find customer command by searching the customer list
     * for names containing the keyword and displaying the results.
     *
     * @param inventory    The current inventory (unused by this command).
     * @param ui           The UI used to display results or error messages.
     * @param customerList The customer list to search through.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) {
        assert keyword != null : "Search keyword cannot be null";

        if (keyword.isEmpty()) {
            ui.printMessage("Please provide a name to search for.");
            return;
        }

        logger.info("Executing FindCustomerCommand with keyword: " + keyword);

        ArrayList<Customer> matches = customerList.findByName(keyword);

        if (matches.isEmpty()) {
            logger.info("No customers found matching keyword: " + keyword);
            ui.printMessage(MESSAGE_NO_MATCHES + keyword);
            return;
        }

        logger.info("Found " + matches.size() + " customer(s) matching keyword: " + keyword);
        ui.printFindCustomerResults(keyword, matches);
    }
}
