package nc.edu.kpi.l3.model;

import nc.edu.kpi.l3.helper.exception.HttpException;
import nc.edu.kpi.l3.helper.exception.NotFoundException;
import nc.edu.kpi.l3.helper.utils.ResourceResolver;
import nc.edu.kpi.l3.helper.Keys;

import java.io.File;
import java.sql.*;

public class Message {

    public static final String FIND_MESSAGE_SQL = "SELECT ID, MESSAGE FROM messages WHERE id = ? LIMIT 1";
    public static final String INSERT_MESSAGE_SQL = "INSERT INTO MESSAGES(message) VALUES (?) RETURNING id";
    public static final String UPDATE_MESSAGE_SQL = "UPDATE MESSAGES set message = ? WHERE id = ? RETURNING id";

    public String message;
    public Integer id;

    public Message(String message) {
        setMessage(message);
    }
    public Message(Integer id, String message) {
        this.id = id;
        setMessage(message);
    }

    public void setMessage(String message) {
        if (message == null || message.length() == 0){
            throw new RuntimeException("Message cant be empty");
        }
        this.message = message;
    }

    public Message save() throws SQLException {
        try (Connection c = Message.getConnection()) {
            PreparedStatement stmt;
            if (this.id != null) {
                stmt = c.prepareStatement(UPDATE_MESSAGE_SQL);
                stmt.setString(1, this.message);
                stmt.setInt(2, this.id);
            } else {
                stmt = c.prepareStatement(INSERT_MESSAGE_SQL);
                stmt.setString(1, this.message);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.id = rs.getInt("ID");
                    return this;
                }
            }
        }
        throw new HttpException();
    }

    private static Connection getConnection(){
        try {
            var file = ResourceResolver.getResource(Keys.get("DB.NAME"));
            return DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        } catch (Exception ex){
            throw new RuntimeException("Database file " + Keys.get("DB.NAME") + " not found", ex);
        }

    }

    public static Message find(final Integer id) throws SQLException {
        try (Connection c = getConnection()) {
            PreparedStatement stmt = c.prepareStatement(FIND_MESSAGE_SQL);
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Message(rs.getInt("ID"), rs.getString("MESSAGE"));
                }
            }
        }
        throw new NotFoundException();
    }

}
