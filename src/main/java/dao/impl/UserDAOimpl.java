package dao.impl;

import controllers.HttpResponse;
import dao.UserDAO;
import main.Main;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

@SuppressWarnings("OverlyBroadCatchBlock")
public class UserDAOimpl implements UserDAO {
    private static final int TOKEN_LENGTH = 20;
    private static final String CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    final ObjectMapper mapper;
    final Random random;

    public UserDAOimpl() {
        mapper = new ObjectMapper();
        random = new Random();
    }

    @Override
    public HttpResponse signUp(String jsonString) {
        final JsonNode json;
        try {
            json = mapper.readValue(jsonString, JsonNode.class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        if (!json.has("email") || !json.has("password"))
            return new HttpResponse(HttpResponse.INCORRECT_REQUEST);
        final String email = json.get("email").getTextValue();
        final String password = json.get("password").getTextValue();

        try (Connection connection = Main.connection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM User WHERE email = ?");
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                stmt.close();
                return new HttpResponse(HttpResponse.ALREADY_EXIST);
            }

            String token;
            while (true) {
                token = generateToken();
                stmt = connection.prepareStatement("SELECT COUNT(*) FROM User WHERE token = ?");
                stmt.setString(1, token);
                resultSet = stmt.executeQuery();
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    break;
                }
            }

            stmt = connection.prepareStatement("INSERT INTO User (email,password,token) VALUES (?,?,?)");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, token);
            stmt.execute();
            stmt.close();
            return new HttpResponse(HttpResponse.OK);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse signIn(String jsonString) {
        final JsonNode json;
        try {
            json = mapper.readValue(jsonString, JsonNode.class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        if (!json.has("email") || !json.has("password"))
            return new HttpResponse(HttpResponse.INCORRECT_REQUEST);
        final String email = json.get("email").getTextValue();
        final String password = json.get("password").getTextValue();

        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement("SELECT token FROM User WHERE email = ? AND password = ?");
            stmt.setString(1, email);
            stmt.setString(2, password);
            final ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String token = resultSet.getString(1);
                stmt.close();
                return new HttpResponse(token);
            }else {
                stmt.close();
                return new HttpResponse(HttpResponse.NOT_FOUND);
            }
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    private String generateToken() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            final int number = random.nextInt(CHAR.length());
            final char ch = CHAR.charAt(number);
            builder.append(ch);
        }
        return builder.toString();
    }
}
