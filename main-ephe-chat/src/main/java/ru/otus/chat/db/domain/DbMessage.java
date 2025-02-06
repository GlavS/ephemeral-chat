package ru.otus.chat.db.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("MESSAGE")
public record DbMessage(@Id Long id, String userName, String messageStr) {}
