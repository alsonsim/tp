package seedu.pharmatracker.command;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.pharmatracker.customer.CustomerList;
import seedu.pharmatracker.data.Inventory;
import seedu.pharmatracker.data.Medication;
import seedu.pharmatracker.exceptions.PharmaTrackerException;
import seedu.pharmatracker.ui.Ui;

/**
 * Updates one or more fields of an existing medication record in the inventory.
 * Only the fields provided will be changed. All others remain unchanged.
 */
public class UpdateCommand extends Command {

    public static final String COMMAND_WORD = "update";

    private static final Logger logger = Logger.getLogger(UpdateCommand.class.getName());

    private final int index;
    private final String name;
    private final String dosage;
    private final Integer quantity;
    private final String expiryDate;
    private final String tag;
    private final String dosageForm;
    private final String manufacturer;
    private final String directions;
    private final String frequency;
    private final String route;
    private final String maxDailyDose;
    private final ArrayList<String> warnings;

    /**
     * Constructs an UpdateCommand with the specified fields to update.
     * Any field that is not being updated should be passed as {@code null}.
     *
     * @param index        The 1-based index of the medication to update in the inventory.
     * @param name         The new name of the medication, or {@code null} to leave unchanged.
     * @param dosage       The new dosage of the medication, or {@code null} to leave unchanged.
     * @param quantity     The new quantity of the medication, or {@code null} to leave unchanged.
     * @param expiryDate   The new expiry date of the medication, or {@code null} to leave unchanged.
     * @param tag          The new tag for the medication, or {@code null} to leave unchanged.
     * @param dosageForm   The new dosage form, or {@code null} to leave unchanged.
     * @param manufacturer The new manufacturer, or {@code null} to leave unchanged.
     * @param directions   The new directions for use, or {@code null} to leave unchanged.
     * @param frequency    The new frequency of intake, or {@code null} to leave unchanged.
     * @param route        The new route of administration, or {@code null} to leave unchanged.
     * @param maxDailyDose The new maximum daily dose, or {@code null} to leave unchanged.
     * @param warnings     The new list of warnings, or {@code null} to leave unchanged.
     */
    public UpdateCommand(int index, String name, String dosage, Integer quantity, String expiryDate, String tag,
                         String dosageForm, String manufacturer, String directions, String frequency, String route,
                         String maxDailyDose, ArrayList<String> warnings) {
        this.index = index;
        this.name = name;
        this.dosage = dosage;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.tag = tag;
        this.dosageForm = dosageForm;
        this.manufacturer = manufacturer;
        this.directions = directions;
        this.frequency = frequency;
        this.route = route;
        this.maxDailyDose = maxDailyDose;
        this.warnings = warnings;
    }

    /**
     * Executes the update command by retrieving the medication at the specified index
     * and applying the provided updates to its fields. Prints a confirmation message
     * summarizing the changes made.
     *
     * @param inventory    The current inventory containing all stored medications.
     * @param ui           The user interface used to display messages and interact with the user.
     * @param customerList The list of registered customers in the system.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, CustomerList customerList) throws PharmaTrackerException {
        assert inventory != null : "Inventory cannot be null in UpdateCommand execution.";
        assert ui != null : "Ui cannot be null in UpdateCommand execution.";

        logger.log(Level.INFO, "Starting execution of UpdateCommand for index:" + index);

        if (inventory.getMedications().isEmpty()) {
            throw new PharmaTrackerException("Inventory is empty.");
        }

        if (index < 1 || index > inventory.getMedications().size()) {
            throw new PharmaTrackerException("Invalid index. Please enter a number between 1 and "
                    + inventory.getMedications().size() + ".");
        }

        Medication med = inventory.getMedication(index - 1);
        ArrayList<String> changes = new ArrayList<>();

        if (name != null) {
            med.setName(name);
            changes.add("Name updated to " + name);
        }

        if (dosage != null) {
            med.setDosage(dosage);
            changes.add("Dosage updated to " + dosage);
        }

        if (quantity != null) {
            med.setQuantity(quantity);
            changes.add("Quantity updated to " + quantity);
        }

        if (expiryDate != null) {
            med.setExpiryDate(expiryDate);
            changes.add("Expiry updated to " + expiryDate);
        }

        if (tag != null) {
            med.setTag(tag);
            changes.add("Tag updated to " + tag);
        }

        if (dosageForm != null) {
            med.setDosageForm(dosageForm);
            changes.add("Dosage Form updated to " + dosageForm);
        }

        if (manufacturer != null) {
            med.setManufacturer(manufacturer);
            changes.add("Manufacturer updated to " + manufacturer);
        }

        if (directions != null) {
            med.setDirections(directions);
            changes.add("Directions updated to " + directions);
        }

        if (frequency != null) {
            med.setFrequency(frequency);
            changes.add("Frequency updated to " + frequency);
        }

        if (route != null) {
            med.setRoute(route);
            changes.add("Route updated to " + route);
        }

        if (maxDailyDose != null) {
            med.setMaxDailyDose(maxDailyDose);
            changes.add("Max Daily Dose updated to " + maxDailyDose);
        }

        if (warnings != null && !warnings.isEmpty()) {
            med.getWarnings().clear(); // Clear old warnings and replace with new ones
            for (String warning : warnings) {
                med.addWarning(warning);
            }
            changes.add("Warnings updated");
        }

        ui.printUpdatedMedicationMessage(med, changes);
    }
}
