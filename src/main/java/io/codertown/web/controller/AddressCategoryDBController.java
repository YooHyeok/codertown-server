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
    @GetMapping("/address-api-collect")
    public void addressApiCollect() throws Exception {
        final String accessToken = getAccessToken(); //액세스토큰 조회
        List<Map<String, Object>> firstAddressList = getFirstAddress(accessToken);
        List<Map<String, Object>> secondAddressList = getSecondAddress(accessToken, firstAddressList);
    }

    /**
     * 두번째 주소 조회 및 수집 <br/>
     * 1. 주소명 <br/>
     * 2. 주소코드
     * @param accessToken
     * @param firstAddressList
     * @return MapList데이터
     */
    private static List<Map<String,Object>> getSecondAddress(String accessToken, List<Map<String, Object>> firstAddressList) {
        for (Map<String, Object> firstAddress : firstAddressList) {
            System.out.println();
            System.out.println();
            System.out.println("firstAddressCd = " + firstAddress.get("firstAddressCd").toString());
            System.out.println();
            try {
                /* accessToken */
                StringBuilder urlBuilder = new StringBuilder("https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json");
                /* accessToken */
                urlBuilder.append("?" + URLEncoder.encode("accessToken", "UTF-8") + "=" + accessToken); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("cd", "UTF-8") + "=" + firstAddress.get("firstAddressCd").toString()); /*Service Key*/
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
                conn.disconnect();

                // JSON 데이터 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> data = objectMapper.readValue(response.toString(), Map.class);
                List<Map<String, Object>> result = (List<Map<String, Object>>) data.get("result");

                System.out.println();
                result.forEach(stringObjectMap -> {
                    System.out.println("변환전 두번째 주소 = " + stringObjectMap);
                });
                System.out.println();
                /*창원시 하위 구에 대한 데이터 통합 코드 38111, 38112, 38113, 38114, 38115*/
                List<Map<String, Object>> secondAddressList = result.stream().map(resultMap -> {
                    Map<String, Object> secondAddress = new HashMap<>();
                    secondAddress.put("secondAddressName", resultMap.get("addr_name"));
                    secondAddress.put("secondAddressCd", resultMap.get("cd"));
                    return secondAddress;
                }).collect(Collectors.toList());
                System.out.println();
                secondAddressList.forEach(stringObjectMap -> {
                    System.out.println("변환된 두번째 주소 = " + stringObjectMap);
                });
                System.out.println();

                return secondAddressList;

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("두번째 주소 조회 오류 발생!");
            }
        }
    }


    /**
     * 첫번째 주소 조회 및 수집 <br/>
     * 1. 주소명 <br/>
     * 2. 주소코드
     * @param accessToken
     * @return MapList데이터
     */
    private static List<Map<String,Object>> getFirstAddress(String accessToken) {
        StringBuilder urlBuilder = new StringBuilder("https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json");
        try {
            /* accessToken */
            urlBuilder.append("?" + URLEncoder.encode("accessToken","UTF-8") + "=" + accessToken); /*Service Key*/
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
            conn.disconnect();

            // JSON 데이터 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> data = objectMapper.readValue(response.toString(), Map.class);
            List<Map<String,Object>> result = (List<Map<String,Object>>) data.get("result");

            System.out.println();
            result.forEach(stringObjectMap -> {
                System.out.println("변환전 첫번째 주소 = " + stringObjectMap);
            });
            System.out.println();

           List<Map<String,Object>> firstAddressList = result.stream().map(resultMap -> {
                Map<String, Object> firstAddress = new HashMap<>();
                firstAddress.put("firstAddressName",resultMap.get("addr_name"));
                firstAddress.put("firstAddressCd",resultMap.get("cd"));
                return firstAddress;
            }).collect(Collectors.toList());
            System.out.println();
            firstAddressList.forEach(stringObjectMap -> {
                System.out.println("변환된 첫번째 주소 = " + stringObjectMap);
            });
            System.out.println();

            return firstAddressList;

        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("첫번째 주소 조회 오류 발생!");
        }
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
