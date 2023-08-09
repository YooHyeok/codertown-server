package io.codertown.web.payload.request;

import io.codertown.web.dto.ProjectPartUpdateDto;
import lombok.Data;

import java.util.List;

@Data
public class ProjectPartUpdateRequest {

    private List<ProjectPartUpdateDto> update;
    private List<ProjectPartUpdateDto> delete;
    private List<ProjectPartUpdateDto> insert;
}
