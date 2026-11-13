
import client.ServerFacade;
import ui.ChessClient;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        try {
            new ChessClient(new ServerFacade(port)).run();
        } catch (Exception ex) {
            System.out.printf("Unable to start client: %s%n", ex.getMessage());
        }
    }
}
