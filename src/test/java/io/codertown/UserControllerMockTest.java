package io.codertown;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.codertown.support.configuration.SecurityConfig;
import io.codertown.support.configuration.WebConfig;
import io.codertown.support.jwt.JwtTokenProvider;
import io.codertown.web.payload.SignStatus;
import io.codertown.web.payload.request.SignUpRequest;
import io.codertown.web.controller.UserController;
import io.codertown.web.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@ContextConfiguration(classes={WebConfig.class, CodertownApplication.class, SecurityConfig.class}) //Security테스트를 위해 추가
public class UserControllerMockTest {
    @Autowired
    private MockMvc mockMvc; // 컨트롤러의 API를 테스트하기 위해 사용되는 객체

    @MockBean //실제 빈 객체가 아닌 Mock(가짜) 객체를 생성해서 주입하는 역할을 수행한다.
    UserService userService; // 실제 행위를 수행하지 않는다. - Mockito의 given() 메서드를 통해 동작을 정의한다.

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("USER 회원가입 데이터 생성 테스트")
    void createUserTest() throws Exception {
        BDDMockito.given(
                userService.signUp(
                        new SignUpRequest("webdevyoo@gmail.com", "1234", null, null, null, "USER_ROLE")
                )
        ).willReturn(new SignStatus(true, 0, "Success"));

        SignUpRequest buildDto = SignUpRequest.builder()
                .email("webdevyoo@gmail.com")
                .nickname(null)
                .profileIcon(null)
                .password("1234")
                .gender(null)
                .role("USER_ROLE")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(buildDto);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/sign-up")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                /* then 결과값 검증(exists 존재여부) - 46Line의 willReturn에 결과값 가정 */
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").exists())
                /* then 결과값 검증(value 일치여부) - 46Line의 willReturn에 결과값 가정 */
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Success"))
                .andDo(MockMvcResultHandlers.print());

        // 모의객체인 userService로부터 signUp 메소드가 1회 호출되었는지 확인.
        Mockito.verify(userService, Mockito.times(1)).signUp(new SignUpRequest("webdevyoo@gmail.com", "1234", null, null, null, "USER_ROLE"));
//        Mockito.verify(userService).signUp(new CreateUserRequestDto("webdevyoo","gmail.com", null, "1234", "유혁스쿨", null, null));
    }
}
