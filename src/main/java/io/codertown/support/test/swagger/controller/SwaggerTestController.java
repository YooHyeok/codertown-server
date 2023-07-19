//package io.codertown.support.test.swagger.controller;
//
//import io.codertown.support.test.swagger.dto.SwagerTestDto;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/swagger")
//@Tag(name = "SwaggerTestController", description = "swaggerTestController")
//public class SwaggerTestController {
//
//
//    @Operation(summary = "Dto 커맨드객체 Response 반환",  description="Swagger 커맨드객체 반환 exampleValue 설정"
//            ,responses = {
//            @ApiResponse(responseCode = "200", description = "가게 상세정보 조회 성공",content = @Content(schema = @Schema(implementation = SwagerTestDto.class))),
//            @ApiResponse(responseCode = "400")
//            })
//    @GetMapping("/example1")
//    public SwagerTestDto getSwaggerExam(@Parameter(name = "이름", required = true, schema = @Schema(ref = "#/components/schemas/CustomMapBody")) @RequestParam String name) {
//        return new SwagerTestDto();
//    }
//
//    @Operation(summary = "Map 객체 Response 반환" , description="Swagger Map반환 exampleValue 설정")
//    @ApiResponses({
//        @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/CustomMapBody")))
//    })
//    @PostMapping("/example1")
//    public Map<String,Object> getSwaggerExam2(@Parameter(name = "이름", required = true) @RequestParam Map<String,Object> name) {
//        return name;
//    }
//
//}
