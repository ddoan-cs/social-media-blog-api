package Service;

import java.util.List;

import DAO.MessageDAO; 
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO; 

    /**
     * Gets the singleton instance of a MessageDao. 
     */
    public MessageService() {
        this.messageDAO = MessageDAO.instance();
    }

    /**
     * Creates a new message within the database. 
     * 
     * @param message message to be inserted.
     * @return newly created message.
     */
    public Message createMessage(Message message) {
        if (!(isValid(message))) {
            return null; 
        }

        return this.messageDAO.createMessage(message);
    }

    /**
     * Get all existing messages within the database. 
     * 
     * @return all messages within the database.
     */
    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    /**
     * Get all existing messages of a given user. 
     * 
     * @param account_id
     */
    public List<Message> getAllMessagesByUser(int account_id) {
        return this.messageDAO.getAllMessagesByUser(account_id); 
    }

    /**
     * Get a specific message matching the given message_id.
     * 
     * @param message_id
     * @return the full message if it exists; otherwise, null. 
     */
    public Message getMessageById(int message_id) {
        return this.messageDAO.getMessageById(message_id); 
    }

    /**
     * Deletes an existing message within the database.
     * 
     * @param message_id
     * @return the deleted message if successful; otherwise, null.
     */
    public Message deleteMessageById(int message_id) {
        Message message = getMessageById(message_id);

        if (message == null) {
            return null; 
        }

        return this.messageDAO.deleteMessageById(message_id, message); 
    }

    /**
     * Updates an existing message within the database.
     * 
     * @param message_id
     * @param message message containing the updated information.
     * @return the updated message if successful; otherwise, null.
     */
    public Message updateMessageById(int message_id, Message message) {
        if (!(isValid(message))) {
            return null; 
        }

        return this.messageDAO.updateMessageById(message_id, message);
    }

    // Helper Methods 

    /**
     * Checks whether a message follows the constaints given: 
     * 1. The message text must not be empty.
     * 2. The message text must not exceed 255 characters.
     * 
     * @param message 
     * @return true if the message is valid; otherwise, false. 
     */
    private boolean isValid(Message message) {
        int text_length = message.getMessage_text().length(); 
        
        if (text_length == 0 || text_length > 255) {
            return false; 
        }

        return true;
    }
}
