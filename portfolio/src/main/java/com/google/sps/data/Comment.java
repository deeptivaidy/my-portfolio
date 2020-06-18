package com.google.sps.data;


public class Comment {
    private long id;
    private String content;
    private String author;
    private long timestamp;

    public Comment(long id, String content, String author, long timestamp) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
    }
}