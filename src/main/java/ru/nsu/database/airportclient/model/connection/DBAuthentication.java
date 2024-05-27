package ru.nsu.database.airportclient.model.connection;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAuthentication {

    public record AuthAnswer(boolean isSuccess, String userRights){}
    public static AuthAnswer authUser(String login, String password) throws SQLException {

        Connection connection;
        try{
            connection = DBConnection.INSTANCE.getConnection();
        } catch (ExceptionInInitializerError ex){
            throw new SQLConnectionException("NO CONNECTION");
        }
        String encodedHash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            encodedHash = bytesToHex(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ignored){}

        if (encodedHash == null){
            return new AuthAnswer(false, null);
        }

        System.out.println(encodedHash);

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from users");
        boolean isUserFound = false;
        String userPassword = null;
        String userRights = null;
        while (rs.next()){
            if (login.equals(rs.getString("user_login"))){
                isUserFound = true;
                userPassword = rs.getString("user_password");
                userRights = rs.getString("User_access_role");
                break;
            }
        }

        stmt.close();

        if(!isUserFound || userPassword == null){
            return new AuthAnswer(false, null);
        }
        if (userPassword.equals(encodedHash)){
            return new AuthAnswer(true, userRights);
        }
        return new AuthAnswer(false, null);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
