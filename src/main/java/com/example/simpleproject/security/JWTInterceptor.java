package com.example.simpleproject.security;

import com.example.simpleproject.service.JWTService;
import com.example.simpleproject.util.CookieBox;
import com.example.simpleproject.vo.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTService service;

    @Value("${server.redirect-url}")
    private String redirectUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        CookieBox cookieBox = new CookieBox(request);
        String token = null;

        if(cookieBox.exists(CookieBox.COOKIE_NAME)){
            token = cookieBox.getValue(CookieBox.COOKIE_NAME);
        }

        if (token != null && service.isUsable(token)) {
            return true;
        } else {
            response.sendRedirect(redirectUrl);
            return false;
        }
         /*
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(CookieBox.COOKIE_NAME))
                .findFirst().map(Cookie::getValue)
                .orElse("dummy");

        System.out.println("token : {"+ token   +   "}  ");

        try{
            Map<String, Object> info = service.verify(token);
            User user = User.builder()
                    .id((Long) info.get("id"))
                    .name((String) info.get("name"))
                    .build();
            request.setAttribute("user",user);
        }catch (ExpiredJwtException ex){
            System.out.println("토큰 만료");
            ModelAndView mav = new ModelAndView("login");
            mav.addObject("return_url",request.getRequestURI());
            throw new ModelAndViewDefiningException(mav);
        }catch(JwtException ex){
            System.out.println("비정상 토큰");
            ModelAndView mav = new ModelAndView("login");
            throw new ModelAndViewDefiningException(mav);
        }
        return true;
         */
    }
}
