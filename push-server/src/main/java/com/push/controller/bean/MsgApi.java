package com.push.controller.bean;

import lombok.Data;

@Data
public class MsgApi {
    String sender;
    String receiver;
    String action;
    String title;
    String content;
    String format;
    String extra;
}
