<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.Job, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    List<Job> jobs = (List<Job>) request.getAttribute("jobs");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Moderate Job Postings – SkillBridge Admin</title>
    
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
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link">
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
                <a href="${pageContext.request.contextPath}/admin/jobs" class="sidebar-link active">
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
                    <h1 style="font-size: 1.75rem;">Moderate Job &amp; Internship Postings 📋</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Review all listings, toggle active visibility, or remove spam postings</p>
                </div>
            </header>

            <div class="dashboard-content">
                <!-- Flash Alerts -->
                <% if (request.getAttribute("errorMessage") != null) { %>
                    <div class="alert alert-danger">
                        <span class="alert-icon">⚠️</span>
                        <span><%= request.getAttribute("errorMessage") %></span>
                    </div>
                <% } %>
                <% if (request.getAttribute("successMessage") != null) { %>
                    <div class="alert alert-success">
                        <span class="alert-icon">✅</span>
                        <span><%= request.getAttribute("successMessage") %></span>
                    </div>
                <% } %>

                <div class="panel-card" style="padding: 2rem;">
                    <% if (jobs != null && !jobs.isEmpty()) { %>
                        <div style="overflow-x: auto;">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Job Title &amp; Location</th>
                                        <th>Company / Recruiter</th>
                                        <th>Type &amp; Experience</th>
                                        <th>Applicants</th>
                                        <th>Status</th>
                                        <th style="text-align: right;">Moderation Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (Job j : jobs) { %>
                                        <tr>
                                            <td>
                                                <h4 style="font-size: 1rem; margin-bottom: 0.2rem;">
                                                    <a href="${pageContext.request.contextPath}/jobs/details?id=<%= j.getId() %>" style="color: var(--text-primary);"><%= j.getTitle() %></a>
                                                </h4>
                                                <span style="font-size: 0.75rem; color: var(--text-muted);">📍 <%= j.getLocation() %></span>
                                            </td>
                                            <td>
                                                <strong><%= j.getCompanyName() %></strong>
                                                <p style="font-size: 0.75rem; color: var(--text-muted);"><%= j.getRecruiterEmail() %></p>
                                            </td>
                                            <td>
                                                <span class="badge badge-primary"><%= j.getJobType().getDisplayName() %></span>
                                                <p style="font-size: 0.75rem; color: var(--text-secondary); margin-top: 0.2rem;"><%= j.getExperienceLevel().getDisplayName() %></p>
                                            </td>
                                            <td><%= j.getApplicantCount() %> Submissions</td>
                                            <td>
                                                <span class="badge <%= j.isActive() ? "badge-success" : "badge-danger" %>">
                                                    <%= j.isActive() ? "Active" : "Closed" %>
                                                </span>
                                            </td>
                                            <td style="text-align: right;">
                                                <div style="display: flex; gap: 0.5rem; justify-content: flex-end;">
                                                    <form action="${pageContext.request.contextPath}/admin/jobs" method="POST" style="display: inline;">
                                                        <input type="hidden" name="action" value="toggle">
                                                        <input type="hidden" name="jobId" value="<%= j.getId() %>">
                                                        <input type="hidden" name="currentStatus" value="<%= j.isActive() %>">
                                                        <button type="submit" class="btn btn-sm btn-outline">
                                                            <%= j.isActive() ? "Deactivate" : "Activate" %>
                                                        </button>
                                                    </form>
                                                    <form action="${pageContext.request.contextPath}/admin/jobs" method="POST" onsubmit="return confirm('Are you sure you want to permanently delete this job?');" style="display: inline;">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="jobId" value="<%= j.getId() %>">
                                                        <button type="submit" class="btn btn-sm btn-outline" style="color: var(--danger); border-color: var(--border-color);" title="Delete Job">
                                                            🗑️
                                                        </button>
                                                    </form>
                                                </div>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    <% } else { %>
                        <div class="empty-state-card">
                            <div class="empty-icon">📋</div>
                            <h3>No job postings available</h3>
                        </div>
                    <% } %>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
