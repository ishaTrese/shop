package com.project.model;

public class Response {
    public String message;
    public RESPONSE_TYPE type;

    public Response() {
    }

    public Response(String message, RESPONSE_TYPE type) {
        this.message = message;
        this.type = type;
    }

    public static Response message(String message, RESPONSE_TYPE type) {
        return switch (type) {
            case SUCCESS -> new Response(message, RESPONSE_TYPE.SUCCESS);
            case ERROR -> new Response(message, RESPONSE_TYPE.ERROR);
            default -> throw new IllegalArgumentException("Unknown response type: " + type);
        };
    }
}