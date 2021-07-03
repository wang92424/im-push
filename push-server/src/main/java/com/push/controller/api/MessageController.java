package com.push.controller.api;

import com.push.sdk.model.Message;
import com.push.push.DefaultMessagePusher;
import com.push.controller.bean.MsgApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/message")
@Api(produces = "application/json", tags = "消息相关接口")
public class MessageController {

    @Autowired
    private DefaultMessagePusher defaultMessagePusher;

    @ApiOperation(httpMethod = "POST", value = "发送消息")
    @PostMapping(value = "/send")
    public ResponseEntity<Long> send(@RequestBody MsgApi msgApi) {

        Message message = new Message();
        message.setSender(msgApi.getSender());
        message.setReceiver(msgApi.getReceiver());
        message.setAction(msgApi.getAction());
        message.setContent(msgApi.getContent());
        message.setFormat(msgApi.getFormat());
        message.setTitle(msgApi.getTitle());
        message.setExtra(msgApi.getExtra());

        message.setId(System.currentTimeMillis());

        defaultMessagePusher.push(message);

        return ResponseEntity.ok(message.getId());
    }


}
