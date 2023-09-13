package io.codertown.web.dto;

import io.codertown.web.entity.UserProject;
import io.codertown.web.entity.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinedProjectDto {
    private Project project;
    private UserProject userProject;
}
