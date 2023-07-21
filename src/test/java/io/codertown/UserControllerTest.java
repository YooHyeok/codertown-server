package io.codertown;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.codertown.support.configuration.SecurityConfig;
import io.codertown.support.configuration.WebConfig;
import io.codertown.web.user.CreateUserRequestDto;
import io.codertown.web.user.UserController;
import io.codertown.web.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes={WebConfig.class, CodertownApplication.class, SecurityConfig.class}) //Security테스트를 위해 추가
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc; // 컨트롤러의 API를 테스트하기 위해 사용되는 객체

    @MockBean //실제 빈 객체가 아닌 Mock(가짜) 객체를 생성해서 주입하는 역할을 수행한다.
    UserService userService; // 실제 행위를 수행하지 않는다. - Mockito의 given() 메서드를 통해 동작을 정의한다.

    @Test
    @DisplayName("USER 회원가입 데이터 생성 테스트")
    void createUserTest() throws Exception {
        BDDMockito.given(
                userService.signUp(
                        new CreateUserRequestDto("webdevyoo","gmail.com", null, "1234", "유혁스쿨", null, null)
                )
        ).willReturn(Boolean.TRUE);

        CreateUserRequestDto buildDto = CreateUserRequestDto.builder()
                .emailId("webdevyoo")
                .emailAddress("gmail.com")
                .fullEmail(null)
                .nickname("유혁스쿨")
                .profileIcon(null)
                .password("1234")
                .gender(null)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(buildDto);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/signUp")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"))
                .andDo(MockMvcResultHandlers.print());

        // 모의객체인 userService로부터 signUp 메소드가 1회 호출되었는지 확인.
        Mockito.verify(userService, Mockito.times(1)).signUp(new CreateUserRequestDto("webdevyoo","gmail.com", null, "1234", "유혁스쿨", null, null));
//        Mockito.verify(userService).signUp(new CreateUserRequestDto("webdevyoo","gmail.com", null, "1234", "유혁스쿨", null, null));
    }
}