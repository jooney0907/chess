package ui;

import chess.*;
import java.util.*;
import client.ServerFacade;
import exception.ResponseException;

public class ChessClient {

    private final ServerFacade server;
    private String authToken = null;
    private State state = State.SIGNEDOUT;
    private int currentGameID;
    private ChessGame.TeamColor currentColor;

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
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> "Unknown command. Type 'help' for available commands.";
            };
        } catch (ResponseException ex) {
            return "Error: " + ex.getMessage();
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
            output.append(number).append(". Game: ").append(game.gameName()).append(" | WHITE: ")
                    .append(white).append(" | BLACK: ").append(black).append("\n");
            number++;
        }
        return output.toString();
    }

    private String joinGame(String[] params) throws ResponseException {
        if (state != State.SIGNEDIN) {
            return "User needs to be logged in";
        }
        if (params.length < 2) {
            return "Please enter game number and desired color.";
        }
        if (params.length > 2) {
            return "Please only enter game number and desired color.";
        }
        int gameNumber;
        try {
            gameNumber = Integer.parseInt(params[0]);
        } catch (NumberFormatException ex) {
            return "Please enter game number.";
        }
        String color = params[1].toUpperCase();
        if (!color.equals("WHITE") && !color.equals("BLACK")) {
            return "Please choose WHITE or BLACK.";
        }
        var games = server.listGames(authToken).games();
        if (gameNumber < 1 || gameNumber > games.size()) {
            return "Game does not exist.";
        }
        var game = games.get(gameNumber - 1);
        server.joinGame(authToken, color, game.gameID());
        currentGameID = game.gameID();
        currentColor = ChessGame.TeamColor.valueOf(color);
        state = State.INGAME;
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        PrintChessBoard printer = new PrintChessBoard(currentColor);
        printer.print(board);
        return "Successfully joined " + game.gameName() + " as " + color;
    }
    private String observeGame(String[] params) throws ResponseException {
        if (state != State.SIGNEDIN) {
            return "You need to be logged in to observe";
        }

        if (params.length < 1) {
            return "Please enter game number.";
        }
        if (params.length > 1) {
            return "Please only enter one game number.";
        }
        int gameNumber;
        try {
            gameNumber = Integer.parseInt(params[0]);
        } catch (NumberFormatException ex) {
            return "Please enter game number.";
        }
        var games = server.listGames(authToken).games();
        if (gameNumber < 1 || gameNumber > games.size()) {
            return "Does not exist.";
        }
        var game = games.get(gameNumber - 1);
        currentGameID = game.gameID();
        currentColor = null;
        state = State.INGAME;
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        PrintChessBoard printer = new PrintChessBoard(currentColor);
        printer.print(board);
        return "Observing " + game.gameName();
    }
    private String leaveGame() {
        if (state != State.INGAME) {
            return "You are not currently in a game.";
        }
        currentGameID = 0;
        currentColor = null;
        state = State.SIGNEDIN;
        return "You have left the game.";
    }

    }
