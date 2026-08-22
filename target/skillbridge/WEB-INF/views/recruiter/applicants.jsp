<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.*, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    List<Application> applicants = (List<Application>) request.getAttribute("applicants");
    List<Job> postedJobs = (List<Job>) request.getAttribute("postedJobs");
    Integer selectedJobId = (Integer) request.getAttribute("selectedJobId");
    String selectedStatus = (String) request.getAttribute("selectedStatus");
    if (selectedStatus == null) selectedStatus = "ALL";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Candidate Pipeline &amp; Applicants – SkillBridge</title>
    
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
                <a href="${pageContext.request.contextPath}/recruiter/applicants" class="sidebar-link active">
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
                    <h1 style="font-size: 1.75rem;">Candidate Pipeline &amp; Applicants 👥</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Review submitted student resumes, transition application stages, and schedule interviews</p>
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

                <!-- Filter Toolbar -->
                <div class="panel-card" style="padding: 1.25rem 1.5rem; margin-bottom: 2rem;">
                    <form action="${pageContext.request.contextPath}/recruiter/applicants" method="GET" style="display: flex; gap: 1rem; align-items: center; flex-wrap: wrap;">
                        <div style="flex: 1; min-width: 220px;">
                            <label class="form-label" style="margin-bottom: 0.25rem;">Filter by Opportunity</label>
                            <select name="jobId" class="form-control" onchange="this.form.submit()">
                                <option value="">-- All Job Postings --</option>
                                <% if (postedJobs != null) { %>
                                    <% for (Job j : postedJobs) { %>
                                        <option value="<%= j.getId() %>" <%= (selectedJobId != null && selectedJobId == j.getId()) ? "selected" : "" %>>
                                            <%= j.getTitle() %> (<%= j.getApplicantCount() %> applicants)
                                        </option>
                                    <% } %>
                                <% } %>
                            </select>
                        </div>

                        <div style="flex: 1; min-width: 200px;">
                            <label class="form-label" style="margin-bottom: 0.25rem;">Filter by Pipeline Status</label>
                            <select name="status" class="form-control" onchange="this.form.submit()">
                                <option value="ALL" <%= "ALL".equals(selectedStatus) ? "selected" : "" %>>All Statuses</option>
                                <option value="APPLIED" <%= "APPLIED".equals(selectedStatus) ? "selected" : "" %>>Applied</option>
                                <option value="UNDER_REVIEW" <%= "UNDER_REVIEW".equals(selectedStatus) ? "selected" : "" %>>Under Review</option>
                                <option value="SHORTLISTED" <%= "SHORTLISTED".equals(selectedStatus) ? "selected" : "" %>>Shortlisted</option>
                                <option value="INTERVIEW" <%= "INTERVIEW".equals(selectedStatus) ? "selected" : "" %>>Interview Scheduled</option>
                                <option value="SELECTED" <%= "SELECTED".equals(selectedStatus) ? "selected" : "" %>>Selected / Offer</option>
                                <option value="REJECTED" <%= "REJECTED".equals(selectedStatus) ? "selected" : "" %>>Rejected</option>
                            </select>
                        </div>

                        <div style="padding-top: 1.25rem;">
                            <a href="${pageContext.request.contextPath}/recruiter/applicants" class="btn btn-outline btn-sm">Reset Filters</a>
                        </div>
                    </form>
                </div>

                <!-- Applicants Cards List -->
                <% if (applicants != null && !applicants.isEmpty()) { %>
                    <div class="applications-container">
                        <% for (Application app : applicants) { %>
                            <div class="applicant-review-card">
                                <div class="applicant-card-header">
                                    <div style="display: flex; gap: 1rem; align-items: flex-start;">
                                        <div class="user-avatar" style="width: 48px; height: 48px; font-size: 1.25rem;">
                                            <%= app.getStudentName() != null ? app.getStudentName().substring(0, 1).toUpperCase() : "S" %>
                                        </div>
                                        <div>
                                            <h3 style="font-size: 1.25rem; margin-bottom: 0.2rem;"><%= app.getStudentName() %></h3>
                                            <p style="color: var(--text-secondary); font-size: 0.9rem;">
                                                <%= app.getStudentHeadline() != null ? app.getStudentHeadline() : "Student" %>
                                                <% if (app.getStudentCgpa() != null) { %>
                                                    • <strong style="color: var(--primary);">CGPA: <%= app.getStudentCgpa() %></strong>
                                                <% } %>
                                            </p>
                                            <p style="font-size: 0.8rem; color: var(--text-muted); margin-top: 0.25rem;">
                                                Applied for: <strong style="color: var(--text-primary);"><%= app.getJobTitle() %></strong> • 📅 <%= app.getAppliedAt().toString().substring(0, 10) %>
                                            </p>
                                        </div>
                                    </div>

                                    <div>
                                        <span class="badge <%= app.getStatus().getBadgeClass() %>" style="font-size: 0.85rem; padding: 0.45rem 0.9rem;">
                                            <%= app.getStatus().getDisplayName() %>
                                        </span>
                                    </div>
                                </div>

                                <!-- Contact & Links Row -->
                                <div class="applicant-links-row">
                                    <span style="font-size: 0.85rem; color: var(--text-secondary);">✉️ <%= app.getStudentEmail() %></span>
                                    <% if (app.getStudentPhone() != null && !app.getStudentPhone().isEmpty()) { %>
                                        <span style="font-size: 0.85rem; color: var(--text-secondary);">📞 <%= app.getStudentPhone() %></span>
                                    <% } %>
                                    <% if (app.getResumeUrl() != null && !app.getResumeUrl().isEmpty()) { %>
                                        <a href="<%= app.getResumeUrl() %>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-primary" style="padding: 0.25rem 0.75rem;">
                                            📄 View Resume
                                        </a>
                                    <% } %>
                                    <% if (app.getStudentGithub() != null && !app.getStudentGithub().isEmpty()) { %>
                                        <a href="<%= app.getStudentGithub() %>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-outline" style="padding: 0.25rem 0.75rem;">
                                            GitHub ↗
                                        </a>
                                    <% } %>
                                    <% if (app.getStudentLinkedin() != null && !app.getStudentLinkedin().isEmpty()) { %>
                                        <a href="<%= app.getStudentLinkedin() %>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-outline" style="padding: 0.25rem 0.75rem;">
                                            LinkedIn ↗
                                        </a>
                                    <% } %>
                                </div>

                                <!-- Cover Letter if any -->
                                <% if (app.getCoverLetter() != null && !app.getCoverLetter().trim().isEmpty()) { %>
                                    <div class="applicant-cover-letter">
                                        <strong>📝 Candidate Note / Cover Letter:</strong>
                                        <p style="margin-top: 0.35rem;"><%= app.getCoverLetter() %></p>
                                    </div>
                                <% } %>

                                <!-- Recruiter Pipeline Action Controls -->
                                <div class="applicant-card-footer">
                                    <form action="${pageContext.request.contextPath}/recruiter/application/status" method="POST" class="status-update-inline-form">
                                        <input type="hidden" name="applicationId" value="<%= app.getId() %>">
                                        
                                        <div style="display: flex; gap: 0.75rem; align-items: center; flex-wrap: wrap;">
                                            <span style="font-size: 0.85rem; font-weight: 600; color: var(--text-secondary);">Change Status:</span>
                                            <select name="status" class="form-control" style="width: auto; padding: 0.4rem 0.75rem;">
                                                <option value="APPLIED" <%= app.getStatus() == ApplicationStatus.APPLIED ? "selected" : "" %>>Applied</option>
                                                <option value="UNDER_REVIEW" <%= app.getStatus() == ApplicationStatus.UNDER_REVIEW ? "selected" : "" %>>Under Review</option>
                                                <option value="SHORTLISTED" <%= app.getStatus() == ApplicationStatus.SHORTLISTED ? "selected" : "" %>>Shortlisted</option>
                                                <option value="INTERVIEW" <%= app.getStatus() == ApplicationStatus.INTERVIEW ? "selected" : "" %>>Interview</option>
                                                <option value="SELECTED" <%= app.getStatus() == ApplicationStatus.SELECTED ? "selected" : "" %>>Selected / Offer</option>
                                                <option value="REJECTED" <%= app.getStatus() == ApplicationStatus.REJECTED ? "selected" : "" %>>Rejected</option>
                                            </select>

                                            <input type="text" name="notes" class="form-control" placeholder="Optional recruiter note to candidate..." value="<%= app.getNotes() != null ? app.getNotes() : "" %>" style="max-width: 320px; padding: 0.4rem 0.75rem;">

                                            <button type="submit" class="btn btn-sm btn-primary">Update Status</button>
                                        </div>
                                    </form>

                                    <div>
                                        <a href="${pageContext.request.contextPath}/recruiter/schedule-interview?applicationId=<%= app.getId() %>" class="btn btn-sm btn-outline" style="border-color: var(--secondary); color: var(--secondary); font-weight: 700;">
                                            🗓️ Schedule Interview
                                        </a>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="empty-state-card">
                        <div class="empty-icon">👥</div>
                        <h3>No candidates found</h3>
                        <p>No applications match your selected filter criteria. Adjust your filters or share your job postings with students.</p>
                        <a href="${pageContext.request.contextPath}/recruiter/manage-jobs" class="btn btn-primary" style="margin-top: 1.25rem;">View Posted Jobs</a>
                    </div>
                <% } %>

            </div>
        </main>
    </div>

</body>
</html>
