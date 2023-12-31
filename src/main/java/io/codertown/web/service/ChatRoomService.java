package io.codertown.web.service;


import io.codertown.web.dto.*;
import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.chat.ChatMessage;
import io.codertown.web.entity.chat.ChatRoom;
import io.codertown.web.entity.chat.ChatRoomUser;
import io.codertown.web.entity.project.Project;
import io.codertown.web.entity.user.User;
import io.codertown.web.entity.user.UserStatusEnum;
import io.codertown.web.payload.request.CreateCokkiriChatRoomRequest;
import io.codertown.web.payload.response.ChatRoomListResponse;
import io.codertown.web.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ChatMessageRepository chatMessageRepository;
    private final ApplicationEventPublisher eventPublisher;


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
     * 회원별 신규 채팅 메시지 토탈 카운트 조회
     * @param loginId
     * @return
     */
    public Long newMsgTotalCount(String loginId) {
        User loginUser = (User) userRepository.findByEmail(loginId);
        return loginUser.getNewMsgTotalCount();
    }

    /**
     * 코끼리 참여 채팅방 목록 출력 API
     * @param loginEmail
     * @return
     */
    public ChatRoomListResponse userChatList(String loginEmail) {
        User loginUser = (User) userRepository.findByEmail(loginEmail);
        List<ChatRoomUserListDto> chatRomUserDtoList = loginUser.getChatRoomUserList().stream().map(chatRoomUser -> {

            /* 로그인 한 유저의 채팅방별 신규 메시지 카운트 */
            Long newMsgCount = chatRoomUser.getNewMsgCount();
//            Boolean isConnectedRoom = chatRoomUser.getIsConnectedRoom();
            /* 채팅 참여 회원 목록 */
            List<ChatRoomUserDto> chatRoomUserDtos = chatRoomUser.getChatRoom().getChatRoomUserList().stream().map(chatRoomUser1 -> {
                UserDto findUser = null;
                if (chatRoomUser1.getChatRoomUser().getStatus().equals(UserStatusEnum.USING)) { // 정상 회원인경우
                    findUser = UserDto.builder()
                            .email(chatRoomUser1.getChatRoomUser().getEmail())
                            .nickname(chatRoomUser1.getChatRoomUser().getNickname())
                            .profileUrl(chatRoomUser1.getChatRoomUser().getProfileUrl())
                            .build();
                }
                return ChatRoomUserDto.builder().userDto(findUser).isConnectedRoom(chatRoomUser1.getIsConnectedRoom()).build();
                }
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
                    .chatUserList(chatRoomUserDtos)
                    .isConfirm(chatRoomUser.getChatRoom().getIsConfirm())
                    .build();


            return ChatRoomUserListDto.builder()
                    .chatRoom(chatRoomDto) //채팅방 DTO
                    .isRoomMaker(chatRoomUser.getIsRoomMaker()) //채팅방 최초 생성 여부
                    .newMsgCount(newMsgCount)
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
    @Transactional(readOnly = false)
    public ChatRoomDetailResponse userChatDetail(String chatRoomId, String loginId) {
        ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();


        /* 나의 신규 메시지 카운트 조회 및 초기화*/
        ChatRoomUser findChatRoomUser = findChatRoom.getChatRoomUserList()
                .stream().filter(chatRoomUser -> chatRoomUser.getChatRoomUser().getEmail().equals(loginId))
                .findAny().orElseThrow();

        Long newMsgCount = findChatRoomUser.getNewMsgCount();
        findChatRoomUser.decrementNewMsgCount(newMsgCount);

        /* 나의 토탈 메시지 카운트 감소*/
        User loginUser = (User) userRepository.findByEmail(loginId);
        loginUser.decrementNewMsgTotalCount(newMsgCount);

        /* 상대방의 신규 메시지 카운트 조회*/
        User findFriend = findChatRoom.getChatRoomUserList()
                .stream().filter(chatRoomUser -> !chatRoomUser.getChatRoomUser().getEmail().equals(loginId))
                .findAny().orElseThrow().getChatRoomUser();

        /* 상대방 메시지의 읽음여부 true로 변경 */
        chatMessageRepository.bulkUpdateIsReadedByChatRoomAndSender(findChatRoom, findFriend);

        List<ChatMessageDto> chatMessageDtoList = findChatRoom.getChatMessage().stream()
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
                .project(ProjectDto.entityToDto(findChatRoom.getProject(), null))
                .cokkiriNo(findChatRoom.getProject().getCokkiri().getId())
                .projectPart(ProjectPartDto.entityToDto(findChatRoom.getProjectPart()))
                .isConfirm(findChatRoom.getIsConfirm())
                .build();


        return result;
    }

    @Transactional(readOnly = false)
    public void changeConnected(String connectedRoomId, String connectedUserEmail, Boolean connectedValue) {

            ChatRoom findChatRoom = chatRoomRepository.findById(connectedRoomId).orElseThrow();
            ChatRoomUser connectedChatRoomUser = findChatRoom.getChatRoomUserList()
                    .stream()
                    .filter(chatRoomUser ->
                            chatRoomUser.getChatRoomUser().getEmail().equals(connectedUserEmail))
                    .findAny().orElseThrow();
            connectedChatRoomUser.changeConnected(connectedValue);

    }

    @Transactional(readOnly = false)
    public void allChatRoomDisconnect(String userId) {
        User findUser = (User) userRepository.findByEmail(userId);
        findUser.getChatRoomUserList().stream()
                .filter(chatRoomUser -> chatRoomUser.getChatRoomUser().getEmail().equals(userId))
                .forEach(chatRoomUser -> chatRoomUser.changeConnected(false));
    }
}