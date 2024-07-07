package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; 

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register",this::registerAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::newMessageHandler);
        app.get("/messages", this::getMessageHandler);
        app.get("/messages/{message_id}",this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

    private void registerAccountHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account registerAccount = accountService.registerAccount(account);
    
        if (registerAccount != null){
            context.json(mapper.writeValueAsString(registerAccount));
        } else {
            context.status(400);
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loginAccount = accountService.login(account);
    
        if (loginAccount != null){
            context.json(loginAccount);
        } else {
            context.status(401);
        }
    }

    private void newMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message newMessage = messageService.newMessage(message);
        
        if (newMessage != null) {
            context.json(mapper.writeValueAsString(newMessage));
        } else {
            context.status(400);
        }
    }

    private void getMessageHandler(Context context){
        List<Message> message = messageService.getAllMessages();
        context.json(message);
    }

    private void getMessageByIDHandler(Context context){
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageByID(id);
        if (message != null) {
            context.json(message);
        }
    }

    private void deleteMessageHandler(Context context){
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message delMessage = messageService.deleteMessage(id);
        if (delMessage != null) {
            context.json(delMessage);
        }
    }

    private void updateMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(id, message);
        if(updatedMessage != null){
            context.json(updatedMessage);
        } else {
            context.status(400);
        }
    }

    private void getMessageByUserHandler(Context context){
        int id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> message = messageService.getMessagesByUser(id);
        context.json(message);
    }
}