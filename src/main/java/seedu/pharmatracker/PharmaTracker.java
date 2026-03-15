package seedu.pharmatracker;

import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.parser.Parser;
import seedu.pharmatracker.ui.Ui;

import java.util.logging.Logger;
import java.util.logging.Level;

public class PharmaTracker {
    private static final Logger logger = Logger.getLogger(PharmaTracker.class.getName());

    private Ui ui;
    private Parser parser;
    private Inventory inventory;

    public PharmaTracker() {
        inventory = new Inventory();
        ui = new Ui();
        parser = new Parser(inventory);
    }

    public void run() {
        assert ui != null : "UI should not be null";
        assert parser != null : "Parser should not be null";
        assert inventory != null : "Inventory should not be null";
        logger.log(Level.INFO, "PharmaTracker starting up");
        ui.printWelcomeMessage();
        while (true) {
            String fullCommand = ui.readCommand();
            logger.log(Level.INFO, "Command received: " + fullCommand);
            parser.parseCommand(fullCommand);
        }
    }

    public static void main(String[] args) {
        new PharmaTracker().run();
    }
}
