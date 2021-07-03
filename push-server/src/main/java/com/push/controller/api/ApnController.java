package com.push.controller.api;

import com.push.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apns")
@Api(produces = "application/json", tags = "APNs推送相关")
public class ApnController {

    @Autowired
    private SessionService sessionService;

    @ApiOperation(httpMethod = "POST", value = "开启apns")
    @PostMapping(value = "/open")
    public ResponseEntity<Void> open(@RequestParam String uid, @RequestParam String deviceToken) {

        sessionService.openApns(uid, deviceToken);

        return ResponseEntity.ok().build();
    }


    @ApiOperation(httpMethod = "POST", value = "关闭apns")
    @PostMapping(value = "/close")
    public ResponseEntity<Void> close(@RequestParam String uid) {

        sessionService.closeApns(uid);

        return ResponseEntity.ok().build();
    }
}
