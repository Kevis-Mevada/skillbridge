<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.Interview, com.skillbridge.model.InterviewStatus, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    List<Interview> interviews = (List<Interview>) request.getAttribute("interviews");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Interview Schedule – SkillBridge</title>
    
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
                <a href="${pageContext.request.contextPath}/student/saved-jobs" class="sidebar-link">
                    <span class="link-icon">🔖</span>
                    <span>Saved Jobs</span>
                </a>
                <a href="${pageContext.request.contextPath}/student/interviews" class="sidebar-link active">
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
                    <h1 style="font-size: 1.75rem;">Interview Schedule 🗓️</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Upcoming technical rounds, meeting links, and interviewer preparation instructions</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/student/applications" class="btn btn-outline btn-sm">
                        ← View Applications
                    </a>
                </div>
            </header>

            <div class="dashboard-content">
                
                <% if (interviews != null && !interviews.isEmpty()) { %>
                    <div class="applications-container">
                        <% for (Interview iv : interviews) { %>
                            <div class="interview-item-card">
                                <div class="interview-header-row">
                                    <div>
                                        <div style="display: flex; align-items: center; gap: 0.75rem; margin-bottom: 0.35rem;">
                                            <h3 style="font-size: 1.25rem;"><%= iv.getRoundName() %></h3>
                                            <span class="badge <%= iv.getStatus().getBadgeClass() %>"><%= iv.getStatus().getDisplayName() %></span>
                                        </div>
                                        <p style="color: var(--text-secondary); font-size: 0.95rem;">
                                            Company: <strong><%= iv.getCompanyName() %></strong> • Role: <strong><%= iv.getJobTitle() %></strong>
                                        </p>
                                    </div>
                                    <div class="interview-time-box">
                                        <span style="font-size: 1.2rem; font-weight: 800; color: var(--primary);">
                                            📅 <%= iv.getInterviewDate() %>
                                        </span>
                                        <span style="font-size: 0.9rem; color: var(--text-secondary); font-weight: 600;">
                                            ⏰ <%= iv.getInterviewTime() %>
                                        </span>
                                    </div>
                                </div>

                                <div class="interview-details-box">
                                    <p><strong>Mode:</strong> <%= iv.getInterviewMode().getDisplayName() %></p>
                                    
                                    <div style="margin-top: 0.75rem; display: flex; align-items: center; gap: 0.75rem; flex-wrap: wrap;">
                                        <strong>Meeting Access:</strong>
                                        <% if (iv.getMeetingLinkOrLocation().startsWith("http")) { %>
                                            <a href="<%= iv.getMeetingLinkOrLocation() %>" target="_blank" rel="noopener noreferrer" class="btn btn-primary" style="padding: 0.4rem 1.1rem;">
                                                🔗 Join Online Meeting ↗
                                            </a>
                                        <% } else { %>
                                            <span style="color: var(--text-primary); background: #ffffff; padding: 0.35rem 0.75rem; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
                                                📍 <%= iv.getMeetingLinkOrLocation() %>
                                            </span>
                                        <% } %>
                                    </div>

                                    <% if (iv.getInstructions() != null && !iv.getInstructions().trim().isEmpty()) { %>
                                        <div class="recruiter-notes-box" style="margin-top: 1rem; margin-bottom: 0;">
                                            <strong>📌 Interviewer Instructions:</strong>
                                            <p style="margin-top: 0.25rem;"><%= iv.getInstructions() %></p>
                                        </div>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="empty-state-card">
                        <div class="empty-icon">🗓️</div>
                        <h3>No scheduled interviews yet</h3>
                        <p>When recruiters shortlist your applications and schedule interview rounds, they will appear here with direct meeting links.</p>
                        <a href="${pageContext.request.contextPath}/student/applications" class="btn btn-primary" style="margin-top: 1.25rem;">Track My Applications →</a>
                    </div>
                <% } %>

            </div>
        </main>
    </div>

</body>
</html>
