package io.codertown.web.service;


import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.chat.ChatRoom;
import io.codertown.web.entity.project.Project;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.request.CreateCokkiriChatRoomRequest;
import io.codertown.web.repository.ChatRoomRepository;
import io.codertown.web.repository.ProjectPartRepository;
import io.codertown.web.repository.ProjectRepository;
import io.codertown.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectPartRepository projectPartRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(readOnly = false)
    public Boolean createChatRoomForCokkiri(CreateCokkiriChatRoomRequest request) {
        User roomMaker = (User) userRepository.findByEmail(request.getLoginId());
        User guest = (User) userRepository.findByEmail(request.getWriterId());
        Project project = projectRepository.findById(request.getProjectNo()).orElseThrow(); //orElseThrow: null이면 예외 발생
        ProjectPart projectPart = projectPartRepository.findById(request.getProjectPartNo()).orElseThrow();
        ChatRoom newChatRoom = ChatRoom.builder().build().createChatRoom(roomMaker, guest, project, projectPart); //채팅 룸 생성
        ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);
        return savedChatRoom.getId()!=null? true:false;
    }


}