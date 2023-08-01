package io.codertown.web.service;

import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.recruit.Cokkiri;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.CokkiriSaveRequest;
import io.codertown.web.repository.PartRepository;
import io.codertown.web.repository.RecruitRepository;
import io.codertown.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final ProjectPartRepository projectPartRepository;

    @Transactional(readOnly = false)
    public void cokkiriSave(CokkiriSaveRequest request) {

        try {
            User findUser = (User) userRepository.findByEmail(request.getWriter());
            request.setUser(findUser);
            //코끼리 & 프로젝트 저장 (Cascade.All)
            Cokkiri cokkiri = Cokkiri.builder().build().createCokkiri(request);
            recruitRepository.save(cokkiri);
            // 프로젝트 파트 저장
            List<ProjectPart> collect = request.getProjectParts().stream()
                    .map(projectPartDto-> ProjectPart.builder().build()
                            .createProjectPart(cokkiri.getProject()
                                    , projectPartDto.getRecruitCount()
                                    ,partRepository.findById(projectPartDto.getPartNo()).get())
                    )
                    .collect(Collectors.toList());
            collect.forEach(projectPartRepository::save);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
