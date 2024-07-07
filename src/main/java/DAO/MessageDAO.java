package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message mkMessage(Message message) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int messageId = generatedKeys.getInt(1);
                return new Message(messageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println("Error in mkMessage: " + e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllMessages: " + e.getMessage());
        }
        return messages;
    }

    public Message getMessageByID(int id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                return new Message(messageId, postedBy, messageText, timePostedEpoch);
            }
        } catch (SQLException e) {
            System.out.println("Error in getMessageByID: " + e.getMessage());
        }
        return null;
    }

    public Message deleteMessage(int id) {
        Message messageToDelete = getMessageByID(id);
        if (messageToDelete == null) {
            return null;
        }

        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                return messageToDelete;
            }
        } catch (SQLException e) {
            System.out.println("Error in deleteMessage: " + e.getMessage());
        }
        return null;
    }

    public Message updateMessage(int id, Message message) {
        Message savedMessage = getMessageByID(id);
        if (savedMessage == null) {
            return null;
        }

        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                return new Message(id, savedMessage.getPosted_by(), message.getMessage_text(), savedMessage.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println("Error in updateMessage: " + e.getMessage());
        }
        return null;
    }

    public List<Message> getMessagesByUser(int id) {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println("Error in getMessagesByUser: " + e.getMessage());
        }
        return messages;
    }
}
