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
    <title>Scheduled Interviews – SkillBridge</title>
    
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
                <a href="${pageContext.request.contextPath}/recruiter/manage-jobs" class="sidebar-link">
                    <span class="link-icon">📋</span>
                    <span>Manage Postings</span>
                </a>
                <a href="${pageContext.request.contextPath}/recruiter/applicants" class="sidebar-link">
                    <span class="link-icon">👥</span>
                    <span>Review Applicants</span>
                </a>
                <a href="${pageContext.request.contextPath}/recruiter/interviews" class="sidebar-link active">
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
                    <h1 style="font-size: 1.75rem;">Interview Management 🗓️</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">View upcoming candidate rounds and track interview outcomes</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/recruiter/applicants" class="btn btn-primary btn-sm">
                        + Schedule More Interviews
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

                <!-- Interviews List -->
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
                                        <p style="color: var(--text-secondary); font-size: 0.9rem;">
                                            Candidate: <strong><%= iv.getStudentName() %></strong> (✉️ <%= iv.getStudentEmail() %>) • Role: <strong><%= iv.getJobTitle() %></strong>
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
                                    <p style="margin-top: 0.35rem;">
                                        <strong>Location / Meeting Link:</strong> 
                                        <% if (iv.getMeetingLinkOrLocation().startsWith("http")) { %>
                                            <a href="<%= iv.getMeetingLinkOrLocation() %>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-primary" style="padding: 0.2rem 0.65rem; margin-left: 0.5rem;">
                                                🔗 Launch Meeting Link ↗
                                            </a>
                                        <% } else { %>
                                            <span style="color: var(--text-primary);"><%= iv.getMeetingLinkOrLocation() %></span>
                                        <% } %>
                                    </p>

                                    <% if (iv.getInstructions() != null && !iv.getInstructions().trim().isEmpty()) { %>
                                        <p style="margin-top: 0.5rem; color: var(--text-secondary); font-size: 0.85rem;">
                                            <strong>Candidate Instructions:</strong> <%= iv.getInstructions() %>
                                        </p>
                                    <% } %>
                                </div>

                                <!-- Action bar -->
                                <div class="interview-action-row">
                                    <form action="${pageContext.request.contextPath}/recruiter/interviews" method="POST" style="display: flex; gap: 0.5rem; align-items: center;">
                                        <input type="hidden" name="interviewId" value="<%= iv.getId() %>">
                                        <span style="font-size: 0.85rem; font-weight: 600; color: var(--text-secondary);">Update Round Status:</span>
                                        <select name="status" class="form-control" style="width: auto; padding: 0.35rem 0.75rem;">
                                            <option value="SCHEDULED" <%= iv.getStatus() == InterviewStatus.SCHEDULED ? "selected" : "" %>>Scheduled</option>
                                            <option value="COMPLETED" <%= iv.getStatus() == InterviewStatus.COMPLETED ? "selected" : "" %>>Completed</option>
                                            <option value="RESCHEDULED" <%= iv.getStatus() == InterviewStatus.RESCHEDULED ? "selected" : "" %>>Rescheduled</option>
                                            <option value="CANCELLED" <%= iv.getStatus() == InterviewStatus.CANCELLED ? "selected" : "" %>>Cancelled</option>
                                        </select>
                                        <button type="submit" class="btn btn-sm btn-primary">Update</button>
                                    </form>

                                    <div>
                                        <% if (iv.getResumeUrl() != null && !iv.getResumeUrl().isEmpty()) { %>
                                            <a href="<%= iv.getResumeUrl() %>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-outline">
                                                📄 Candidate Resume
                                            </a>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="empty-state-card">
                        <div class="empty-icon">🗓️</div>
                        <h3>No scheduled interviews</h3>
                        <p>You haven't scheduled any candidate interview rounds yet. Review applicants and click "Schedule Interview".</p>
                        <a href="${pageContext.request.contextPath}/recruiter/applicants" class="btn btn-primary" style="margin-top: 1.25rem;">Review Applicants</a>
                    </div>
                <% } %>

            </div>
        </main>
    </div>

</body>
</html>
