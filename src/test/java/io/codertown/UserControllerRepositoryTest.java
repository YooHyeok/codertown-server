package io.codertown;

import io.codertown.web.entity.project.Project;
import io.codertown.web.repository.ProjectRepository;
import io.codertown.web.entity.user.User;
import io.codertown.web.repository.UserRepository;
import io.codertown.web.entity.UserProject;
import io.codertown.web.repository.UserProjectRepository;
import io.codertown.web.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback
public class UserControllerRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("회원별 참여중인 프로젝트 조회")
    void projectList() {
        User findUser = (User)userRepository.findByEmail("webdevyoo@gmail.com");
        //프로젝트 저장
        Project project1 = Project.builder().projectTitle("프로젝트1").build();
        Project saveProject = projectRepository.save(project1);
        //회원 - 프로젝트 중간테이블에 회원, 프로젝트 저장
        UserProject saveUserProject = UserProject.builder().projectUser(findUser).project(saveProject).build();
        userProjectRepository.save(saveUserProject);

        List<UserProject> projectUsers = findUser.getProjectUsers();
        System.out.println("projectUsers = " + projectUsers);
        List<Project> projects = null;
        for (UserProject user : projectUsers) {
            projects= new ArrayList<>();
            projects.add(user.getProject());
        }
        System.out.println("projects = " + projects);

    }

}
