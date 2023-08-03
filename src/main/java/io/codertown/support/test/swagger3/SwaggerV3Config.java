package io.codertown.support.test.swagger3;

//@Configuration
public class SwaggerV3Config {

    /*@Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springshop-public")
                .pathsToMatch("/public/**")
                .build();
    }
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("springshop-admin")
                .pathsToMatch("/swagger/**")
//                .addOpenApiMethodFilter(method -> method.isAnnotationPresent())
                .build();
    }

    @Bean
    public OpenAPI api() {
        // ExampleValue Scheme 설정 componets()의 키값을 참조한다.
        Schema customMapBody = new Schema<Map<String, Object>>()
                .addProperties("name",new StringSchema().example("John123"))
                .addProperties("password",new StringSchema().example("P4SSW0RD"))
                .addProperties("image",new StringSchema().example("https://robohash.org/John123.png"));

        return new OpenAPI()
                .info(new Info().title("Spring Boot Open API TEST with swagger")
                        .description("Spring shop sample application")
                        .version("v0.0.1")
                        )
                .components(new Components().addSchemas("CustomMapBody", customMapBody));
    }*/
}