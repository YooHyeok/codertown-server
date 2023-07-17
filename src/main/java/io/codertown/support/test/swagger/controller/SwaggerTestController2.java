package io.codertown.support.test.swagger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/swagger")
@Tag(name = "SwaggerTestController2", description = "swaggerTestController2")
public class SwaggerTestController2 {


    @Operation(summary="Swagger 예제",  description="Swagger의 기본 Annotation")
    @GetMapping("/example2")
    public String getSwaggerExam(@Parameter(name = "이름", required = true) @RequestParam String name) {
        return name;
    }

    @Operation( description="Swagger의 기본 Annotation")
    @GetMapping("/example3")
    public String getSwaggerExam3(@Parameter(name = "이름", required = true) @RequestParam String name) {
        return name;
    }

}
