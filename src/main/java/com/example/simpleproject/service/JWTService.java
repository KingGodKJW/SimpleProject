package com.example.simpleproject.service;

import com.example.simpleproject.exception.UnauthorizedException;
import com.example.simpleproject.util.CookieBox;
import com.example.simpleproject.vo.UserResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JWTService {

    @Value("${spring.jwt.secret_key}")
    private String secretKey;


    /**
     * body 가 들어간 토큰 생성
     *
     * @param body
     * @param expired 토근 만료 시간
     * @return
     */
    public String token(Map<String, Object> body, Optional<LocalDateTime> expired) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);

        Key key = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setClaims(body)
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))
                .signWith(SignatureAlgorithm.HS512, key);

        expired.ifPresent(exp -> {
            builder.setExpiration(Timestamp.valueOf(exp));
        });

        return builder.compact();
    }

    /**
     * 기본 만료시간 : 하루 30분 : LocalDateTime.now().plusMinutes(30) 1시간 :
     * LocalDateTime.now().plusHours(1)
     *
     * @param body
     * @return
     */
    public String token(Map<String, Object> body) {
        return token(body, Optional.of(LocalDateTime.now().plusDays(1)));
    }

    /**
     * 토큰 검증후 저장된 값 복원
     */
    public Map<String, Object> verify(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token)
                .getBody();
        return new HashMap<>(claims);
    }

    /**
     * 유효한 토큰인지 검증
     */
    public boolean isUsable(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnauthorizedException();
        }
    }

    public String destroyToken(HttpServletRequest request, HttpServletResponse response) {
        CookieBox cookieBox = new CookieBox(request);
        Cookie deletedCookie = cookieBox.deleteCookie(CookieBox.COOKIE_NAME);
        response.addCookie(deletedCookie);
        return "success";
    }



}
