<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.Job, com.skillbridge.model.StudentProfile" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    Job job = (Job) request.getAttribute("job");
    StudentProfile profile = (StudentProfile) request.getAttribute("profile");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Submit Application – <%= (job != null) ? job.getTitle() : "Job" %> | SkillBridge</title>
    
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

        <!-- Main Form Area -->
        <main class="dashboard-main">
            <!-- Topbar -->
            <header class="dashboard-topbar">
                <div>
                    <h1 style="font-size: 1.75rem;">Apply for Opportunity 🚀</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Review your application details before submitting to <%= (job != null) ? job.getCompanyName() : "Employer" %></p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/jobs/details?id=<%= (job != null) ? job.getId() : "" %>" class="btn btn-outline btn-sm">← Back to Job Details</a>
                </div>
            </header>

            <div class="dashboard-content">
                
                <% if (job != null) { %>
                    <!-- Job Summary Card -->
                    <div class="apply-job-header-card">
                        <div style="display: flex; gap: 1rem; align-items: center;">
                            <div class="company-avatar">
                                <%= (job.getCompanyName() != null && !job.getCompanyName().isEmpty()) ? job.getCompanyName().substring(0, 1).toUpperCase() : "C" %>
                            </div>
                            <div>
                                <h2 style="font-size: 1.4rem; margin-bottom: 0.2rem;"><%= job.getTitle() %></h2>
                                <p style="color: var(--text-secondary); font-size: 0.9rem;"><%= job.getCompanyName() %> • 📍 <%= job.getLocation() %> • <span class="badge badge-primary"><%= job.getJobType().getDisplayName() %></span></p>
                            </div>
                        </div>
                    </div>

                    <!-- Application Submission Form -->
                    <div class="profile-section-card">
                        <div class="section-card-header">
                            <h3>Candidate Submission Details</h3>
                            <p>Verify your contact information and attach your resume link</p>
                        </div>

                        <form action="${pageContext.request.contextPath}/student/apply" method="POST" class="profile-form">
                            <input type="hidden" name="jobId" value="<%= job.getId() %>">

                            <div class="form-row">
                                <div class="form-group">
                                    <label class="form-label">Applicant Name</label>
                                    <input type="text" class="form-control" value="<%= (currentUser != null) ? currentUser.getFullName() : "" %>" disabled>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">Applicant Email</label>
                                    <input type="email" class="form-control" value="<%= (currentUser != null) ? currentUser.getEmail() : "" %>" disabled>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="resumeUrl">Resume URL / Cloud Link *</label>
                                <input type="url" 
                                       id="resumeUrl" 
                                       name="resumeUrl" 
                                       class="form-control" 
                                       placeholder="https://drive.google.com/file/d/your-resume" 
                                       value="<%= (profile != null && profile.getResumeUrl() != null) ? profile.getResumeUrl() : "" %>" 
                                       required>
                                <small style="color: var(--text-muted); font-size: 0.8rem; margin-top: 0.25rem;">
                                    Make sure the link sharing permissions are set to "Anyone with the link can view".
                                </small>
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="coverLetter">Cover Letter / Note to Recruiter (Optional)</label>
                                <textarea id="coverLetter" 
                                          name="coverLetter" 
                                          class="form-control" 
                                          rows="5" 
                                          placeholder="Explain why you are a great fit for this role, key projects you've built, and what excites you about <%= job.getCompanyName() %>..."></textarea>
                            </div>

                            <div style="display: flex; gap: 1rem; align-items: center; margin-top: 1rem;">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    Submit Application 🚀
                                </button>
                                <a href="${pageContext.request.contextPath}/jobs/details?id=<%= job.getId() %>" class="btn btn-outline">
                                    Cancel
                                </a>
                            </div>
                        </form>
                    </div>
                <% } %>

            </div>
        </main>
    </div>

</body>
</html>
