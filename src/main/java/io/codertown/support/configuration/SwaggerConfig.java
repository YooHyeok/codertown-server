//package io.codertown.support.configuration;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.ExternalDocumentation;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
//import io.swagger.v3.oas.models.media.Schema;
//import io.swagger.v3.oas.models.media.StringSchema;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Map;
//
//@Configuration
//public class SwaggerConfig {
//
//    /*@Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("io.codertown.web.controller"))
//                                                     // [Group + Artifact] ex) com.example.demo
//                .paths(PathSelectors.any())
//                .build();
//    }*/
//
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("springshop-public")
//                .pathsToMatch("/public/**")
//                .build();
//    }
//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .group("springshop-admin")
//                .pathsToMatch("/swagger/**")
////                .addOpenApiMethodFilter(method -> method.isAnnotationPresent())
//                .build();
//    }
//
//    @Bean
//    public OpenAPI api() {
//        // ExampleValue Scheme 설정 componets()의 키값을 참조한다.
//        Schema customMapBody = new Schema<Map<String, Object>>()
//                .addProperties("name",new StringSchema().example("John123"))
//                .addProperties("password",new StringSchema().example("P4SSW0RD"))
//                .addProperties("image",new StringSchema().example("https://robohash.org/John123.png"));
//
//        return new OpenAPI()
//                .info(new Info().title("Spring Boot Open API TEST with swagger")
//                        .description("Spring shop sample application")
//                        .version("v0.0.1")
//                        )
//                .components(new Components().addSchemas("CustomMapBody", customMapBody));
//    }
//}