package ru.otus.chat.controller;

public class UploadException extends RuntimeException {
    public UploadException(String s) {
        super(s);
    }

    public UploadException(String s, Throwable cause) {
        super(s, cause);
    }
}
