package seedu.address.cashier.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.cashier.ui.CashierMessages.CLEARED_SUCCESSFULLY;
import static seedu.address.cashier.ui.CashierMessages.NO_SUCH_COMMAND;
import static seedu.address.cashier.ui.CashierMessages.NO_SUCH_INDEX_CASHIER;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import seedu.address.cashier.logic.commands.ClearCommand;
import seedu.address.cashier.logic.commands.CommandResult;
import seedu.address.cashier.model.Model;
import seedu.address.cashier.model.ModelManager;
import seedu.address.cashier.storage.Storage;
import seedu.address.cashier.storage.StorageManager;
import seedu.address.person.model.UserPrefs;
import seedu.address.testutil.TypicalItem;
import seedu.address.testutil.TypicalTransactions;

public class LogicManagerTest {
    private File iFile;
    private File tFile;
    private Model model;
    private seedu.address.person.model.Model personModel;
    private Storage storage;
    //private seedu.address.transaction.storage.Storage transactionStorage;
    private seedu.address.transaction.model.Model transactionModel;
    private seedu.address.inventory.model.Model inventoryModel;
    private Logic logic;

    LogicManagerTest() throws Exception {
        try {
            model = new ModelManager(TypicalItem.getTypicalInventoryList(),
                    TypicalTransactions.getTypicalTransactionList());
            personModel = new seedu.address.person.model.ModelManager(getTypicalAddressBook(), new UserPrefs());
            iFile = File.createTempFile("testing", "tempInventory.txt");
            tFile = File.createTempFile("testing", "tempTransaction.txt");
            storage = new StorageManager(iFile, tFile, personModel);

            /*model.getTransactionList();
            transactionStorage =
                    new seedu.address.transaction.storage.StorageManager(tFile, personModel);*/
            transactionModel =
                    new seedu.address.transaction.model.ModelManager(storage.getTransactionList());
            inventoryModel =
                    new seedu.address.inventory.model.ModelManager(
                            new seedu.address.inventory.storage.StorageManager(iFile).getInventoryList());
            logic =
                    new LogicManager(model, storage, personModel, transactionModel, inventoryModel);
        } catch (IOException e) {
            throw new AssertionError("This method should not throw an exception.");
        }

    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "yufkvghkk";
        assertParseException(invalidCommand, NO_SUCH_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 44";
        assertCommandException(deleteCommand, NO_SUCH_INDEX_CASHIER);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ClearCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, CLEARED_SUCCESSFULLY, model);
    }



    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      Model expectedModel) {
        try {
            CommandResult result = logic.execute(inputCommand);
            assertEquals(expectedMessage, result.getFeedbackToUser());
            assertEquals(expectedModel, model);
        } catch (Exception e) {
            throw new AssertionError("There should not be an error");
        }

    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, Exception.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, seedu.address.cashier.model.Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, Exception.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage) {
        Model expectedModel = new seedu.address.cashier.model.ModelManager(
                model.getInventoryList(), model.getTransactionList());
        System.out.println("expected msg: " + expectedMessage);
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel.getInventoryList(), model.getInventoryList());
        assertEquals(expectedModel.getSalesList(), model.getSalesList());
        //assertEquals(expectedModel, model);
    }


}
