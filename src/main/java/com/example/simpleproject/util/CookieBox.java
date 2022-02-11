package com.example.simpleproject.util;

import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class CookieBox {
    public static final String COOKIE_NAME = "login_token";

    private final Map<String, Object> cookieMap = new HashMap<>();

    public CookieBox(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
    }

    public static Cookie createCookie(String name, String value, String path, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        //cookie.setHttpOnly(true);  // XSS 공격 방어를 위해 설정
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public String getValue(String name) throws IOException {
        Cookie cookie = (Cookie) cookieMap.get(name);
        if (cookie == null) return null;
        return cookie.getValue();
    }

    public boolean exists(String name) {
        return cookieMap.get(name) != null;
    }

    public Cookie getCookie(String name) {
        return (Cookie) cookieMap.get(name);
    }

    public Cookie deleteCookie(String cookieKey) {
        Cookie cookie = null;
        if (exists(cookieKey)) {
            cookie = getCookie(cookieKey);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            cookie.setValue(null);
        }
        return cookie;
    }

}
