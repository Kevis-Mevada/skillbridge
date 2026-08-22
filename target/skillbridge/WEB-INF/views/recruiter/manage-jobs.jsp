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
    <title>Manage Job Postings – SkillBridge</title>
    
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
                    <h4><%= (currentUser != null) ? currentUser.getFullName() : "Recruiter" %></h4>
                    <p><%= (currentUser != null) ? currentUser.getEmail() : "" %></p>
                </div>
            </div>

            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/recruiter/dashboard" class="sidebar-link">
                    <span class="link-icon">📊</span>
                    <span>Dashboard</span>
                </a>
                <a href="${pageContext.request.contextPath}/recruiter/post-job" class="sidebar-link">
                    <span class="link-icon">➕</span>
                    <span>Post New Job / Intern</span>
                </a>
                <a href="${pageContext.request.contextPath}/recruiter/manage-jobs" class="sidebar-link active">
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

        <!-- Main Content -->
        <main class="dashboard-main">
            <!-- Topbar -->
            <header class="dashboard-topbar">
                <div>
                    <h1 style="font-size: 1.75rem;">Manage Job Postings 📋</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">View, edit, close, or delete your active opportunity listings</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/recruiter/post-job" class="btn btn-primary btn-sm">
                        + Post New Opportunity
                    </a>
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
                                        <th>Job Title</th>
                                        <th>Type &amp; Experience</th>
                                        <th>Location</th>
                                        <th>Applicants</th>
                                        <th>Deadline</th>
                                        <th>Status</th>
                                        <th style="text-align: right;">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (Job j : jobs) { %>
                                        <tr>
                                            <td>
                                                <h4 style="font-size: 1rem; margin-bottom: 0.2rem;">
                                                    <a href="${pageContext.request.contextPath}/jobs/details?id=<%= j.getId() %>" style="color: var(--text-primary);"><%= j.getTitle() %></a>
                                                </h4>
                                                <span style="font-size: 0.75rem; color: var(--text-muted);">Posted: <%= j.getCreatedAt().toString().substring(0, 10) %></span>
                                            </td>
                                            <td>
                                                <div style="display: flex; flex-direction: column; gap: 0.25rem;">
                                                    <span class="badge badge-primary" style="width: fit-content;"><%= j.getJobType().getDisplayName() %></span>
                                                    <span style="font-size: 0.75rem; color: var(--text-secondary);"><%= j.getExperienceLevel().getDisplayName() %></span>
                                                </div>
                                            </td>
                                            <td><%= j.getLocation() %></td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/recruiter/applicants?jobId=<%= j.getId() %>" class="btn btn-sm btn-outline" style="font-weight: 700;">
                                                    👥 <%= j.getApplicantCount() %> Review
                                                </a>
                                            </td>
                                            <td>
                                                <%= j.getDeadline() != null ? j.getDeadline() : "<span style='color: var(--text-muted);'>No Deadline</span>" %>
                                            </td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/recruiter/manage-jobs" method="POST" style="display: inline;">
                                                    <input type="hidden" name="action" value="toggle">
                                                    <input type="hidden" name="jobId" value="<%= j.getId() %>">
                                                    <input type="hidden" name="currentStatus" value="<%= j.isActive() %>">
                                                    <button type="submit" class="badge <%= j.isActive() ? "badge-success" : "badge-danger" %>" style="cursor: pointer; border: none;" title="Click to toggle status">
                                                        <%= j.isActive() ? "● Active" : "○ Closed" %>
                                                    </button>
                                                </form>
                                            </td>
                                            <td style="text-align: right;">
                                                <div style="display: flex; gap: 0.5rem; justify-content: flex-end;">
                                                    <a href="${pageContext.request.contextPath}/recruiter/edit-job?id=<%= j.getId() %>" class="btn btn-sm btn-outline" title="Edit Job">
                                                        ✏️ Edit
                                                    </a>
                                                    <form action="${pageContext.request.contextPath}/recruiter/manage-jobs" method="POST" onsubmit="return confirm('Are you sure you want to delete this job posting permanently?');" style="display: inline;">
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
                            <div class="empty-icon">📂</div>
                            <h3>No job postings found</h3>
                            <p>You haven't posted any jobs or internships yet. Click the button below to start hiring.</p>
                            <a href="${pageContext.request.contextPath}/recruiter/post-job" class="btn btn-primary" style="margin-top: 1.25rem;">+ Post New Opportunity</a>
                        </div>
                    <% } %>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
