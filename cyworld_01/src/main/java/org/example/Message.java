package org.example;

public class Message {
    private String sender;
    private String receiver;
    private String content;

    // 생성자, getter, setter 등 필요한 메소드를 구현
    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    // 필요한 추가 메소드 구현
}
