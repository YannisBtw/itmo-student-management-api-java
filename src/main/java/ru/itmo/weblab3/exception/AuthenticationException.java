package ru.itmo.weblab3.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) { super(message); }
}