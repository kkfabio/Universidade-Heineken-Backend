package com.heineken.auth.service;

import com.heineken.auth.model.dto.response.AtRiskUserResponse;
import com.heineken.auth.model.dto.response.CourseStatsResponse;
import com.heineken.auth.model.dto.response.InstructorProfileResponse;
import com.heineken.auth.model.dto.response.RecentActivityResponse;
import com.heineken.auth.model.dto.response.WeeklyActivityResponse;

import java.util.List;

public interface DashboardService {

    InstructorProfileResponse getInstructorProfile(String email);

    List<CourseStatsResponse> getCourseStats();

    List<AtRiskUserResponse> getAtRiskUsers();

    List<WeeklyActivityResponse> getWeeklyActivity();

    List<RecentActivityResponse> getRecentActivity();
}
