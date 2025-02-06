package ru.otus.chat.db.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.chat.db.domain.DbMessage;

@SpringBootTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void shouldSaveMessage() {
        DbMessage m1 = new DbMessage(null, "User", "Message1");
        messageRepository.save(m1);
        DbMessage m2 = messageRepository.findById(1L).get();
        assertEquals(m1.messageStr(), m2.messageStr());
    }
}
