package ru.otus.chat.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;
import ru.otus.chat.db.domain.DbMessage;
import ru.otus.chat.db.repository.MessageRepository;
import ru.otus.chat.domain.Message;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private static final String RESPONSE_TOPIC = "/topic/response";
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(MessageRepository messageRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo(RESPONSE_TOPIC)
    public Message getMessage(Message message) {
        logger.info("got message:{}, from username:{}", message, message.userName());
        saveMessageToDatabase(message);
        return new Message(message.userName(), HtmlUtils.htmlEscape(message.messageStr()));
    }

    @MessageMapping("/file")
    @SendTo("/topic/file")
    public Message getFileMessage(Message message) {
        logger.info("got file:{}, from username:{}", message, message.userName());
        saveMessageToDatabase(message);
        return message;
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        var genericMessage = event.getMessage();
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(genericMessage);
        List<String> usr = headerAccessor.getNativeHeader("usr");
        String simpDestination = (String) headerAccessor.getHeader("simpDestination");
        if (simpDestination == null || usr == null) {
            return;
        }
        String currentUser = usr.getFirst();
        if (simpDestination.equals(RESPONSE_TOPIC + "/" + currentUser)) {
            logger.info("User connected:{}", currentUser);
            Message message = new Message(currentUser, "User connected: " + currentUser);
            messagingTemplate.convertAndSend(RESPONSE_TOPIC, message);
            messageRepository.findAll().forEach(dbMessage -> {
                logger.info("got message from database:{}, from username:{}", dbMessage, currentUser);
                messagingTemplate.convertAndSend(RESPONSE_TOPIC + "/" + currentUser, dbMessage);
            });
        }
    }

    private void saveMessageToDatabase(Message message) {
        DbMessage dbMessage = new DbMessage(null, message.userName(), message.messageStr());
        DbMessage saved = messageRepository.save(dbMessage);
        logger.info("Message saved to db:{}", saved);
    }
}
