package io.codertown.web.service;


import io.codertown.web.dto.ChatRoomDto;
import io.codertown.web.dto.ChatRoomUserDto;
import io.codertown.web.dto.UserDto;
import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.chat.ChatMessage;
import io.codertown.web.entity.chat.ChatRoom;
import io.codertown.web.entity.project.Project;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.request.CreateCokkiriChatRoomRequest;
import io.codertown.web.payload.response.ChatRoomListResponse;
import io.codertown.web.repository.ChatRoomRepository;
import io.codertown.web.repository.ProjectPartRepository;
import io.codertown.web.repository.ProjectRepository;
import io.codertown.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectPartRepository projectPartRepository;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 코끼리 참여 채팅방 생성 API
     * @param request
     * @return
     */
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

    /**
     * 코끼리 참여 채팅방 목록 출력 API
     * @param loginEmail
     * @return
     */
    public ChatRoomListResponse userChatList(String loginEmail) {
        User roomMaker = (User) userRepository.findByEmail(loginEmail);
        
        List<ChatRoomUserDto> chatRomUserDtoList = roomMaker.getChatRoomUserList().stream().map(chatRoomUser -> {

            /* 채팅 참여 회원 목록 */
            List<UserDto> userDtoList = chatRoomUser.getChatRoom().getChatRoomUserList().stream().map(chatRoomUser1 ->
                    UserDto.builder()
                            .email(chatRoomUser1.getChatRoomUser().getEmail())
                            .nickname(chatRoomUser1.getChatRoomUser().getNickname())
                            .profileUrl(chatRoomUser1.getChatRoomUser().getProfileUrl())
                            .build()
            ).collect(Collectors.toList());

            /* 최신 채팅 메시지(가장 마지막에 저장) */
            List<ChatMessage> chatMessage = chatRoomUser.getChatRoom().getChatMessage(); //채팅 메시지목록

            /**
             * [채팅방 DTO]
             * 최신 채팅 메시지 : chatMessage...
             * 최신 채팅 메시지 발송 시간 : chatMessage...
             * 채팅 참여 회원 목록 : userDtoList
             * 프로젝트 파트 참여 수락 여부
             */
            ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                    .lastChatMessage(chatMessage.size() == 0 ? null : chatMessage.get(chatMessage.size()).getMessage()) //가장 마지막 메시지
                    .lastChatMessageDate(chatMessage.size() == 0 ? null : chatMessage.get(chatMessage.size()).getChatSendDate()) //가장 마지막 메시지 전송 시간
                    .chatUserList(userDtoList)
                    .isConfirm(chatRoomUser.getChatRoom().getIsConfirm())
                    .build();

            return ChatRoomUserDto.builder()
                    .chatRoom(chatRoomDto) //채팅방 DTO
                    .isRoomMaker(chatRoomUser.getIsRoomMaker()) //채팅방 최초 생성 여부
                    .build();

        }).collect(Collectors.toList());

        return ChatRoomListResponse.builder().chatRomUserDtoList(chatRomUserDtoList).build();
    }
}