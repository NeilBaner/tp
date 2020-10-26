package seedu.duke.userinterface;

import seedu.duke.exceptions.CorruptFileException;
import seedu.duke.exceptions.InvalidUserInputException;
import seedu.duke.exceptions.ZeroNoteException;
import seedu.duke.storage.Storage;
import seedu.duke.userinterface.command.CliCommand;
import seedu.duke.userinterface.command.Exit;

import java.io.IOException;
import java.util.Scanner;

public class CliUserInterface {
    private AppState appState;

    private boolean toQuit = false;
    private PersonalMesssageGenerator msgGenerator;
    private CliMessages messages;

    public CliUserInterface() {
        msgGenerator = new PersonalMesssageGenerator();
        messages = new CliMessages();
    }

    private void loadState() throws CorruptFileException {
        Storage storage = new Storage();
        appState = storage.readFromFile();
    }

    private void saveState() throws ZeroNoteException {
        Storage storage = new Storage();
        storage.saveToFile(appState);
    }

    private void checkNameOfUser(String userInput) throws IOException {
        Storage storage = new Storage();
        boolean isNameOfUserFilled;
        isNameOfUserFilled = storage.isNameOfUserFilled();
            if (isNameOfUserFilled) {
                messages.printFillInNameOfUserMessage();
                storage.saveNameOfUser(userInput);
            }
    }


    public void run() {
        try {
            loadState();
        } catch (CorruptFileException e) {
            e.printErrorMessage();
        }
        startUI();
        String userInput;
        Scanner keyboard = new Scanner(System.in);
        while (!toQuit) {
            userInput = keyboard.nextLine();
            try {
                if (userInput.equals(Exit.COMMAND_WORD)) {
                    saveState();
                    toQuit = true;
                }
                checkNameOfUser(userInput);
                executeCommand(userInput);
            } catch (ZeroNoteException e) {
                e.printErrorMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeCommand(String userInput) throws ZeroNoteException {
        InputParser parser = new InputParser();
        CliCommand command = parser.getCommandFromInput(userInput, appState);
        if(command.isPersonalised) {
            String personalMessage = msgGenerator.generatePersonalisedMessage();
            System.out.println(personalMessage);
        }
        command.execute();
    }

    private void startUI() {
        System.out.println("Welcome to");
        System.out.println(" _ _ _                  _ _ _   _    _            _");
        System.out.println("|_ _  |   _ _    _  _  |  _  | |  \\ | |   _ _   _| |_    _ _");
        System.out.println("  /  /  /  _  \\ | |/_\\ | | | | |   \\| |  /   \\ |_   _| /   _ \\");
        System.out.println(" /  /_  |  _ _/ | |    | |_| | |  |\\  | |  [] |  | |_  |  _ _/");
        System.out.println("|_ _ _|  \\ _ _| |_|    |_ _ _| | _| \\_|  \\ _ /   |_ _|  \\ _ _|");
        System.out.println("You are now in timetable mode");
    }

    public String printExit() {
        return "GOODBYE HOPE TO SEE YOU AGAIN";
    }

}
