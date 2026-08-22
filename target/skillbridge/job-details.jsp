<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.*, java.util.List" %>
<%
    Job job = (Job) request.getAttribute("job");
    User currentUser = (User) session.getAttribute("currentUser");
    Boolean hasApplied = (Boolean) request.getAttribute("hasApplied");
    if (hasApplied == null) hasApplied = false;
    String appStatus = (String) request.getAttribute("applicationStatus");
    Boolean isSaved = (Boolean) request.getAttribute("isSaved");
    if (isSaved == null) isSaved = false;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= (job != null) ? job.getTitle() + " at " + job.getCompanyName() : "Job Details" %> – SkillBridge</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <!-- Header Navigation -->
    <header class="navbar">
        <div class="container nav-container">
            <a href="${pageContext.request.contextPath}/" class="brand-logo">
                <span class="brand-icon">⚡</span>
                <span>SkillBridge</span>
            </a>

            <nav class="nav-links">
                <a href="${pageContext.request.contextPath}/" class="nav-link">Home</a>
                <a href="${pageContext.request.contextPath}/jobs" class="nav-link active">Explore Jobs</a>
                <% if (currentUser != null && currentUser.getRole() == Role.STUDENT) { %>
                    <a href="${pageContext.request.contextPath}/student/dashboard" class="nav-link">Dashboard</a>
                    <a href="${pageContext.request.contextPath}/student/applications" class="nav-link">My Applications</a>
                <% } %>
            </nav>

            <div class="nav-actions">
                <a href="${pageContext.request.contextPath}/jobs" class="btn btn-outline btn-sm">← Back to Listings</a>
                <% if (currentUser == null) { %>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-sm">Log In</a>
                <% } %>
            </div>
        </div>
    </header>

    <% if (job != null) { %>
        <main class="container section-padding" style="padding-top: 2rem;">
            
            <!-- Top Hero Overview Banner -->
            <div class="job-detail-hero-card">
                <div class="job-detail-header-flex">
                    <div style="display: flex; gap: 1.25rem; align-items: center;">
                        <div class="company-avatar-large">
                            <%= (job.getCompanyName() != null && !job.getCompanyName().isEmpty()) ? job.getCompanyName().substring(0, 1).toUpperCase() : "C" %>
                        </div>
                        <div>
                            <h1 style="font-size: 2rem; margin-bottom: 0.25rem;"><%= job.getTitle() %></h1>
                            <p style="font-size: 1.1rem; color: var(--text-secondary); font-weight: 600;">
                                <%= job.getCompanyName() %> • 📍 <%= job.getLocation() %>
                            </p>
                        </div>
                    </div>

                    <!-- Action CTA & Bookmark -->
                    <div class="job-detail-cta-group">
                        <% if (hasApplied) { %>
                            <div class="status-applied-banner">
                                <span>Status:</span>
                                <span class="badge <%= "SELECTED".equals(appStatus) ? "badge-success" : ("REJECTED".equals(appStatus) ? "badge-danger" : "badge-primary") %>" style="font-size: 0.85rem; padding: 0.5rem 1rem;">
                                    <%= appStatus != null ? appStatus.replace("_", " ") : "APPLIED" %>
                                </span>
                            </div>
                        <% } else { %>
                            <% if (currentUser != null && currentUser.getRole() == Role.STUDENT) { %>
                                <a href="${pageContext.request.contextPath}/student/apply?jobId=<%= job.getId() %>" class="btn btn-primary btn-lg" id="btn-apply-job">
                                    🚀 Apply Now
                                </a>
                            <% } else if (currentUser == null) { %>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-lg">
                                    Log In to Apply
                                </a>
                            <% } %>
                        <% } %>

                        <% if (currentUser != null && currentUser.getRole() == Role.STUDENT) { %>
                            <form action="${pageContext.request.contextPath}/student/save-job" method="POST" style="display: inline;">
                                <input type="hidden" name="jobId" value="<%= job.getId() %>">
                                <button type="submit" class="btn btn-outline" title="<%= isSaved ? "Remove from Saved" : "Save Job" %>">
                                    <%= isSaved ? "★ Saved" : "☆ Save Job" %>
                                </button>
                            </form>
                        <% } %>
                    </div>
                </div>

                <!-- Badges Row -->
                <div class="job-badges-row" style="margin-top: 1.5rem; padding-top: 1.25rem; border-top: 1px solid var(--border-color);">
                    <span class="badge badge-success"><%= job.getJobType().getDisplayName() %></span>
                    <span class="badge badge-info">🎯 <%= job.getExperienceLevel().getDisplayName() %></span>
                    <span class="badge badge-primary">
                        <% if (job.getSalaryMin() != null && job.getSalaryMin().doubleValue() > 0) { %>
                            💰 $<%= job.getSalaryMin().intValue() %> - $<%= job.getSalaryMax().intValue() %>
                        <% } else { %>
                            💰 Competitive / Stipend
                        <% } %>
                    </span>
                    <span class="badge badge-secondary">👥 <%= job.getVacancies() %> <%= job.getVacancies() > 1 ? "Openings" : "Opening" %></span>
                    <% if (job.getDeadline() != null) { %>
                        <span class="badge badge-warning">⏳ Apply by <%= job.getDeadline() %></span>
                    <% } %>
                </div>
            </div>

            <!-- Two-Column Content Layout -->
            <div class="job-detail-grid">
                
                <!-- Main Body -->
                <div class="job-detail-body">
                    
                    <!-- Description -->
                    <div class="detail-section-card">
                        <h3>About the Role</h3>
                        <div class="rich-text-content">
                            <%= job.getDescription().replace("\n", "<br>") %>
                        </div>
                    </div>

                    <!-- Responsibilities -->
                    <% if (job.getResponsibilities() != null && !job.getResponsibilities().trim().isEmpty()) { %>
                        <div class="detail-section-card">
                            <h3>Key Responsibilities</h3>
                            <div class="rich-text-content">
                                <%= job.getResponsibilities().replace("\n", "<br>") %>
                            </div>
                        </div>
                    <% } %>

                    <!-- Requirements -->
                    <% if (job.getRequirements() != null && !job.getRequirements().trim().isEmpty()) { %>
                        <div class="detail-section-card">
                            <h3>Requirements &amp; Qualifications</h3>
                            <div class="rich-text-content">
                                <%= job.getRequirements().replace("\n", "<br>") %>
                            </div>
                        </div>
                    <% } %>

                    <!-- Required Skills -->
                    <% if (job.getRequiredSkills() != null && !job.getRequiredSkills().isEmpty()) { %>
                        <div class="detail-section-card">
                            <h3>Required Technical &amp; Core Skills</h3>
                            <div class="skills-cloud" style="background: transparent; padding: 0;">
                                <% for (Skill sk : job.getRequiredSkills()) { %>
                                    <div class="skill-tag-badge" style="background: var(--primary-light); color: var(--primary); border: none;">
                                        <span class="skill-name">⚡ <%= sk.getName() %></span>
                                    </div>
                                <% } %>
                            </div>
                        </div>
                    <% } %>

                </div>

                <!-- Sidebar Details -->
                <aside class="job-detail-sidebar">
                    
                    <!-- Company Card -->
                    <div class="detail-sidebar-card">
                        <h3>About the Employer</h3>
                        <h4 style="font-size: 1.15rem; margin-top: 0.5rem; color: var(--primary);"><%= job.getCompanyName() %></h4>
                        <% if (job.getCompanyLocation() != null) { %>
                            <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 0.75rem;">📍 <%= job.getCompanyLocation() %></p>
                        <% } %>

                        <% if (job.getCompanyDescription() != null && !job.getCompanyDescription().isEmpty()) { %>
                            <p style="font-size: 0.85rem; color: var(--text-secondary); line-height: 1.6; margin-bottom: 1rem;">
                                <%= job.getCompanyDescription() %>
                            </p>
                        <% } %>

                        <% if (job.getCompanyWebsite() != null && !job.getCompanyWebsite().isEmpty()) { %>
                            <a href="<%= job.getCompanyWebsite() %>" target="_blank" rel="noopener noreferrer" class="btn btn-sm btn-outline" style="width: 100%;">
                                Visit Website ↗
                            </a>
                        <% } %>
                    </div>

                    <!-- Role Quick Facts -->
                    <div class="detail-sidebar-card">
                        <h3>Opportunity Summary</h3>
                        <ul class="quick-facts-list">
                            <li>
                                <span>Opportunity Type:</span>
                                <strong><%= job.getJobType().getDisplayName() %></strong>
                            </li>
                            <li>
                                <span>Experience Level:</span>
                                <strong><%= job.getExperienceLevel().getDisplayName() %></strong>
                            </li>
                            <li>
                                <span>Location:</span>
                                <strong><%= job.getLocation() %></strong>
                            </li>
                            <li>
                                <span>Vacancies:</span>
                                <strong><%= job.getVacancies() %></strong>
                            </li>
                            <li>
                                <span>Posted Date:</span>
                                <strong><%= job.getCreatedAt() != null ? job.getCreatedAt().toString().substring(0, 10) : "Recent" %></strong>
                            </li>
                        </ul>
                    </div>

                </aside>

            </div>

        </main>
    <% } else { %>
        <div class="container section-padding text-center">
            <h2>Opportunity Not Found</h2>
            <p>The job posting you are looking for does not exist or has been closed.</p>
            <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary" style="margin-top: 1rem;">Browse All Jobs</a>
        </div>
    <% } %>

    <!-- Footer -->
    <footer class="footer">
        <div class="container footer-bottom">
            <p>&copy; 2026 SkillBridge. All rights reserved.</p>
            <p>Internship &amp; Job Application Management System</p>
        </div>
    </footer>

</body>
</html>
