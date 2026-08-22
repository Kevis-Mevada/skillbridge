<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.AdminDashboardStats, com.skillbridge.model.Job, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    AdminDashboardStats stats = (AdminDashboardStats) request.getAttribute("stats");
    if (stats == null) stats = new AdminDashboardStats();
    List<Job> recentJobs = (List<Job>) request.getAttribute("recentJobs");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard – SkillBridge System Governance</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="dashboard-body">

    <div class="dashboard-layout">
        <!-- Sidebar Navigation -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <a href="${pageContext.request.contextPath}/" class="brand-logo">
                    <span class="brand-icon" style="background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);">🛡️</span>
                    <span>SkillBridge</span>
                </a>
                <span class="badge badge-secondary" style="margin-top: 0.5rem; background: var(--accent-light); color: var(--accent);">Admin Governance</span>
            </div>

            <div class="sidebar-user-card">
                <div class="user-avatar" style="background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);">
                    A
                </div>
                <div class="user-meta">
                    <h4><%= (currentUser != null) ? currentUser.getFullName() : "Administrator" %></h4>
                    <p>System Superuser</p>
                </div>
            </div>

            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link active">
                    <span class="link-icon">📊</span>
                    <span>Admin Overview</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/students" class="sidebar-link">
                    <span class="link-icon">🎓</span>
                    <span>Manage Students</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/recruiters" class="sidebar-link">
                    <span class="link-icon">🏢</span>
                    <span>Manage Employers</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/jobs" class="sidebar-link">
                    <span class="link-icon">📋</span>
                    <span>Moderate Jobs</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/applications" class="sidebar-link">
                    <span class="link-icon">📑</span>
                    <span>All Applications</span>
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
                    <h1 style="font-size: 1.75rem;">System Governance &amp; Analytics 🛡️</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Platform-wide user metrics, active opportunities, and moderation controls</p>
                </div>
            </header>

            <div class="dashboard-content">
                <!-- Platform Metrics Grid -->
                <div class="metrics-grid">
                    <div class="metric-card">
                        <div class="metric-icon-box bg-indigo-light">
                            <span class="metric-icon">🎓</span>
                        </div>
                        <div>
                            <p class="metric-label">Registered Students</p>
                            <h2 class="metric-value"><%= stats.getTotalStudents() %></h2>
                        </div>
                    </div>

                    <div class="metric-card">
                        <div class="metric-icon-box bg-sky-light">
                            <span class="metric-icon">🏢</span>
                        </div>
                        <div>
                            <p class="metric-label">Hiring Companies</p>
                            <h2 class="metric-value"><%= stats.getTotalRecruiters() %></h2>
                        </div>
                    </div>

                    <div class="metric-card">
                        <div class="metric-icon-box bg-emerald-light">
                            <span class="metric-icon">📢</span>
                        </div>
                        <div>
                            <p class="metric-label">Active Job Openings</p>
                            <h2 class="metric-value"><%= stats.getActiveJobs() %></h2>
                        </div>
                    </div>

                    <div class="metric-card">
                        <div class="metric-icon-box bg-amber-light">
                            <span class="metric-icon">📑</span>
                        </div>
                        <div>
                            <p class="metric-label">Total Submissions</p>
                            <h2 class="metric-value"><%= stats.getTotalApplications() %></h2>
                        </div>
                    </div>

                    <div class="metric-card">
                        <div class="metric-icon-box bg-purple-light">
                            <span class="metric-icon">🗓️</span>
                        </div>
                        <div>
                            <p class="metric-label">Interviews Held</p>
                            <h2 class="metric-value"><%= stats.getTotalInterviews() %></h2>
                        </div>
                    </div>

                    <div class="metric-card">
                        <div class="metric-icon-box bg-slate-light">
                            <span class="metric-icon">📦</span>
                        </div>
                        <div>
                            <p class="metric-label">Total Postings</p>
                            <h2 class="metric-value"><%= stats.getTotalJobs() %></h2>
                        </div>
                    </div>
                </div>

                <!-- Moderate Platform Jobs Table -->
                <div class="panel-card" style="padding: 2rem;">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                        <div>
                            <h3 style="font-size: 1.25rem;">Live Opportunities Across Platform</h3>
                            <p style="font-size: 0.85rem; color: var(--text-secondary);">Direct moderation and status control over recruiter listings</p>
                        </div>
                        <a href="${pageContext.request.contextPath}/admin/jobs" class="btn btn-outline btn-sm">Manage All Postings →</a>
                    </div>

                    <% if (recentJobs != null && !recentJobs.isEmpty()) { %>
                        <div style="overflow-x: auto;">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Job Title</th>
                                        <th>Company / Employer</th>
                                        <th>Type</th>
                                        <th>Applicants</th>
                                        <th>Status</th>
                                        <th style="text-align: right;">Moderation Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (Job j : recentJobs) { %>
                                        <tr>
                                            <td>
                                                <strong><a href="${pageContext.request.contextPath}/jobs/details?id=<%= j.getId() %>" style="color: var(--text-primary);"><%= j.getTitle() %></a></strong>
                                                <p style="font-size: 0.75rem; color: var(--text-muted);">📍 <%= j.getLocation() %></p>
                                            </td>
                                            <td>
                                                <strong><%= j.getCompanyName() %></strong>
                                                <p style="font-size: 0.75rem; color: var(--text-muted);"><%= j.getRecruiterEmail() %></p>
                                            </td>
                                            <td>
                                                <span class="badge badge-primary"><%= j.getJobType().getDisplayName() %></span>
                                            </td>
                                            <td><%= j.getApplicantCount() %> Submissions</td>
                                            <td>
                                                <span class="badge <%= j.isActive() ? "badge-success" : "badge-danger" %>">
                                                    <%= j.isActive() ? "Active" : "Closed" %>
                                                </span>
                                            </td>
                                            <td style="text-align: right;">
                                                <form action="${pageContext.request.contextPath}/admin/jobs" method="POST" style="display: inline;">
                                                    <input type="hidden" name="action" value="toggle">
                                                    <input type="hidden" name="jobId" value="<%= j.getId() %>">
                                                    <input type="hidden" name="currentStatus" value="<%= j.isActive() %>">
                                                    <button type="submit" class="btn btn-sm btn-outline">
                                                        <%= j.isActive() ? "Deactivate" : "Activate" %>
                                                    </button>
                                                </form>
                                                <form action="${pageContext.request.contextPath}/admin/jobs" method="POST" onsubmit="return confirm('Are you sure you want to permanently remove this job posting?');" style="display: inline;">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="jobId" value="<%= j.getId() %>">
                                                    <button type="submit" class="btn btn-sm btn-outline" style="color: var(--danger); border-color: var(--border-color);">
                                                        Delete
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    <% } %>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
