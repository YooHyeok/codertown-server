package io.codertown.support.jwt;

import io.codertown.support.base.CommonLoggerComponent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenProvider extends CommonLoggerComponent {
    private final UserDetailsService userDetailsService;
    @Value("${springboot.jwt.secret}")
    private String secretKey = "secretKey";
    private final long tokenValidMillsecond = 1000L * 60 * 60; // 토큰 유효시간 1시간

    @PostConstruct
    protected void init() {
        LOGGER.info("[init] Start initializing secretKey in JwtTokenProvider");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        LOGGER.info("[init] Success initializing secretKey in JwtTokenProvider");
    }

    /**
     * createToken
     * @param nickname
     * @param roles
     * @return
     */
    public String createToken(String nickname, List<String> roles) {
        LOGGER.info("[createToken] Start token createing");
        Claims claims = Jwts.claims().setSubject(nickname);
        claims.put("roles", roles);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillsecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        LOGGER.info("createToken Success token createing");
        return token;
    }

    /**
     * refreshToken
     * Client에서 Cookie에 저장해둔다.
     * createToken을 통해 생성/발급된 토큰과 비교한다. 만료되었을 때 새로운 토큰을 발급해주는 토큰이다.
     *
     * @param nickname
     * @param roles
     * @return
     */
    public String refreshToken(String nickname, List<String> roles) {
        LOGGER.info("[refreshToken] Start token createing");
        Claims claims = Jwts.claims().setSubject(nickname);
        claims.put("roles", roles);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillsecond*5)) // 만료 : 현재시간 + 5시간 = 5시간후
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        LOGGER.info("[refreshToken] Success token createing");
        return token;
    }

    /**
     * getUserNickname
     * 토큰에서 회원정보를 추출한다.
     * @param token
     * @return
     */
    public String getUserNickname(String token) {
        LOGGER.info("[getUserNickname] Token-based user identification information extraction");

        String info = Jwts.parserBuilder()
                .setSigningKey(secretKey).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
        LOGGER.info("[getUserNickname] Success Token-based user identification information extraction, info : {}", info);
        return info;
    }

    /**
     * getAuthentication <br/>
     * 토큰으로부터 인증 정보를 조회한다.
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        LOGGER.info("[getAuthentication] Start querying token authentication information");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserNickname(token));
        LOGGER.info("[getAuthentication] Success querying token authentication information, UserDetails UserNickname : {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * resolveToken <br/>
     * request 헤더 에서 엑세스토큰값을 가져온다. <br/>
     * "X-AUTH-TOKEN" : "token"
     * @param request
     * @return
     */
    public String resolveToken(HttpServletRequest request) {
        LOGGER.info("[resolveToken] Token value extraction from Http-Header");
        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * validateToken <br/>
     * 토큰의 유효성 + 만료시간을 확인한다.
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        LOGGER.info("[validationToken] Start Validation validate check");
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date()); //만료시간 체크
        } catch (Exception e) {
            LOGGER.info("[validationToken] Token Validation Exception Raised");
            return false;
        }
    }

}
