package io.codertown.web.controller;

import io.codertown.web.payload.CoggleSaveRequest;
import io.codertown.web.service.CoggleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CoggleController {

    private final CoggleService coggleService;

    /**
     * 코글 저장 API
     * @param request
     * @return 성공: TRUE | 실패: FALSE
     */
    @PostMapping("/coggle-save")
    public ResponseEntity<Boolean> coggleSave(@RequestBody CoggleSaveRequest request) {
        try {
            Boolean result = coggleService.coggleSave(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
