package client;


import model.*;
import exception.ResponseException;
import com.google.gson.Gson;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        var body = new UserData(username, password, email);
        var request = buildRequest("POST", "/user", body);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public int creategame(String authToken, String gameName) throws Exception{
        var body = new CreateGameRequest(gameName);
        var request = authTokBuildRequest("POST", "/game", body, authToken);
        var response = sendRequest(request);
        var result = handleResponse(response, CreateGameResult.class);
        return result.gameID();
    }

    public AuthData login(String username, String password) throws ResponseException{
        var body = new LoginRequest(username,password);
        var request = buildRequest("POST", "/session", body);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void clear() throws ResponseException{
        var request = buildRequest("DELETE","/db", null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void logout(String authToken)throws ResponseException{
        var request = authTokBuildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    private HttpRequest authTokBuildRequest(String method, String path, Object body, String authToken){
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));

        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("Authorization", authToken);
        }
        return request.build();
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));

        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }

        return request.build();
    }
    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }
    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }
    private <T> T handleResponse(HttpResponse<String> response,
                                 Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(
                    ResponseException.fromHttpStatusCode(status),
                    "Request failed: " + response.body()
            );
        }
        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }




    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
