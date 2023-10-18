package io.codertown.web.dto;

import io.codertown.web.entity.UserProject;
import io.codertown.web.entity.project.PersonalStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProjectDto {
    private Long userProjectNo;
    private UserDto userDto;
    private PersonalStatusEnum personalStatus;
    public UserProjectDto convertEntityToDto(UserProject userProject) {
        return UserProjectDto.builder()
                .userProjectNo(userProject.getId())
                .userDto(UserDto.builder().build().userEntityToDto(userProject.getProjectUser()))
                .personalStatus(userProject.getPersonalStatus())
                .build();
    }
}
