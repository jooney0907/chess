package ui;

import java.util.*;
import client.ServerFacade;

public class ChessClient {

    private final ServerFacade server;
    private String authToken = null;
    private State state = State.SIGNEDOUT;

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
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> "Register";
                case "login" -> "Login";
                case "quit" -> "Quit";
                case "help" -> help();
                default -> "help";
            };
    }
    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
        } else {
            return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
        }
        }
}