package io.codertown;

import io.codertown.web.dto.JoinedProjectDto;
import io.codertown.web.entity.UserProject;
import io.codertown.web.entity.project.Project;
import io.codertown.web.repository.ProjectRepository;
import io.codertown.web.repository.UserProjectRepository;
import io.codertown.web.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@SpringBootTest
@Transactional
@Rollback
public class ProjectRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("내가 참여중인 프로젝트 조회(나의 파트 함께 조회) 테스트 1")
    void findJoinedProjectTest1() {
        long startTime = System.currentTimeMillis();
        projectRepository.findJoinedProjectTest1().forEach(project -> {
            System.out.println("project = " + project.getProject());
            System.out.println("project = " + project.getUserProject());
        });
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("코드 실행 시간: " + elapsedTime + " 밀리초");
    }

    @Test
    @DisplayName("내가 참여중인 프로젝트 조회(나의 파트 함께 조회) 테스트 2")
    void findJoinedProjectTest2() {
        long startTime = System.currentTimeMillis();
        projectRepository.findJoinedProjectTest2().stream().map(object -> {
            return new JoinedProjectDto((Project) object[0], (UserProject) object[1]);
        }).collect(Collectors.toList()).forEach(project -> {
            System.out.println("project = " + project.getProject());
            System.out.println("project = " + project.getUserProject());
        });
        /*projectRepository.findJoinedProjectTest2().forEach(o -> {
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
        long startTime = System.currentTimeMillis();
        projectRepository.findJoinedProjectTest3().stream().map(tuple -> {
            return new JoinedProjectDto(tuple.get("pt", Project.class), tuple.get("up", UserProject.class));
        }).collect(Collectors.toList()).forEach(project -> {
            System.out.println("project = " + project.getProject());
            System.out.println("project = " + project.getUserProject());
        });
        /*projectRepository.findJoinedProjectTest3().forEach(o -> {
            System.out.println("o[0] = " + o.get("pt", Project.class));
            System.out.println("o[1] = " + o.get("up", UserProject.class));
        });*/
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("코드 실행 시간: " + elapsedTime + " 밀리초");
    }
}
