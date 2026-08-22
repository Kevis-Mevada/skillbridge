<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.Application, com.skillbridge.model.ApplicationStatus, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    List<Application> applications = (List<Application>) request.getAttribute("applications");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Applications – SkillBridge</title>
    
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
                <a href="${pageContext.request.contextPath}/student/applications" class="sidebar-link active">
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

        <!-- Main Content -->
        <main class="dashboard-main">
            <!-- Topbar -->
            <header class="dashboard-topbar">
                <div>
                    <h1 style="font-size: 1.75rem;">Track Job Applications 📄</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Real-time status updates from recruiters across all your submissions</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary btn-sm">
                        + Apply for More Roles
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

                <!-- Applications List -->
                <% if (applications != null && !applications.isEmpty()) { %>
                    <div class="applications-container">
                        <% for (Application app : applications) { %>
                            <div class="application-tracker-card">
                                <div class="app-card-header">
                                    <div>
                                        <h3 style="font-size: 1.25rem; margin-bottom: 0.25rem;">
                                            <a href="${pageContext.request.contextPath}/jobs/details?id=<%= app.getJobId() %>" style="color: var(--text-primary);">
                                                <%= app.getJobTitle() %>
                                            </a>
                                        </h3>
                                        <p style="color: var(--text-secondary); font-size: 0.9rem;">
                                            <strong><%= app.getCompanyName() %></strong> • 📍 <%= app.getJobLocation() %> • <%= app.getJobType().getDisplayName() %>
                                        </p>
                                    </div>
                                    <div style="text-align: right;">
                                        <span class="badge <%= app.getStatus().getBadgeClass() %>" style="font-size: 0.85rem; padding: 0.45rem 0.9rem;">
                                            <%= app.getStatus().getDisplayName() %>
                                        </span>
                                        <p style="color: var(--text-muted); font-size: 0.75rem; margin-top: 0.35rem;">
                                            Applied: <%= app.getAppliedAt().toString().substring(0, 10) %>
                                        </p>
                                    </div>
                                </div>

                                <!-- Visual Pipeline Tracker Steps -->
                                <div class="pipeline-progress-bar">
                                    <%
                                        ApplicationStatus s = app.getStatus();
                                        int stepIndex = 1;
                                        if (s == ApplicationStatus.UNDER_REVIEW) stepIndex = 2;
                                        else if (s == ApplicationStatus.SHORTLISTED) stepIndex = 3;
                                        else if (s == ApplicationStatus.INTERVIEW) stepIndex = 4;
                                        else if (s == ApplicationStatus.SELECTED) stepIndex = 5;
                                        else if (s == ApplicationStatus.REJECTED) stepIndex = 0;
                                    %>
                                    <div class="track-step <%= (stepIndex >= 1) ? "completed" : "" %>">
                                        <div class="track-dot">1</div>
                                        <span>Applied</span>
                                    </div>
                                    <div class="track-step-line <%= (stepIndex >= 2) ? "active" : "" %>"></div>

                                    <div class="track-step <%= (stepIndex >= 2) ? "completed" : (stepIndex == 1 ? "current" : "") %>">
                                        <div class="track-dot">2</div>
                                        <span>Under Review</span>
                                    </div>
                                    <div class="track-step-line <%= (stepIndex >= 3) ? "active" : "" %>"></div>

                                    <div class="track-step <%= (stepIndex >= 3) ? "completed" : (stepIndex == 2 ? "current" : "") %>">
                                        <div class="track-dot">3</div>
                                        <span>Shortlisted</span>
                                    </div>
                                    <div class="track-step-line <%= (stepIndex >= 4) ? "active" : "" %>"></div>

                                    <div class="track-step <%= (stepIndex >= 4) ? "completed" : (stepIndex == 3 ? "current" : "") %>">
                                        <div class="track-dot">4</div>
                                        <span>Interview</span>
                                    </div>
                                    <div class="track-step-line <%= (stepIndex >= 5) ? "active" : "" %>"></div>

                                    <div class="track-step <%= (stepIndex == 5) ? "completed" : (s == ApplicationStatus.REJECTED ? "rejected" : "") %>">
                                        <div class="track-dot"><%= (s == ApplicationStatus.REJECTED) ? "✕" : "5" %></div>
                                        <span><%= (s == ApplicationStatus.REJECTED) ? "Rejected" : "Selected" %></span>
                                    </div>
                                </div>

                                <!-- Recruiter Notes / Instructions if any -->
                                <% if (app.getNotes() != null && !app.getNotes().trim().isEmpty()) { %>
                                    <div class="recruiter-notes-box">
                                        <strong>💬 Recruiter Note:</strong> <%= app.getNotes() %>
                                    </div>
                                <% } %>

                                <!-- Actions Bar -->
                                <div class="app-card-actions">
                                    <div style="display: flex; gap: 0.75rem; align-items: center;">
                                        <% if (app.getResumeUrl() != null && !app.getResumeUrl().isEmpty()) { %>
                                            <a href="<%= app.getResumeUrl() %>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-outline">
                                                📄 Attached Resume
                                            </a>
                                        <% } %>
                                        <a href="${pageContext.request.contextPath}/jobs/details?id=<%= app.getJobId() %>" class="btn btn-sm btn-outline">
                                            View Job Posting
                                        </a>
                                    </div>

                                    <!-- Withdraw Option if still pending -->
                                    <% if (app.getStatus() == ApplicationStatus.APPLIED || app.getStatus() == ApplicationStatus.UNDER_REVIEW) { %>
                                        <form action="${pageContext.request.contextPath}/student/applications" method="POST" onsubmit="return confirm('Are you sure you want to withdraw your application?');">
                                            <input type="hidden" name="action" value="withdraw">
                                            <input type="hidden" name="applicationId" value="<%= app.getId() %>">
                                            <button type="submit" class="btn btn-sm btn-outline" style="color: var(--danger); border-color: var(--danger);">
                                                Withdraw
                                            </button>
                                        </form>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="empty-state-card">
                        <div class="empty-icon">📂</div>
                        <h3>No applications yet</h3>
                        <p>You haven't submitted any job or internship applications. Explore open positions to kickstart your career!</p>
                        <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary" style="margin-top: 1.25rem;">Browse Open Jobs →</a>
                    </div>
                <% } %>

            </div>
        </main>
    </div>

</body>
</html>
