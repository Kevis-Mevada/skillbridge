<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.StudentProfile, com.skillbridge.model.StudentDashboardStats" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    StudentProfile profile = (StudentProfile) request.getAttribute("profile");
    StudentDashboardStats stats = (StudentDashboardStats) request.getAttribute("stats");
    if (stats == null) stats = new StudentDashboardStats();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard – SkillBridge</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="dashboard-body">

    <div class="dashboard-layout">
        <!-- Sidebar Navigation -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <a href="${pageContext.request.contextPath}/" class="brand-logo">
                    <span class="brand-icon">⚡</span>
                    <span>SkillBridge</span>
                </a>
                <span class="badge badge-primary" style="margin-top: 0.5rem;">Student Portal</span>
            </div>

            <div class="sidebar-user-card">
                <div class="user-avatar">
                    <%= (currentUser != null && currentUser.getFullName() != null) ? currentUser.getFullName().substring(0, 1).toUpperCase() : "S" %>
                </div>
                <div class="user-meta">
                    <h4><%= (currentUser != null) ? currentUser.getFullName() : "Student" %></h4>
                    <p><%= (currentUser != null) ? currentUser.getEmail() : "" %></p>
                </div>
            </div>

            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/student/dashboard" class="sidebar-link active">
                    <span class="link-icon">📊</span>
                    <span>Dashboard</span>
                </a>
                <a href="${pageContext.request.contextPath}/student/profile" class="sidebar-link">
                    <span class="link-icon">👤</span>
                    <span>My Profile &amp; Skills</span>
                </a>
                <a href="${pageContext.request.contextPath}/jobs" class="sidebar-link">
                    <span class="link-icon">🔍</span>
                    <span>Browse Opportunities</span>
                </a>
                <a href="${pageContext.request.contextPath}/student/applications" class="sidebar-link">
                    <span class="link-icon">📄</span>
                    <span>My Applications</span>
                </a>
                <a href="${pageContext.request.contextPath}/student/saved-jobs" class="sidebar-link">
                    <span class="link-icon">🔖</span>
                    <span>Saved Jobs</span>
                </a>
                <a href="${pageContext.request.contextPath}/student/interviews" class="sidebar-link">
                    <span class="link-icon">🗓️</span>
                    <span>Interviews</span>
                </a>
            </nav>

            <div class="sidebar-footer">
                <a href="${pageContext.request.contextPath}/logout" class="sidebar-link logout-link">
                    <span class="link-icon">🚪</span>
                    <span>Log Out</span>
                </a>
            </div>
        </aside>

        <!-- Main Dashboard Content -->
        <main class="dashboard-main">
            <!-- Top App Bar -->
            <header class="dashboard-topbar">
                <div>
                    <h1 style="font-size: 1.75rem;">Welcome back, <%= (currentUser != null) ? currentUser.getFullName().split(" ")[0] : "Student" %>! 👋</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Track your job applications and upcoming interview schedules</p>
                </div>
                <div style="display: flex; gap: 1rem; align-items: center;">
                    <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary btn-sm">
                        <span>🔍 Explore Jobs</span>
                    </a>
                </div>
            </header>

            <div class="dashboard-content">
                <!-- Profile Completion Banner -->
                <div class="completion-card">
                    <div class="completion-header">
                        <div>
                            <h3>Profile Strength: <%= stats.getProfileCompletionPercentage() %>%</h3>
                            <p><%= stats.getProfileCompletionPercentage() < 100 ? "Complete your profile, education, and skills to increase your visibility to recruiters." : "Great job! Your profile is 100% complete and visible to top recruiters." %></p>
                        </div>
                        <a href="${pageContext.request.contextPath}/student/profile" class="btn btn-sm btn-outline">
                            <%= stats.getProfileCompletionPercentage() < 100 ? "Complete Profile →" : "Edit Profile" %>
                        </a>
                    </div>
                    <div class="progress-bar-container">
                        <div class="progress-bar-fill" style="width: <%= stats.getProfileCompletionPercentage() %>%;"></div>
                    </div>
                </div>

                <!-- Live Metrics Grid -->
                <div class="metrics-grid">
                    <!-- Total Applications -->
                    <div class="metric-card">
                        <div class="metric-icon-box bg-indigo-light">
                            <span class="metric-icon">📑</span>
                        </div>
                        <div>
                            <p class="metric-label">Total Applied</p>
                            <h2 class="metric-value"><%= stats.getTotalApplications() %></h2>
                        </div>
                    </div>

                    <!-- Under Review -->
                    <div class="metric-card">
                        <div class="metric-icon-box bg-amber-light">
                            <span class="metric-icon">⏳</span>
                        </div>
                        <div>
                            <p class="metric-label">Under Review</p>
                            <h2 class="metric-value"><%= stats.getUnderReviewCount() %></h2>
                        </div>
                    </div>

                    <!-- Shortlisted -->
                    <div class="metric-card">
                        <div class="metric-icon-box bg-sky-light">
                            <span class="metric-icon">⭐</span>
                        </div>
                        <div>
                            <p class="metric-label">Shortlisted</p>
                            <h2 class="metric-value"><%= stats.getShortlistedCount() %></h2>
                        </div>
                    </div>

                    <!-- Upcoming Interviews -->
                    <div class="metric-card">
                        <div class="metric-icon-box bg-purple-light">
                            <span class="metric-icon">🗓️</span>
                        </div>
                        <div>
                            <p class="metric-label">Interviews</p>
                            <h2 class="metric-value"><%= stats.getUpcomingInterviewsCount() %></h2>
                        </div>
                    </div>

                    <!-- Selected / Offers -->
                    <div class="metric-card">
                        <div class="metric-icon-box bg-emerald-light">
                            <span class="metric-icon">🎉</span>
                        </div>
                        <div>
                            <p class="metric-label">Offers / Selected</p>
                            <h2 class="metric-value"><%= stats.getSelectedCount() %></h2>
                        </div>
                    </div>

                    <!-- Saved Opportunities -->
                    <div class="metric-card">
                        <div class="metric-icon-box bg-slate-light">
                            <span class="metric-icon">🔖</span>
                        </div>
                        <div>
                            <p class="metric-label">Saved Jobs</p>
                            <h2 class="metric-value"><%= stats.getSavedJobsCount() %></h2>
                        </div>
                    </div>
                </div>

                <!-- Quick Action Banners & Resource Highlights -->
                <div class="dashboard-panels-grid">
                    <div class="panel-card">
                        <div class="panel-header">
                            <h3>🚀 Quick Actions</h3>
                        </div>
                        <div class="quick-actions-list">
                            <a href="${pageContext.request.contextPath}/jobs?type=INTERNSHIP" class="action-item">
                                <div class="action-icon">💡</div>
                                <div>
                                    <h4>Find Fresh Internships</h4>
                                    <p>Discover summer and full-time internship roles for students</p>
                                </div>
                                <span class="action-arrow">→</span>
                            </a>

                            <a href="${pageContext.request.contextPath}/student/profile" class="action-item">
                                <div class="action-icon">🎯</div>
                                <div>
                                    <h4>Update Technical Skills</h4>
                                    <p>Add your proficiency in Java, PostgreSQL, React, or Python</p>
                                </div>
                                <span class="action-arrow">→</span>
                            </a>

                            <a href="${pageContext.request.contextPath}/student/profile" class="action-item">
                                <div class="action-icon">🎓</div>
                                <div>
                                    <h4>Add Education &amp; Degree</h4>
                                    <p>Showcase your university, GPA, and graduation year</p>
                                </div>
                                <span class="action-arrow">→</span>
                            </a>
                        </div>
                    </div>

                    <div class="panel-card">
                        <div class="panel-header">
                            <h3>📌 Application Pipeline Guide</h3>
                        </div>
                        <div class="pipeline-guide-list">
                            <div class="guide-step">
                                <span class="badge badge-primary">Step 1: Applied</span>
                                <p>Your resume and details are securely delivered to the recruiter.</p>
                            </div>
                            <div class="guide-step">
                                <span class="badge badge-warning">Step 2: Under Review</span>
                                <p>Hiring manager reviews qualifications and portfolio links.</p>
                            </div>
                            <div class="guide-step">
                                <span class="badge badge-info">Step 3: Shortlisted</span>
                                <p>You passed the initial screening; an interview schedule is prepared.</p>
                            </div>
                            <div class="guide-step">
                                <span class="badge badge-secondary">Step 4: Interview</span>
                                <p>Join the online meeting link or attend the scheduled round.</p>
                            </div>
                            <div class="guide-step">
                                <span class="badge badge-success">Step 5: Selected</span>
                                <p>Congratulations! Offer details will be shared.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
