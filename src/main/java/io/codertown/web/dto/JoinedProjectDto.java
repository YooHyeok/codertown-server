package io.codertown.web.dto;

import io.codertown.web.entity.project.Project;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinedProjectDto {
    private Project project;

    private Long participationPartNo;
    private String participationPartName;

}
