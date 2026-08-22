<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.*, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    RecruiterProfile profile = (RecruiterProfile) request.getAttribute("profile");
    RecruiterDashboardStats stats = (RecruiterDashboardStats) request.getAttribute("stats");
    if (stats == null) stats = new RecruiterDashboardStats();
    List<Job> postedJobs = (List<Job>) request.getAttribute("postedJobs");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recruiter Dashboard – <%= (profile != null) ? profile.getCompanyName() : "Employer" %> | SkillBridge</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="dashboard-body">

    <div class="dashboard-layout">
        <!-- Sidebar Navigation -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <a href="${pageContext.request.contextPath}/" class="brand-logo">
                    <span class="brand-icon">💼</span>
                    <span>SkillBridge</span>
                </a>
                <span class="badge badge-secondary" style="margin-top: 0.5rem;">Employer Portal</span>
            </div>

            <div class="sidebar-user-card">
                <div class="user-avatar" style="background: linear-gradient(135deg, #0ea5e9 0%, #3b82f6 100%);">
                    <%= (currentUser != null && currentUser.getFullName() != null) ? currentUser.getFullName().substring(0, 1).toUpperCase() : "R" %>
                </div>
                <div class="user-meta">
                    <h4><%= (profile != null && profile.getCompanyName() != null) ? profile.getCompanyName() : "Company" %></h4>
                    <p><%= (currentUser != null) ? currentUser.getFullName() : "" %></p>
                </div>
            </div>

            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/recruiter/dashboard" class="sidebar-link active">
                    <span class="link-icon">📊</span>
                    <span>Dashboard</span>
                </a>
                <a href="${pageContext.request.contextPath}/recruiter/post-job" class="sidebar-link">
                    <span class="link-icon">➕</span>
                    <span>Post New Job / Intern</span>
                </a>
                <a href="${pageContext.request.contextPath}/recruiter/manage-jobs" class="sidebar-link">
                    <span class="link-icon">📋</span>
                    <span>Manage Postings</span>
                </a>
                <a href="${pageContext.request.contextPath}/recruiter/applicants" class="sidebar-link">
                    <span class="link-icon">👥</span>
                    <span>Review Applicants</span>
                </a>
                <a href="${pageContext.request.contextPath}/recruiter/interviews" class="sidebar-link">
                    <span class="link-icon">🗓️</span>
                    <span>Interviews</span>
                </a>
                <a href="${pageContext.request.contextPath}/recruiter/profile" class="sidebar-link">
                    <span class="link-icon">🏢</span>
                    <span>Company Profile</span>
                </a>
            </nav>

            <div class="sidebar-footer">
                <a href="${pageContext.request.contextPath}/logout" class="sidebar-link logout-link">
                    <span class="link-icon">🚪</span>
                    <span>Log Out</span>
                </a>
            </div>
        </aside>

        <!-- Main Dashboard Area -->
        <main class="dashboard-main">
            <!-- Topbar -->
            <header class="dashboard-topbar">
                <div>
                    <h1 style="font-size: 1.75rem;">Recruiter Overview 🚀</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Manage candidate pipeline, job postings, and interview schedules for <%= (profile != null) ? profile.getCompanyName() : "your organization" %></p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/recruiter/post-job" class="btn btn-primary btn-sm">
                        + Post Opportunity
                    </a>
                </div>
            </header>

            <div class="dashboard-content">
                <!-- Metrics Grid -->
                <div class="metrics-grid">
                    <div class="metric-card">
                        <div class="metric-icon-box bg-indigo-light">
                            <span class="metric-icon">📢</span>
                        </div>
                        <div>
                            <p class="metric-label">Active Postings</p>
                            <h2 class="metric-value"><%= stats.getActiveJobsCount() %></h2>
                        </div>
                    </div>

                    <div class="metric-card">
                        <div class="metric-icon-box bg-sky-light">
                            <span class="metric-icon">👥</span>
                        </div>
                        <div>
                            <p class="metric-label">Total Applicants</p>
                            <h2 class="metric-value"><%= stats.getTotalApplicantsCount() %></h2>
                        </div>
                    </div>

                    <div class="metric-card">
                        <div class="metric-icon-box bg-amber-light">
                            <span class="metric-icon">⏳</span>
                        </div>
                        <div>
                            <p class="metric-label">Under Review</p>
                            <h2 class="metric-value"><%= stats.getUnderReviewCount() %></h2>
                        </div>
                    </div>

                    <div class="metric-card">
                        <div class="metric-icon-box bg-purple-light">
                            <span class="metric-icon">⭐</span>
                        </div>
                        <div>
                            <p class="metric-label">Shortlisted</p>
                            <h2 class="metric-value"><%= stats.getShortlistedCount() %></h2>
                        </div>
                    </div>

                    <div class="metric-card">
                        <div class="metric-icon-box bg-slate-light">
                            <span class="metric-icon">🗓️</span>
                        </div>
                        <div>
                            <p class="metric-label">Interviews</p>
                            <h2 class="metric-value"><%= stats.getScheduledInterviewsCount() %></h2>
                        </div>
                    </div>

                    <div class="metric-card">
                        <div class="metric-icon-box bg-emerald-light">
                            <span class="metric-icon">🎯</span>
                        </div>
                        <div>
                            <p class="metric-label">Hired / Selected</p>
                            <h2 class="metric-value"><%= stats.getHiredCount() %></h2>
                        </div>
                    </div>
                </div>

                <!-- Recent Posted Jobs Table -->
                <div class="panel-card" style="padding: 2rem;">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                        <div>
                            <h3 style="font-size: 1.25rem;">Active Job &amp; Internship Postings</h3>
                            <p style="font-size: 0.85rem; color: var(--text-secondary);">Direct access to candidate pipeline by role</p>
                        </div>
                        <a href="${pageContext.request.contextPath}/recruiter/manage-jobs" class="btn btn-outline btn-sm">View All Postings →</a>
                    </div>

                    <% if (postedJobs != null && !postedJobs.isEmpty()) { %>
                        <div style="overflow-x: auto;">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Job Title</th>
                                        <th>Type</th>
                                        <th>Location</th>
                                        <th>Applicants</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (Job j : postedJobs) { %>
                                        <tr>
                                            <td>
                                                <strong><a href="${pageContext.request.contextPath}/jobs/details?id=<%= j.getId() %>" style="color: var(--text-primary);"><%= j.getTitle() %></a></strong>
                                            </td>
                                            <td>
                                                <span class="badge badge-primary"><%= j.getJobType().getDisplayName() %></span>
                                            </td>
                                            <td><%= j.getLocation() %></td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/recruiter/applicants?jobId=<%= j.getId() %>" class="btn btn-sm btn-outline" style="font-weight: 700;">
                                                    👥 <%= j.getApplicantCount() %> Candidates
                                                </a>
                                            </td>
                                            <td>
                                                <span class="badge <%= j.isActive() ? "badge-success" : "badge-danger" %>">
                                                    <%= j.isActive() ? "Active" : "Closed" %>
                                                </span>
                                            </td>
                                            <td>
                                                <div style="display: flex; gap: 0.5rem;">
                                                    <a href="${pageContext.request.contextPath}/recruiter/edit-job?id=<%= j.getId() %>" class="btn btn-sm btn-outline" title="Edit">✏️</a>
                                                    <a href="${pageContext.request.contextPath}/recruiter/applicants?jobId=<%= j.getId() %>" class="btn btn-sm btn-primary" title="Review Applicants">View Pipeline</a>
                                                </div>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    <% } else { %>
                        <div class="empty-state-card" style="padding: 2.5rem 1rem;">
                            <div class="empty-icon">💼</div>
                            <h4>No postings created yet</h4>
                            <p>Create your first job or internship listing to start attracting student talent.</p>
                            <a href="${pageContext.request.contextPath}/recruiter/post-job" class="btn btn-primary" style="margin-top: 1rem;">+ Post New Opportunity</a>
                        </div>
                    <% } %>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
