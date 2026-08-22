<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.Application, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    List<Application> applications = (List<Application>) request.getAttribute("applications");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Platform Applications – SkillBridge Admin</title>
    
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
                <a href="${pageContext.request.contextPath}/admin/jobs" class="sidebar-link">
                    <span class="link-icon">📋</span>
                    <span>Moderate Jobs</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/applications" class="sidebar-link active">
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
                    <h1 style="font-size: 1.75rem;">Platform Applications Pipeline 📑</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Global overview of student submissions and recruiter response statuses</p>
                </div>
            </header>

            <div class="dashboard-content">
                <div class="panel-card" style="padding: 2rem;">
                    <% if (applications != null && !applications.isEmpty()) { %>
                        <div style="overflow-x: auto;">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Applicant Name</th>
                                        <th>Target Role &amp; Company</th>
                                        <th>Applied Date</th>
                                        <th>Status</th>
                                        <th>Resume Link</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (Application app : applications) { %>
                                        <tr>
                                            <td>
                                                <strong><%= app.getStudentName() %></strong>
                                                <p style="font-size: 0.75rem; color: var(--text-muted);">✉️ <%= app.getStudentEmail() %></p>
                                            </td>
                                            <td>
                                                <strong><%= app.getJobTitle() %></strong>
                                                <p style="font-size: 0.75rem; color: var(--text-secondary);">🏢 <%= app.getCompanyName() %> • 📍 <%= app.getJobLocation() %></p>
                                            </td>
                                            <td>
                                                <%= app.getAppliedAt().toString().substring(0, 10) %>
                                            </td>
                                            <td>
                                                <span class="badge <%= app.getStatus().getBadgeClass() %>">
                                                    <%= app.getStatus().getDisplayName() %>
                                                </span>
                                            </td>
                                            <td>
                                                <% if (app.getResumeUrl() != null && !app.getResumeUrl().isEmpty()) { %>
                                                    <a href="<%= app.getResumeUrl() %>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-outline">
                                                        📄 View Resume ↗
                                                    </a>
                                                <% } else { %>
                                                    <span style="color: var(--text-muted);">No Link</span>
                                                <% } %>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    <% } else { %>
                        <div class="empty-state-card">
                            <div class="empty-icon">📑</div>
                            <h3>No applications submitted yet</h3>
                        </div>
                    <% } %>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
