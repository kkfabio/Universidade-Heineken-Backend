package com.heineken.auth.service;

import com.heineken.auth.model.dto.request.CreateCourseRequest;
import com.heineken.auth.model.dto.request.CreateModuleRequest;
import com.heineken.auth.model.dto.request.UpdateCourseRequest;
import com.heineken.auth.model.dto.request.UpdateModuleRequest;
import com.heineken.auth.model.dto.response.CourseResponse;
import com.heineken.auth.model.dto.response.ModuleResponse;

import java.util.List;
import java.util.UUID;

public interface AdminCourseService {

    List<CourseResponse> getCourses(String status, String category);

    CourseResponse createCourse(CreateCourseRequest request);

    CourseResponse updateCourse(UUID id, UpdateCourseRequest request);

    ModuleResponse createModule(UUID courseId, CreateModuleRequest request);

    ModuleResponse updateModule(UUID courseId, UUID moduleId, UpdateModuleRequest request);

    void deleteModule(UUID courseId, UUID moduleId);
}
