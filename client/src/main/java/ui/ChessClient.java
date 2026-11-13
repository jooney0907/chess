package ui;

import java.util.*;
import client.ServerFacade;
import exception.ResponseException;

public class ChessClient {

    private final ServerFacade server;
    private String authToken = null;

    public ChessClient(ServerFacade server) {
        this.server = server;
    }
    public void run() {
        System.out.println("Welcome to Chess!");
        System.out.println("Type 'help' to see available commands.");

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("quit")) {
            System.out.print("\n>>> ");
            String input = scanner.nextLine();

            result = eval(input);
            System.out.println(result);
        }
    }
    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signin" -> signIn(params);
                case "rescue" -> rescuePet(params);
                case "list" -> listPets();
                case "signout" -> signOut();
                case "adopt" -> adoptPet(params);
                case "adoptall" -> adoptAllPets();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
}