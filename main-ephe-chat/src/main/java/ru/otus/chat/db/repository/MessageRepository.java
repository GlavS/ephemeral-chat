package ru.otus.chat.db.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.chat.db.domain.DbMessage;

public interface MessageRepository extends CrudRepository<DbMessage, Long> {}
