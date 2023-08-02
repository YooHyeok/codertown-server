package io.codertown.web.service;

import io.codertown.web.entity.Coggle;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.CoggleSaveRequest;
import io.codertown.web.repository.CoggleRepository;
import io.codertown.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoggleService {

    private final CoggleRepository coggleRepository;
    private final UserRepository userRepository;

    /**
     * 코글 저장
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    public Boolean coggleSave(CoggleSaveRequest request) {
        try {
            User writer = (User)userRepository.findByEmail(request.getWriter());
            Coggle coggle = Coggle.builder()
                    .category(request.getCategory())
                    .title(request.getTitle())
                    .content(request.getContent())
                    .status(false)
                    .user(writer)
                    .build();
            coggleRepository.save(coggle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
