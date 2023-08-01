package io.codertown.web.controller;

import io.codertown.web.payload.CoggleSaveRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoggleController {

    @PostMapping("/coggle-save")
    public void coggleSave(@RequestBody CoggleSaveRequest request) {
        System.out.println("request = " + request);

    }
}
