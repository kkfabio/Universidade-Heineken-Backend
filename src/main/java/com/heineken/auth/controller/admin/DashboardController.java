package com.heineken.auth.controller.admin;

import com.heineken.auth.model.dto.request.CreatePostRequest;
import com.heineken.auth.model.dto.request.UpdatePostRequest;
import com.heineken.auth.model.dto.response.AtRiskUserResponse;
import com.heineken.auth.model.dto.response.CompanyPostResponse;
import com.heineken.auth.model.dto.response.CourseStatsResponse;
import com.heineken.auth.model.dto.response.InstructorProfileResponse;
import com.heineken.auth.model.dto.response.PagedResponse;
import com.heineken.auth.model.dto.response.RecentActivityResponse;
import com.heineken.auth.model.dto.response.WeeklyActivityResponse;
import com.heineken.auth.service.CompanyPostService;
import com.heineken.auth.service.DashboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    private final DashboardService dashboardService;
    private final CompanyPostService companyPostService;

    @GetMapping("/profile")
    public ResponseEntity<InstructorProfileResponse> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(dashboardService.getInstructorProfile(userDetails.getUsername()));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<CourseStatsResponse>> getCourseStats() {
        return ResponseEntity.ok(dashboardService.getCourseStats());
    }

    @GetMapping("/at-risk")
    public ResponseEntity<List<AtRiskUserResponse>> getAtRiskUsers() {
        return ResponseEntity.ok(dashboardService.getAtRiskUsers());
    }

    @GetMapping("/activity")
    public ResponseEntity<List<WeeklyActivityResponse>> getWeeklyActivity() {
        return ResponseEntity.ok(dashboardService.getWeeklyActivity());
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<RecentActivityResponse>> getRecentActivity() {
        return ResponseEntity.ok(dashboardService.getRecentActivity());
    }

    @GetMapping("/posts")
    public ResponseEntity<PagedResponse<CompanyPostResponse>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(companyPostService.getPosts(pageable));
    }

    @PostMapping("/posts")
    public ResponseEntity<CompanyPostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyPostService.createPost(request));
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<CompanyPostResponse> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequest request) {
        return ResponseEntity.ok(companyPostService.updatePost(id, request));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        companyPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
