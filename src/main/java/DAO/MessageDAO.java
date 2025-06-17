package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private static MessageDAO messageDAO = null; 

    private MessageDAO() {}

    public static MessageDAO instance() {
        if (messageDAO == null) {
            messageDAO = new MessageDAO();
        }

        return messageDAO; 
    } 

    /** 
     * Insert a new message into the message table.
     * 
     * @param message message object containing the fields.
     * @return the message object.
     */
    public Message createMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text) VALUES(?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), 
                    message.getTime_posted_epoch());
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null; 
    }
    /** 
     * Grab all the messages in the database. 
     * 
     * @return a list of all messages.
     */
    public List<Message> getAllMessages() {
        List<Message> list = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message";
            Statement statement = conn.createStatement(); 

            statement.execute(sql);
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                list.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                rs.getString("message_text"), rs.getLong("time_posted_epoch"))); 
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return list; 
    }

    /** 
     * Get all the messages posted by a specific user. 
     * 
     * @param account_id
     * @return a list of messages.
     */
    public List<Message> getAllMessagesByUser(int account_id) {
        List<Message> list = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

            preparedStatement.executeQuery();
            ResultSet rs = preparedStatement.getResultSet();
            while (rs.next()) {
                list.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                rs.getString("message_text"), rs.getLong("time_posted_epoch"))); 
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return list; 
    }

    /** 
     * Get the message with the matching id given.
     * 
     * @param message_id 
     * @return the message.
     */
    public Message getMessageById(int message_id) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            preparedStatement.executeQuery();
            ResultSet rs = preparedStatement.getResultSet();
            while (rs.next()) {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                rs.getString("message_text"), rs.getLong("time_posted_epoch")); 
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null; 
    }

    /** 
     * Delete message with the matching id given.
     * 
     * @param message_id 
     * @param message message to be deleted.
     * 
     * @return the deleted message if the delete was successful; otherwise, null
     */
    public Message deleteMessageById(int message_id, Message message) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "DELETE FROM message WHERE message_id = (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            if (preparedStatement.executeUpdate() > 0) {
                return message;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null; 
    }

    /** 
     * Update message with the matching id given.
     * 
     * @param message_id
     * @param message message object containing all fields. 
     * 
     * @return the updated message if the update was successful; otherwise, null
     */
    public Message updateMessageById(int message_id, Message message) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, message_id);

            if (preparedStatement.executeUpdate() > 0) {
                return this.getMessageById(message_id); 
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }
    
}