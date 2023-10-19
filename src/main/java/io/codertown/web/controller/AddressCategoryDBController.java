package io.codertown.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.codertown.web.entity.address.AddressFirst;
import io.codertown.web.entity.address.AddressSecond;
import io.codertown.web.entity.address.AddressThird;
import io.codertown.web.payload.response.AddressResponse;
import io.codertown.web.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AddressCategoryDBController {

    private final AddressRepository addressRepository;

    @GetMapping("/address-first")
    public List<AddressResponse> addressFirstSearch() {
        return addressRepository.findAll().stream()
                .map(addressFirst -> AddressResponse.builder()
                        .addressNo(addressFirst.getAddrFirstNo())
                        .addrName(addressFirst.getAddrName())
                        .build()
                ).collect(Collectors.toList());
    }

    @GetMapping("/address-second")
    public List<AddressResponse> addressSecondSearch(Long addrFirstNo) {

        return addressRepository.findById(addrFirstNo).orElseThrow()
                        .getAddressSecondList().stream()
                        .map(addressSecond ->
                    AddressResponse.builder()
                            .addressNo(addressSecond.getAddrSecondNo())
                            .addrName(addressSecond.getAddrName())
                            .build()
                ).collect(Collectors.toList());
    }

    @GetMapping("/address-third")
    public List<AddressResponse> addressThirdSearch(Long addrFirstNo, Long addrSecondNo) {

        return addressRepository.findById(addrFirstNo).orElseThrow()
                .getAddressSecondList().stream()
                .filter(addressSecond -> addressSecond.getAddrSecondNo().equals(addrSecondNo))
                .findAny().orElseThrow().getAddressThirdList().stream()
                .map(addressThird ->
                        AddressResponse.builder()
                                .addressNo(addressThird.getAddrThirdNo())
                                .addrName(addressThird.getAddrName())
                                .build()
                ).collect(Collectors.toList());
    }

    @GetMapping("/address-api-collect")
    public void addressApiCollect() throws Exception {
        long startTime = System.currentTimeMillis();

        final String accessToken = getAccessToken(); //액세스토큰 조회
        getFirstAddress(accessToken);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("코드 실행 시간: " + elapsedTime + " 밀리초");
    }
    @Transactional(readOnly = false)
    void getThirdAddress(String accessToken, AddressFirst  addressFirst, AddressSecond addressSecond) {

        try {
            /* accessToken */
            StringBuilder urlBuilder = new StringBuilder("https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json");
            /* accessToken */
            urlBuilder.append("?" + URLEncoder.encode("accessToken", "UTF-8") + "=" + accessToken); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("cd", "UTF-8") + "=" + addressSecond.getAddrCode().toString()); /*Service Key*/
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
                System.out.println("변환전 세번째 주소 = " + stringObjectMap);
            });
            /* 엔티티 생성 후 세번째 주소 메소드 호출 */
            result.forEach(resultMap -> {
                AddressThird addressThird = AddressThird.builder()
                        .addressSecond(addressSecond)
                        .addrName(resultMap.get("addr_name").toString())
                        .addrCode(resultMap.get("cd").toString())
                        .build();
                addressSecond.getAddressThirdList().add(addressThird); //양방향 연관관계 추가
            });
            addressRepository.save(addressFirst);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("두번째 주소 조회 오류 발생!");
        }

    }

    private void getSecondAddress(String accessToken, AddressFirst addressFirst) {

        try {
            /* accessToken */
            StringBuilder urlBuilder = new StringBuilder("https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json");
            /* accessToken */
            urlBuilder.append("?" + URLEncoder.encode("accessToken", "UTF-8") + "=" + accessToken); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("cd", "UTF-8") + "=" + addressFirst.getAddrCode()); /*Service Key*/
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
            /* 엔티티 생성 후 두번째 주소 메소드 호출 */
            result.forEach(resultMap -> {
                AddressSecond addressSecond = AddressSecond.builder()
                        .addressFirst(addressFirst)
                        .addrName(resultMap.get("addr_name").toString())
                        .addrCode(resultMap.get("cd").toString())
                        .addressThirdList(new ArrayList<>())
                        .build();
                addressFirst.getAddressSecondList().add(addressSecond); //양방향 연관관계 추가

                getThirdAddress(accessToken, addressFirst, addressSecond); //세번째 주소 호출
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("두번째 주소 조회 오류 발생!");
        }

    }


    /**
     * 첫번째 주소 조회 및 수집 <br/>
     * 1. 주소명 <br/>
     * 2. 주소코드
     * @return
     */
    private void getFirstAddress(String accessToken) {
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

           /*List<Map<String,Object>> firstAddressList = result.stream().map(resultMap -> {
                Map<String, Object> firstAddress = new HashMap<>();
                firstAddress.put("firstAddressName",resultMap.get("addr_name"));
                firstAddress.put("firstAddressCd",resultMap.get("cd"));
                return firstAddress;
            }).collect(Collectors.toList());
            System.out.println();
            firstAddressList.forEach(stringObjectMap -> {
                System.out.println("변환된 첫번째 주소 = " + stringObjectMap);
            });*/

            result.forEach(resultMap -> {
                AddressFirst addrFirst = AddressFirst.builder()
                        .addrName(resultMap.get("addr_name").toString())
                        .addrCode(resultMap.get("cd").toString())
                        .addressSecondList(new ArrayList<>())
                        .build();
                getSecondAddress(accessToken, addrFirst);

            });

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
