package io.codertown.web.service;

import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.CokkiriSaveRequest;
import io.codertown.web.repository.ProjectRepository;
import io.codertown.web.repository.RecruitRepository;
import io.codertown.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = false)
    public void cokkiriSave(CokkiriSaveRequest request) {
        User findUser = (User) userRepository.findByEmail(request.getWriter());
        request.setUser(findUser);
        //코끼리 & 프로젝트 저장 (Cascade.All)
//        Project project = Project.builder().build().createProject(request);
        Cokkiri cokkiri = Cokkiri.builder().build().createCokkiri(request);
        recruitRepository.save(cokkiri);
    }
}
