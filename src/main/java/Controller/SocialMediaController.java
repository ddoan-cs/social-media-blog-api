package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List; 
import java.util.ArrayList;

import Service.AccountService;
import Model.Account;
import Service.MessageService;
import Model.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    
    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/login", this::loginAccountHandler);
        app.post("/register", this::registerAccountHandler);
        app.post("/messages", this::createMessageHandler); 
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler); 
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler); 
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);

        return app;
    }

    /**
     * Handles user login by validating credentials.
     * If the credentials are correct, returns the logged-in account with status 200.
     * Otherwise, responds with status 401 (Unauthorized).
     *
     * @param ctx the Javalin context containing the HTTP request and response
     */
    private void loginAccountHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account loggedInAccount = accountService.loginToAccount(account);

        if (loggedInAccount == null) {
            ctx.status(401);
        } else {
            ctx.status(200).json(loggedInAccount);
        }
    }

    /**
     * Handles user registration by adding a new account.
     * Responds with status 200 and the registered account if successful.
     * Otherwise, responds with status 400 (Bad Request).
     *
     * @param ctx the Javalin context containing the HTTP request and response
     */
    private void registerAccountHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account registeredAccount = accountService.registerAccount(account);

        if (registeredAccount == null) {
            ctx.status(400);
        } else {
            ctx.status(200).json(registeredAccount);
        }
    }

    /**
     * Handles creation of a new message.
     * Only creates the message if the posting user exists.
     * Returns the created message with status 200 if successful,
     * or status 400 (Bad Request) if creation fails.
     *
     * @param ctx the Javalin context containing the HTTP request and response
     */
    private void createMessageHandler(Context ctx) {
        Message body = ctx.bodyAsClass(Message.class);
        Message message = null; 
        if (accountService.accountExists(body.getPosted_by())) {
            message = messageService.createMessage(body);
        }

        if (message == null) {
            ctx.status(400).result("");
        } else {
            ctx.status(200).json(message);
        }

    }

    /**
     * Retrieves all messages in the system.
     * Responds with status 200 and the list of messages.
     *
     * @param ctx the Javalin context containing the HTTP request and response
     */
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    /**
     * Retrieves a single message by its ID.
     * Responds with status 200 and the message if found,
     * or an empty response with status 200 if not found.
     *
     * @param ctx the Javalin context containing the HTTP request and response
     */
    private void getMessageByIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);

        if (message == null) { 
            ctx.status(200).result("");
        } else {
            ctx.status(200).json(message);
        }
    }

    /**
     * Deletes a message by its ID.
     * Returns the deleted message with status 200 if successful,
     * or an empty response if no message was found to delete.
     *
     * @param ctx the Javalin context containing the HTTP request and response
     */
    private void deleteMessageByIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageById(message_id);

        if (message == null) { 
            ctx.status(200).result("");
        } else {
            ctx.status(200).json(message);
        }
    }

    /**
     * Updates a message by its ID with the provided message body.
     * Returns the updated message with status 200 if successful,
     * or status 400 (Bad Request) if the update is invalid or fails.
     *
     * @param ctx the Javalin context containing the HTTP request and response
     */
    private void updateMessageByIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = ctx.bodyAsClass(Message.class);
        Message newMessage = messageService.updateMessageById(message_id, message);

        if (newMessage == null) { 
            ctx.status(400).result("");
        } else {
            ctx.status(200).json(newMessage);
        }
    }

    /**
     * Retrieves all messages posted by a specific user.
     * Responds with status 200 and the list of messages.
     *
     * @param ctx the Javalin context containing the HTTP request and response
     */
    private void getAllMessagesByUserHandler(Context ctx) {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByUser(account_id);
        ctx.status(200).json(messages);
    }
    
}