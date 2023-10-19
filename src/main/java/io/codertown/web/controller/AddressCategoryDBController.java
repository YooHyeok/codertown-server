package io.codertown.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AddressCategoryDBController {
    @GetMapping("/address-api")
    public String getAddress() throws Exception {
        final String accessToken = getAccessToken(); //액세스토큰 조회

    }


    /**
     * 액세스토큰 조회
     * @return
     */
    private static String getAccessToken() {
        final String consumerKey = "f4021c2052634a94a2be";
        final String consumerSecret = "2607dc0a5e144af88eed";
        StringBuilder urlBuilder = new StringBuilder("https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json");
        try {
            /* consumer_key */
            urlBuilder.append("?" + URLEncoder.encode("consumer_key","UTF-8") + "=" + consumerKey); /*Service Key*/
            /* consumer_secret */
            urlBuilder.append("&" + URLEncoder.encode("consumer_secret","UTF-8") + "=" + consumerSecret); /*Service Key*/
            URL url = new URL(urlBuilder.toString()); //URL 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            // 응답 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            // JSON 데이터 파싱
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> data = objectMapper.readValue(response.toString(), Map.class);
            Map<String, Object> result = (Map<String, Object>) data.get("result");
            String accessToken =  (String) result.get("accessToken");
            conn.disconnect();
            return accessToken;
        }catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("액세스토큰 조회 오류 발생!");
        }
    }
}
