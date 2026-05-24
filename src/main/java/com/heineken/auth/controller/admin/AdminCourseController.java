package com.heineken.auth.controller.admin;

import com.heineken.auth.model.dto.request.CreateCourseRequest;
import com.heineken.auth.model.dto.request.CreateModuleRequest;
import com.heineken.auth.model.dto.request.UpdateCourseRequest;
import com.heineken.auth.model.dto.request.UpdateModuleRequest;
import com.heineken.auth.model.dto.response.CourseResponse;
import com.heineken.auth.model.dto.response.ModuleResponse;
import com.heineken.auth.service.AdminCourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminCourseController {

    private final AdminCourseService adminCourseService;

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getCourses(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(adminCourseService.getCourses(status, category));
    }

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminCourseService.createCourse(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable UUID id,
            @RequestBody UpdateCourseRequest request) {
        return ResponseEntity.ok(adminCourseService.updateCourse(id, request));
    }

    @PostMapping("/{courseId}/modules")
    public ResponseEntity<ModuleResponse> createModule(
            @PathVariable UUID courseId,
            @Valid @RequestBody CreateModuleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminCourseService.createModule(courseId, request));
    }

    @PutMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<ModuleResponse> updateModule(
            @PathVariable UUID courseId,
            @PathVariable UUID moduleId,
            @RequestBody UpdateModuleRequest request) {
        return ResponseEntity.ok(adminCourseService.updateModule(courseId, moduleId, request));
    }

    @DeleteMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<Void> deleteModule(
            @PathVariable UUID courseId,
            @PathVariable UUID moduleId) {
        adminCourseService.deleteModule(courseId, moduleId);
        return ResponseEntity.noContent().build();
    }
}
