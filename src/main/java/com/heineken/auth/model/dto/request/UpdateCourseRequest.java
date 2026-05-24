package com.heineken.auth.model.dto.request;

import lombok.Data;

@Data
public class UpdateCourseRequest {

    private String title;
    private String category;
    private String level;
    private Integer durationHours;
    private String status;
}
