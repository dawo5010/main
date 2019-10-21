package seedu.address.reimbursement.logic;

import java.io.IOException;

import seedu.address.reimbursement.commands.Command;
import seedu.address.reimbursement.commands.CommandResult;
import seedu.address.reimbursement.model.Model;
import seedu.address.reimbursement.model.ReimbursementList;

/**
 * Implements the logic for Reimbursements.
 */
public class LogicManager implements Logic {

    private final seedu.address.reimbursement.model.Model reimbursementModel;
    private final seedu.address.reimbursement.storage.StorageManager reimbursementStorage;
    private final seedu.address.person.model.Model personModel;
    private final seedu.address.transaction.model.Model transactionModel;
    private final seedu.address.transaction.storage.StorageManager transactionStorage;
    private final ReimbursementTabParser parser;


    public LogicManager(Model reimbursementModel,
                        seedu.address.reimbursement.storage.StorageManager reimbursementStorage,
                        seedu.address.transaction.model.Model transactionModel,
                        seedu.address.transaction.storage.StorageManager transactionStorage,
                        seedu.address.person.model.Model personModel) {

        this.reimbursementModel = reimbursementModel;
        this.reimbursementStorage = reimbursementStorage;

        this.parser = new ReimbursementTabParser();

        this.personModel = personModel;

        this.transactionModel = transactionModel;
        this.transactionStorage = transactionStorage;
    }

    @Override
    public CommandResult execute(String commandText) throws Exception {
        Command command = parser.parseCommand(commandText, personModel);
        CommandResult commandResult = command.execute(reimbursementModel, personModel);
        transactionStorage.writeFile(transactionModel.getTransactionList());
        reimbursementStorage.writeFile(reimbursementModel.getReimbursementList());
        return commandResult;
    }

    @Override
    public ReimbursementList getFilteredList() {
        return reimbursementModel.getFilteredReimbursementList();
    }

    @Override
    public void updateReimbursementList() throws IOException {
        reimbursementModel.updateReimbursementList(reimbursementStorage.readReimbursementList());
        reimbursementStorage.writeFile(reimbursementModel.getReimbursementList());
    }
}
