package Service;

import Model.Account;
import Model.Message;
import DAO.AccountDAO;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public Message newMessage(Message message) {
        if (isValidMessage(message)) {
            return messageDAO.mkMessage(message);
        }
        return null;
    }

    private boolean isValidMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty()) {
            return false;
        }
        if (message.getMessage_text().length() >= 255) {
            return false;
        }
        List<Account> accounts = accountDAO.getAllAccounts();
        for (Account ac : accounts) {
            if (ac.getAccount_id() == message.getPosted_by()) {
                return true;
            }
        }
        return false;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int id) {
        return messageDAO.getMessageByID(id);
    }

    public Message deleteMessage(int id) {
        return messageDAO.deleteMessage(id);
    }

    public Message updateMessage(int id, Message message) {
        List<Message> messages = messageDAO.getAllMessages();
        if (!message.getMessage_text().isEmpty() && message.getMessage_text().length() < 255) {
            for (Message mg : messages){
                if (mg.getMessage_id() == id){
                    return messageDAO.updateMessage(id, message);
                }
            }
        }
        return null;
    }

    public List<Message> getMessagesByUser(int id) {
        return messageDAO.getMessagesByUser(id);
    }
}
