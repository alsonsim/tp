package seedu.pharmatracker;

import static seedu.pharmatracker.parser.Parser.parse;

import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.ui.Ui;

public class PharmaTracker {

    private Ui ui;
    private Inventory inventory;

    public PharmaTracker() {
        ui = new Ui();
        inventory = new Inventory();
    }

    public void run() {
        ui.printWelcomeMessage();
        while (true) {
            String fullCommand = ui.readCommand();
            Command c = parse(fullCommand);
            c.execute(inventory);
        }
    }

    /**
     * Main entry-point for the PharmaTracker application.
     */
    public static void main(String[] args) {
        new PharmaTracker().run();
    }
}
