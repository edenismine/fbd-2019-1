package mx.unam.ciencias.fbd.view;

import mx.unam.ciencias.fbd.util.ConsoleUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * A command line panel.
 */
public class Panel {
    /**
     * Command that signals end of execution (EOE).
     */
    private static final String EOE = "EXIT";
    /**
     * The panel's title. It precedes all user input.
     */
    private String title;
    /**
     * The scanner used to recieve all user input.
     */
    private Scanner scanner;
    /**
     * The handler that handles user input.
     */
    private CommandHandler handler;
    /**
     * All regex sequences the panel recognizes as commands.
     */
    private List<String> commands;
    /**
     * The panel's help printer.
     */
    private IPrintHelp help;

    /**
     * Constructs a new panel with the given title and commands. By default the help printer and the handler are
     * null. All panels accept "EXIT" as a command to terminate execution.
     *
     * @param title    The panel's title
     * @param commands All regex sequences the panel recognizes as commands.
     */
    public Panel(String title, Scanner scanner, String... commands) {
        this.title = title;
        this.scanner = scanner;
        ArrayList<String> temp = new ArrayList<>();
        temp.add(EOE);
        temp.addAll(Arrays.asList(commands));
        this.commands = temp;
    }

    public void setHandler(CommandHandler handler) {
        this.handler = handler;
    }

    public void setHelp(IPrintHelp help) {
        this.help = help;
    }

    /**
     * Runs the panel.
     */
    public void run() {
        boolean close = false;
        String userInput;
        String regex = String.join("|", commands);
        help.printHelp();
        do {
            userInput = ConsoleUtils.getValidString(regex, scanner, title);
            if (userInput.matches(EOE)) {
                close = true;
            } else {
                handler.handle(userInput);
            }
        } while (!close);
    }
}