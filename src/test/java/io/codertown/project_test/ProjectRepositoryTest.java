package io.codertown.project_test;

import io.codertown.web.entity.user.User;
import io.codertown.web.repository.UserProjectRepository;
import io.codertown.web.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
public class ProjectRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private ProjectTestRepository projectTestRepository;

    @Test
    @DisplayName("내가 참여중인 프로젝트 조회(나의 파트 함께 조회) 테스트 1")
    void findJoinedProjectTest1() {
/*
        long startTime = System.currentTimeMillis();
        projectTestRepository.findJoinedProjectTest1().forEach(project -> {
            System.out.println("project = " + project.getProject());
            System.out.println("project = " + project.getUserProject());
        });
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("코드 실행 시간: " + elapsedTime + " 밀리초");
*/
    }

    @Test
    @DisplayName("내가 참여중인 프로젝트 조회(나의 파트 함께 조회) 테스트 2")
    void findJoinedProjectTest2() {
        long startTime = System.currentTimeMillis();
        projectTestRepository.findJoinedProjectTest2().forEach(project -> {
            System.out.println("project = " + project.getProject());
            System.out.println("project = " + project.getProjectPart().getPart());
        });
        /*projectTestRepository.findJoinedProjectTest2().stream().map(object -> {
            System.out.println("object.length = " + object.length);
            return new JoinedProjectTestDto((Project) object[0], (UserProject) object[1], (ProjectPart) object[2]);
        }).collect(Collectors.toList()).forEach(project -> {
            System.out.println("project = " + project.getProject());
            System.out.println("project = " + project.getUserProject());
            System.out.println("project = " + project.getProjectPart());
        });*/
        /*projectTestRepository.findJoinedProjectTest2().forEach(o -> {
            System.out.println("o[1] = " + o[0]);
            System.out.println("o[2] = " + o[1]);
        });*/
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("코드 실행 시간: " + elapsedTime + " 밀리초");
    }

    @Test
    @DisplayName("내가 참여중인 프로젝트 조회(나의 파트 함께 조회) 테스트 3")
    void findJoinedProjectTest3() {
        /*long startTime = System.currentTimeMillis();
        projectTestRepository.findJoinedProjectTest3().stream().map(tuple -> {
            return new JoinedProjectTestDto(tuple.get("pt", Project.class), tuple.get("up", UserProject.class), tuple.get("pp", ProjectPart.class));
        }).collect(Collectors.toList()).forEach(project -> {
            System.out.println("project = " + project.getProject());
            System.out.println("project = " + project.getUserProject());
            System.out.println("project = " + project.getProjectPart());
        });
        projectTestRepository.findJoinedProjectTest3().forEach(o -> {
            System.out.println("o[0] = " + o.get("pt", Project.class));
            System.out.println("o[1] = " + o.get("up", UserProject.class));
        });
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("코드 실행 시간: " + elapsedTime + " 밀리초");*/
    }
    
    @Test
    @DisplayName("내가 참여중인 프로젝트 조회(나의 파트 함께 조회) 테스트 4")
    void findJoinedProjectTest4() {
        User loginUser = (User) userRepository.findByEmail("webdevyoo@gmail.com");
        /*List<Map<String, Object>> joinedProject = projectTestRepository.findJoinedProject(loginUser);

        joinedProject.forEach(stringObjectMap -> {
            System.out.println(stringObjectMap.get("part"));
        });*/

        /*List<JoinedProjectDto> collect = joinedProject.stream()
                .map(result -> JoinedProjectTestDto.builder()
                        .project((Project) result.get("project"))
                        .participationPartNo( (Long) result.get("participationPartNo"))
                        .participationPartName((String) result.get("participationPartName"))
                        .build())
                .collect(Collectors.toList());
        collect.forEach(joinedProjectDto -> {
            System.out.println("project = " + joinedProjectDto);
        });*/
    }
}
