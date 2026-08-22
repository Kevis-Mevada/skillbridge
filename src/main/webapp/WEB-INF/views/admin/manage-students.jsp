<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.StudentProfile, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    List<StudentProfile> students = (List<StudentProfile>) request.getAttribute("students");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Students – SkillBridge Admin</title>
    
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
                <a href="${pageContext.request.contextPath}/admin/students" class="sidebar-link active">
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
                    <h1 style="font-size: 1.75rem;">Manage Student Accounts 🎓</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">View candidate profiles, toggle account active status, or delete accounts</p>
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
                    <% if (students != null && !students.isEmpty()) { %>
                        <div style="overflow-x: auto;">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Student Name</th>
                                        <th>Contact Email &amp; Phone</th>
                                        <th>Academic Info</th>
                                        <th>Resume / Links</th>
                                        <th>Account Status</th>
                                        <th style="text-align: right;">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (StudentProfile sp : students) { %>
                                        <tr>
                                            <td>
                                                <strong><%= sp.getFullName() %></strong>
                                                <p style="font-size: 0.75rem; color: var(--text-secondary);"><%= sp.getHeadline() != null ? sp.getHeadline() : "Student" %></p>
                                            </td>
                                            <td>
                                                <span>✉️ <%= sp.getEmail() %></span>
                                                <% if (sp.getPhone() != null && !sp.getPhone().isEmpty()) { %>
                                                    <p style="font-size: 0.75rem; color: var(--text-muted);">📞 <%= sp.getPhone() %></p>
                                                <% } %>
                                            </td>
                                            <td>
                                                <% if (sp.getCgpa() != null) { %>
                                                    <strong style="color: var(--primary);">CGPA: <%= sp.getCgpa() %></strong>
                                                <% } else { %>
                                                    <span style="color: var(--text-muted);">Not specified</span>
                                                <% } %>
                                            </td>
                                            <td>
                                                <div style="display: flex; gap: 0.35rem;">
                                                    <% if (sp.getResumeUrl() != null && !sp.getResumeUrl().isEmpty()) { %>
                                                        <a href="<%= sp.getResumeUrl() %>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-outline" style="padding: 0.2rem 0.5rem;" title="Resume">📄</a>
                                                    <% } %>
                                                    <% if (sp.getGithubUrl() != null && !sp.getGithubUrl().isEmpty()) { %>
                                                        <a href="<%= sp.getGithubUrl() %>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-outline" style="padding: 0.2rem 0.5rem;" title="GitHub">GH</a>
                                                    <% } %>
                                                    <% if (sp.getLinkedinUrl() != null && !sp.getLinkedinUrl().isEmpty()) { %>
                                                        <a href="<%= sp.getLinkedinUrl() %>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-outline" style="padding: 0.2rem 0.5rem;" title="LinkedIn">IN</a>
                                                    <% } %>
                                                </div>
                                            </td>
                                            <td>
                                                <span class="badge <%= sp.isActive() ? "badge-success" : "badge-danger" %>">
                                                    <%= sp.isActive() ? "Active" : "Deactivated" %>
                                                </span>
                                            </td>
                                            <td style="text-align: right;">
                                                <div style="display: flex; gap: 0.5rem; justify-content: flex-end;">
                                                    <form action="${pageContext.request.contextPath}/admin/students" method="POST" style="display: inline;">
                                                        <input type="hidden" name="action" value="toggle">
                                                        <input type="hidden" name="userId" value="<%= sp.getUserId() %>">
                                                        <input type="hidden" name="currentStatus" value="<%= sp.isActive() %>">
                                                        <button type="submit" class="btn btn-sm btn-outline">
                                                            <%= sp.isActive() ? "Deactivate" : "Activate" %>
                                                        </button>
                                                    </form>
                                                    <form action="${pageContext.request.contextPath}/admin/students" method="POST" onsubmit="return confirm('Are you sure you want to permanently delete this student account?');" style="display: inline;">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="userId" value="<%= sp.getUserId() %>">
                                                        <button type="submit" class="btn btn-sm btn-outline" style="color: var(--danger); border-color: var(--border-color);" title="Delete Student">
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
                            <div class="empty-icon">🎓</div>
                            <h3>No students registered yet</h3>
                        </div>
                    <% } %>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
