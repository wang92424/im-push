package com.push.controller.admin;

import com.push.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/console/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping(value = "/list")
    public String list(Model model) {
        model.addAttribute("sessionList", sessionService.findAll());
        return "console/session/manage";

    }

}
