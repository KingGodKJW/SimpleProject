package com.example.simpleproject.controller;

import com.example.simpleproject.repository.UserRepository;
import com.example.simpleproject.service.JWTService;
import com.example.simpleproject.util.CookieBox;
import com.example.simpleproject.util.JWTUtil;
import com.example.simpleproject.vo.User;
import com.example.simpleproject.vo.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private JWTUtil util;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password, HttpServletResponse res, Model model) throws Exception{
        try{
            manager.authenticate(new UsernamePasswordAuthenticationToken(name,password));
            String token = util.generateToken(name);
            Cookie setCookie = CookieBox.createCookie(CookieBox.COOKIE_NAME,token,"/",60*60*3);
            res.addCookie(setCookie);
            UserResponseDTO dto = new UserResponseDTO(name,password);
            model.addAttribute("user",dto);
        }catch (Exception e){
            throw new Exception("invalid name/password");
        }
        return "main";
    }







}
