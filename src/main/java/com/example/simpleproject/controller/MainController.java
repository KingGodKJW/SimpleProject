package com.example.simpleproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String indexPage() throws Exception{
        return "index";
    }

    @GetMapping("/main")
    public String mainPage() throws Exception{
        return "main";
    }
}
