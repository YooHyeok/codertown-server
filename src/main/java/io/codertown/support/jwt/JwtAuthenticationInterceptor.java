package io.codertown.support.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = jwtTokenProvider.resolveToken(request); //request header로 부터 토큰 가져오기
        if(token == null) return true; //1. 토큰이 비어있을 때 : 로그인시 토큰 생성 전이기 때문에 로그인 처리로 보낸다.
        String[] tokens = token.split(",");
        if(jwtTokenProvider.validateToken(tokens[0])) { //2.access token이 유효한 경우, 정상처리
            System.out.println("2. 유효");
            //토큰이 유효하면 토큰으로부터 유저 정보를 받아온다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //Security Context에 Authentication 객체를 저장한다.
            return true;
        } else if (tokens.length==1) {//3. accessToken을 가져왔는데 만료되었을 경우. refreshToken 요청
            System.out.println("3. refreshToken 요청");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"rescode\": 100}");
            response.getWriter().flush();

        } else if(jwtTokenProvider.validateToken(tokens[1])) { //4.refreshToken 유효함. 새로운 두개의 토큰 재발급
            System.out.println("4. refreshToken유효함 두개의 새 토큰 재발급");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String userNickname = jwtTokenProvider.getUserNickname(tokens[1]);
            String accessToken = jwtTokenProvider.createToken(userNickname, new ArrayList<>());
            String refreshToken = jwtTokenProvider.refreshToken(userNickname, new ArrayList<>());
            response.getWriter().write(
                    "{\"rescode\":101, \"accessToken\":\"" +accessToken+"\",\"refreshToken\":\""+refreshToken+"\"}");
            response.getWriter().flush();

        }else {//5. refresh 토큰 만료됨. 재로그인 요청
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"rescode\":102}");
            response.getWriter().flush();

        }
        return false;

    }
}
