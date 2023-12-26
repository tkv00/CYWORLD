package org.example;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
        private String sender;
        private String receiver;
        private String title;
        private String content;
        private  String send_time;

        public Message(String sender, String receiver, String title, String content,String send_time) {
            this.sender = sender;
            this.receiver = receiver;
            this.title = title;
            this.content = content;
        }

        public String getSender() {
            return sender;
        }

        public String getReceiver() {
            return receiver;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
        public String getSentTime() {
            String time=new SimpleDateFormat("yyyy-MM-dd HH-mm").format(new Date());
            return time;
        }
    }
