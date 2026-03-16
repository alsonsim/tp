package seedu.pharmatracker.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.pharmatracker.command.Command;
import seedu.pharmatracker.command.FindCommand;

public class ParserTest {


    @Test
    public void parser_findCommand_returnsCorrectCommandType() {
        Command c = Parser.parse("find Paracetamol");
        assertTrue(c instanceof FindCommand);
    }

    @Test
    public void parser_findCommandNoKeyword_returnsNull() {
        Command c = Parser.parse("find");
        assertEquals(null, c);
    }
}
