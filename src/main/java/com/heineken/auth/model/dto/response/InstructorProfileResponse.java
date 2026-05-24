package com.heineken.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InstructorProfileResponse {
    private String name;
    private String email;
    private Long totalCourses;
    private Long totalStudents;
}
