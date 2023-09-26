package io.codertown.web.service;


import io.codertown.web.dto.*;
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

import java.util.Comparator;
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
            List<ChatMessage> chatMessage = chatRoomUser.getChatRoom().getChatMessage()
                    .stream().sorted(Comparator.comparing(ChatMessage::getChatSendDate)).collect(Collectors.toList()); //채팅 메시지목록 sorted를 사용하여 오름차순 정렬

            /**
             * [채팅방 DTO]
             * 최신 채팅 메시지 : chatMessage...
             * 최신 채팅 메시지 발송 시간 : chatMessage...
             * 채팅 참여 회원 목록 : userDtoList
             * 프로젝트 파트 참여 수락 여부
             */
            ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                    .chatRoomNo(chatRoomUser.getChatRoom().getId())
                    .lastChatMessage(chatMessage.size() == 0 ? null : chatMessage.get(chatMessage.size()-1).getMessage()) //가장 마지막 메시지
                    .lastChatMessageDate(chatMessage.size() == 0 ? null : chatMessage.get(chatMessage.size()-1).getChatSendDate()) //가장 마지막 메시지 전송 시간
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

    /**
     * 채팅 Room 정보 조회
     * 1. 채팅 메시지 리스트
     * 2. 지원한 프로젝트 정보
     * 3. 지원한 파트 정보
     * 4. 수락여부
     * @param chatRoomId
     * @return
     */
    public ChatRoomDetailResponse userChatDetail(String chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();

        List<ChatMessageDto> chatMessageDtoList = chatRoom.getChatMessage().stream()
                .map(chatMessage -> {

                    UserDto userDto = UserDto.builder()
                            .email(chatMessage.getSender().getEmail())
                            .nickname(chatMessage.getSender().getNickname())
                            .profileUrl(chatMessage.getSender().getProfileUrl())
                            .build();

                    return ChatMessageDto.builder()
                                    .sender(userDto)
                                    .message(chatMessage.getMessage())
                                    .chatSendDate(chatMessage.getChatSendDate())
                                    .build();
                }).collect(Collectors.toList());


            ChatRoomDetailResponse result = ChatRoomDetailResponse.builder()
                .chatMessageDtoList(chatMessageDtoList)
                .project(ProjectDto.entityToDto(chatRoom.getProject(), null))
                .projectPart(ProjectPartDto.entityToDto(chatRoom.getProjectPart()))
                .isConfirm(chatRoom.getIsConfirm())
                .build();


        return result;
    }
}