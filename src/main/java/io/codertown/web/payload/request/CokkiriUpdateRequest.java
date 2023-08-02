package io.codertown.web.payload.request;

public class CokkiriUpdateRequest {
    private Long recruitNo;
    private Long partNo;
    private int recruitCount; // 클라이언트에서 현재 지원한 인원보다 작을경우 유효성검증 처리
}
