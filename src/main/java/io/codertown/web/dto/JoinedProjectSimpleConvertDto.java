package io.codertown.web.dto;

import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinedProjectSimpleConvertDto {

    private Project project;
//    private UserProject userProject;
    private ProjectPart projectPart;

}
