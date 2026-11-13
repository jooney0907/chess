package exception;

public class ResponseException extends Exception {

    public enum Code {
        ServerError,
        ClientError,
        Unauthorized,
        Forbidden
    }

    private final Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public static Code fromHttpStatusCode(int status) {
        return switch (status) {
            case 400 -> Code.ClientError;
            case 401 -> Code.Unauthorized;
            case 403 -> Code.Forbidden;
            default -> Code.ServerError;
        };
    }
}
