package io.codertown.web.dto;

import lombok.Data;

@Data
public class ProjectPartUpdateDto {
    private Long projectPartNo;
    private int recruitCount;

    private Long partNo;
}
