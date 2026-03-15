package seedu.pharmatracker.parser;

import seedu.pharmatracker.data.Inventory;

public class Parser {
    private Inventory inventory;

    public Parser(Inventory inventory) {
        this.inventory = inventory;
    }

    public void parseCommand(String userInput) {
        String[] inputParts = userInput.trim().split("\\s+", 2);
        String commandWord = inputParts[0].toLowerCase();

        switch (commandWord) {
        case "add":
            System.out.println("Add command triggered.");
            break;
        case "delete":
            System.out.println("Delete command triggered.");
            break;
        case "dispense":
            System.out.println("Dispense command triggered.");
            break;
        case "list":
            inventory.listMedications();
            break;
        case "find":
            System.out.println("Find command triggered.");
            break;
        case "view":
            System.out.println("View command triggered.");
            break;
        case "sort":
            inventory.sortByExpiryDate();
            break;
        case "label":
            System.out.println("Label command triggered.");
            break;
        case "help":
            System.out.println("Help command triggered.");
            break;
        case "exit":
            System.out.println("Exit command triggered.");
            System.exit(0);
            break;
        default:
            System.out.println("Unknown command! Please type 'help' to see the list of available commands.");
        }
    }
}
