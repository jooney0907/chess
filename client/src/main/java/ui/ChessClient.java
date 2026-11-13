package ui;

import java.util.*;
import client.ServerFacade;
import exception.ResponseException;

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
        try {
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "quit" -> "quit";
                case "help" -> help();
                default -> "Unknown command. Type 'help' for available commands.";
            };
        } catch (ResponseException ex) {
            return "Unable to complete request. Please check your information and try again.";
        }
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

        private String register(String[] params) throws ResponseException{
            if (params.length == 3) {
                String username = params[0];
                String password = params[1];
                String email = params[2];
                var result = server.register(username, password, email);
                authToken = result.authToken();
                state = State.SIGNEDIN;
                return "Successfully registered as " + result.username();
            } else if(params.length > 3){
                return "Please only enter username, password, and email.";
            }
                return "Please enter username, password, and email";
        }

    private String login(String[] params) throws ResponseException {
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            var result = server.login(username, password);
            authToken = result.authToken();
            state = State.SIGNEDIN;
            return "Successfully logged in as " + result.username();
        } else if (params.length > 2) {
            return "Please only enter username and password.";
        }
        return "Please enter username and password.";
    }
    private String logout() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            return "You are not currently logged in.";
        }
        server.logout(authToken);
        authToken = null;
        state = State.SIGNEDOUT;
        return "User is logged out.";
    }
    private String createGame(String[] params) throws ResponseException {
        if (state != State.SIGNEDIN) {
            return "You need to be logged in order to create a game.";
        }
        if (params.length == 1) {
            String gameName = params[0];
            server.creategame(authToken, gameName);
            return "New game: " + gameName;
        } else if (params.length > 1) {
            return "Please only enter one name";
        }
        return "Please enter a name.";
    }
    private String listGames() throws ResponseException {
        if (state != State.SIGNEDIN) {
            return "You need to be logged in to list games.";
        }
        var result = server.listGames(authToken);
        var games = result.games();
        if (games.isEmpty()) {
            return "No games available.";
        }
        StringBuilder output = new StringBuilder();
        int number = 1;
        for (var game : games) {
            String white = game.whiteUsername() == null ? "Available" : game.whiteUsername();
            String black = game.blackUsername() == null ? "Available" : game.blackUsername();
            output.append(number).append(". Game: ").append(game.gameName()).append(" | White: ")
                    .append(white).append(" | Black: ").append(black).append("\n");
            number++;
        }
        return output.toString();
    }
    }
