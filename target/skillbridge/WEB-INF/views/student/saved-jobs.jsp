<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.SavedJob, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    List<SavedJob> savedJobs = (List<SavedJob>) request.getAttribute("savedJobs");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Saved Jobs &amp; Bookmarks – SkillBridge</title>
    
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
                <a href="${pageContext.request.contextPath}/student/dashboard" class="sidebar-link">
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
                <a href="${pageContext.request.contextPath}/student/saved-jobs" class="sidebar-link active">
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

        <!-- Main Content -->
        <main class="dashboard-main">
            <!-- Topbar -->
            <header class="dashboard-topbar">
                <div>
                    <h1 style="font-size: 1.75rem;">Saved Opportunities 🔖</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Your bookmarked roles to review and apply when you are ready</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/jobs" class="btn btn-outline btn-sm">
                        🔍 Explore More Jobs
                    </a>
                </div>
            </header>

            <div class="dashboard-content">
                <!-- Flash Alerts -->
                <% if (request.getAttribute("successMessage") != null) { %>
                    <div class="alert alert-success">
                        <span class="alert-icon">✅</span>
                        <span><%= request.getAttribute("successMessage") %></span>
                    </div>
                <% } %>

                <!-- Saved Jobs List -->
                <% if (savedJobs != null && !savedJobs.isEmpty()) { %>
                    <div class="saved-jobs-grid">
                        <% for (SavedJob sj : savedJobs) { %>
                            <div class="job-card">
                                <div class="job-card-header">
                                    <div class="company-avatar">
                                        <%= (sj.getCompanyName() != null && !sj.getCompanyName().isEmpty()) ? sj.getCompanyName().substring(0, 1).toUpperCase() : "C" %>
                                    </div>
                                    <div class="job-card-title-group">
                                        <h3 class="job-title">
                                            <a href="${pageContext.request.contextPath}/jobs/details?id=<%= sj.getJobId() %>"><%= sj.getJobTitle() %></a>
                                        </h3>
                                        <p class="company-name"><%= sj.getCompanyName() %></p>
                                    </div>
                                </div>

                                <div class="job-badges-row">
                                    <span class="badge badge-primary"><%= sj.getJobType().getDisplayName() %></span>
                                    <span class="badge badge-info">📍 <%= sj.getLocation() %></span>
                                    <span class="badge badge-secondary">🎯 <%= sj.getExperienceLevel().getDisplayName() %></span>
                                </div>

                                <div class="job-card-footer">
                                    <div class="salary-tag">
                                        <% if (sj.getSalaryMin() != null && sj.getSalaryMin().doubleValue() > 0) { %>
                                            <span>💰 $<%= sj.getSalaryMin().intValue() %> - $<%= sj.getSalaryMax().intValue() %></span>
                                        <% } else { %>
                                            <span>💰 Competitive / Stipend</span>
                                        <% } %>
                                    </div>

                                    <div style="display: flex; gap: 0.5rem; align-items: center;">
                                        <form action="${pageContext.request.contextPath}/student/save-job" method="POST" style="display: inline;">
                                            <input type="hidden" name="jobId" value="<%= sj.getJobId() %>">
                                            <input type="hidden" name="action" value="remove">
                                            <button type="submit" class="btn btn-sm btn-outline" style="color: var(--danger); border-color: var(--border-color);" title="Remove Bookmark">
                                                🗑️
                                            </button>
                                        </form>
                                        <a href="${pageContext.request.contextPath}/student/apply?jobId=<%= sj.getJobId() %>" class="btn btn-sm btn-primary">
                                            Apply Now →
                                        </a>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="empty-state-card">
                        <div class="empty-icon">🔖</div>
                        <h3>No saved opportunities</h3>
                        <p>When browsing jobs, click the "Save Job" button to bookmark positions you want to apply for later.</p>
                        <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary" style="margin-top: 1.25rem;">Explore Jobs Now →</a>
                    </div>
                <% } %>

            </div>
        </main>
    </div>

</body>
</html>
