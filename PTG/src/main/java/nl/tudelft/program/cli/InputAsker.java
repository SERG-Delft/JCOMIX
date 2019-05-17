package nl.tudelft.program.cli;

import java.util.Scanner;
import java.util.Set;

/**
 * This class will ask the user for input.
 *
 * @author Dimitri Stallenberg
 */
@SuppressWarnings("PMD.SystemPrintln")
public class InputAsker {

    private Scanner scanner;

    /**
     * Constructor.
     */
    public InputAsker() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Simple method which asks the given question and returns the user input.
     *
     * @param question the question to aks
     * @return the input from the user
     */
    public String getInput(String question) {
        System.out.println("\n" + question);
        return scanner.nextLine();
    }

    /**
     * Simple method which asks the given question and returns the user input if it matches the regex.
     *
     * @param question the question to aks
     * @param regex    the regex the answer must match
     * @return the input from the user
     */
    public String getInput(String question, String regex) {
        do {
            System.out.println("\n" + question);
            System.out.println("Regex Format: " + regex);

            String result = scanner.nextLine();

            if (result.matches(regex)) {
                return result;
            } else if (result.contains("quit")) {
                System.out.println("\nQuiting program...");
                System.exit(0);
            } else {
                System.out.println("\nAnswer does not match the right format!");
            }
        } while (true);
    }

    /**
     * Simple method which asks the given question and returns the user input if it matches one of the given options.
     *
     * @param question the question to aks
     * @param options  the options the answer must match
     * @return the input from the user
     */
    public String getInput(String question, Set<String> options) {
        do {
            System.out.println("\n" + question);
            System.out.println("Options: " + options.toString());

            String result = scanner.nextLine();

            if (options.contains(result)) {
                return result;
            } else if (result.contains("quit")) {
                System.out.println("\nQuiting program...");
                System.exit(0);
            } else {
                System.out.println("\nAnswer does not match any of the options!");
            }
        } while (true);
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        scanner.close();
    }
}
