<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.*, java.util.List" %>
<%
    List<Job> jobList = (List<Job>) request.getAttribute("jobList");
    List<Skill> allSkills = (List<Skill>) request.getAttribute("allSkills");
    JobFilterCriteria criteria = (JobFilterCriteria) request.getAttribute("criteria");
    if (criteria == null) criteria = new JobFilterCriteria();

    Integer totalJobs = (Integer) request.getAttribute("totalJobs");
    if (totalJobs == null) totalJobs = (jobList != null ? jobList.size() : 0);

    Integer currentPage = (Integer) request.getAttribute("currentPage");
    if (currentPage == null) currentPage = 1;

    Integer totalPages = (Integer) request.getAttribute("totalPages");
    if (totalPages == null) totalPages = 1;

    User currentUser = (User) session.getAttribute("currentUser");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Explore top internship and entry-level job opportunities on SkillBridge. Filter by role, skills, location, and experience.">
    <title>Explore Jobs &amp; Internships – SkillBridge</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <!-- Main Navigation Bar -->
    <header class="navbar">
        <div class="container nav-container">
            <a href="${pageContext.request.contextPath}/" class="brand-logo">
                <span class="brand-icon">⚡</span>
                <span>SkillBridge</span>
            </a>

            <nav class="nav-links">
                <a href="${pageContext.request.contextPath}/" class="nav-link">Home</a>
                <a href="${pageContext.request.contextPath}/jobs" class="nav-link active">Explore Jobs</a>
                <% if (currentUser != null) { %>
                    <% if (currentUser.getRole() == Role.STUDENT) { %>
                        <a href="${pageContext.request.contextPath}/student/dashboard" class="nav-link">Dashboard</a>
                        <a href="${pageContext.request.contextPath}/student/applications" class="nav-link">My Applications</a>
                    <% } else if (currentUser.getRole() == Role.RECRUITER) { %>
                        <a href="${pageContext.request.contextPath}/recruiter/dashboard" class="nav-link">Dashboard</a>
                    <% } else if (currentUser.getRole() == Role.ADMIN) { %>
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">Admin</a>
                    <% } %>
                <% } %>
            </nav>

            <div class="nav-actions">
                <% if (currentUser == null) { %>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Log In</a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Sign Up</a>
                <% } else { %>
                    <span style="font-size: 0.9rem; font-weight: 600; color: var(--text-secondary);">Hi, <%= currentUser.getFullName().split(" ")[0] %></span>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline">Log Out</a>
                <% } %>
            </div>
        </div>
    </header>

    <!-- Main Search & Results Section -->
    <main class="container section-padding" style="padding-top: 2.5rem;">
        
        <!-- Search Header Bar -->
        <div class="search-header-panel">
            <h2>Find Your Next <span class="gradient-text">Career Opportunity</span></h2>
            <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">Browse verified entry-level jobs and internships tailored for students &amp; freshers</p>

            <form action="${pageContext.request.contextPath}/jobs" method="GET" class="hero-search-box" style="margin-bottom: 0;">
                <div class="search-input-group">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
                    <input type="text" name="keyword" placeholder="Job title, company, or keyword..." value="<%= criteria.getKeyword() != null ? criteria.getKeyword() : "" %>">
                </div>
                <div class="search-divider"></div>
                <div class="search-input-group">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path><circle cx="12" cy="10" r="3"></circle></svg>
                    <input type="text" name="location" placeholder="City or 'Remote'" value="<%= criteria.getLocation() != null ? criteria.getLocation() : "" %>">
                </div>
                <button type="submit" class="btn btn-primary">Search</button>
            </form>
        </div>

        <!-- Layout Grid: Filters Sidebar + Job Cards Grid -->
        <div class="jobs-layout-grid">
            
            <!-- Filters Sidebar -->
            <aside class="filters-sidebar">
                <div class="filters-card">
                    <div class="filters-header">
                        <h3>Filters</h3>
                        <a href="${pageContext.request.contextPath}/jobs" class="clear-filters-link">Clear All</a>
                    </div>

                    <form action="${pageContext.request.contextPath}/jobs" method="GET" id="filter-form">
                        <!-- Preserve keyword & location -->
                        <% if (criteria.getKeyword() != null && !criteria.getKeyword().isEmpty()) { %>
                            <input type="hidden" name="keyword" value="<%= criteria.getKeyword() %>">
                        <% } %>
                        <% if (criteria.getLocation() != null && !criteria.getLocation().isEmpty()) { %>
                            <input type="hidden" name="location" value="<%= criteria.getLocation() %>">
                        <% } %>

                        <!-- Job Type Filter -->
                        <div class="filter-group">
                            <label class="filter-group-title">Opportunity Type</label>
                            <div class="filter-options">
                                <label class="filter-checkbox-label">
                                    <input type="radio" name="type" value="" <%= criteria.getJobType() == null ? "checked" : "" %> onchange="this.form.submit()">
                                    <span>All Types</span>
                                </label>
                                <label class="filter-checkbox-label">
                                    <input type="radio" name="type" value="INTERNSHIP" <%= criteria.getJobType() == JobType.INTERNSHIP ? "checked" : "" %> onchange="this.form.submit()">
                                    <span>Internship</span>
                                </label>
                                <label class="filter-checkbox-label">
                                    <input type="radio" name="type" value="FULL_TIME" <%= criteria.getJobType() == JobType.FULL_TIME ? "checked" : "" %> onchange="this.form.submit()">
                                    <span>Full Time</span>
                                </label>
                                <label class="filter-checkbox-label">
                                    <input type="radio" name="type" value="REMOTE" <%= criteria.getJobType() == JobType.REMOTE ? "checked" : "" %> onchange="this.form.submit()">
                                    <span>Remote</span>
                                </label>
                                <label class="filter-checkbox-label">
                                    <input type="radio" name="type" value="PART_TIME" <%= criteria.getJobType() == JobType.PART_TIME ? "checked" : "" %> onchange="this.form.submit()">
                                    <span>Part Time</span>
                                </label>
                            </div>
                        </div>

                        <!-- Experience Level Filter -->
                        <div class="filter-group">
                            <label class="filter-group-title">Experience Level</label>
                            <div class="filter-options">
                                <label class="filter-checkbox-label">
                                    <input type="radio" name="experience" value="" <%= criteria.getExperienceLevel() == null ? "checked" : "" %> onchange="this.form.submit()">
                                    <span>Any Experience</span>
                                </label>
                                <label class="filter-checkbox-label">
                                    <input type="radio" name="experience" value="FRESHER" <%= criteria.getExperienceLevel() == ExperienceLevel.FRESHER ? "checked" : "" %> onchange="this.form.submit()">
                                    <span>Fresher / Student</span>
                                </label>
                                <label class="filter-checkbox-label">
                                    <input type="radio" name="experience" value="0-1_YEARS" <%= criteria.getExperienceLevel() == ExperienceLevel.EXP_0_1 ? "checked" : "" %> onchange="this.form.submit()">
                                    <span>0 - 1 Years</span>
                                </label>
                                <label class="filter-checkbox-label">
                                    <input type="radio" name="experience" value="1-3_YEARS" <%= criteria.getExperienceLevel() == ExperienceLevel.EXP_1_3 ? "checked" : "" %> onchange="this.form.submit()">
                                    <span>1 - 3 Years</span>
                                </label>
                            </div>
                        </div>

                        <!-- Required Skill Filter -->
                        <div class="filter-group">
                            <label class="filter-group-title" for="filter-skill">Filter by Skill</label>
                            <select name="skillId" id="filter-skill" class="form-control" onchange="this.form.submit()">
                                <option value="">-- All Skills --</option>
                                <% if (allSkills != null) { %>
                                    <% for (Skill sk : allSkills) { %>
                                        <option value="<%= sk.getId() %>" <%= (criteria.getSkillId() != null && criteria.getSkillId() == sk.getId()) ? "selected" : "" %>>
                                            <%= sk.getName() %>
                                        </option>
                                    <% } %>
                                <% } %>
                            </select>
                        </div>
                    </form>
                </div>
            </aside>

            <!-- Job Cards Results Grid -->
            <section class="job-results-container">
                <div class="results-meta-bar">
                    <p class="results-count">Showing <strong><%= totalJobs %></strong> open opportunities</p>
                </div>

                <% if (jobList != null && !jobList.isEmpty()) { %>
                    <div class="job-cards-grid">
                        <% for (Job j : jobList) { %>
                            <div class="job-card">
                                <div class="job-card-header">
                                    <div class="company-avatar">
                                        <%= (j.getCompanyName() != null && !j.getCompanyName().isEmpty()) ? j.getCompanyName().substring(0, 1).toUpperCase() : "C" %>
                                    </div>
                                    <div class="job-card-title-group">
                                        <h3 class="job-title">
                                            <a href="${pageContext.request.contextPath}/jobs/details?id=<%= j.getId() %>"><%= j.getTitle() %></a>
                                        </h3>
                                        <p class="company-name"><%= j.getCompanyName() %></p>
                                    </div>
                                </div>

                                <div class="job-badges-row">
                                    <span class="badge <%= j.getJobType() == JobType.INTERNSHIP ? "badge-success" : "badge-primary" %>">
                                        <%= j.getJobType().getDisplayName() %>
                                    </span>
                                    <span class="badge badge-info">
                                        📍 <%= j.getLocation() %>
                                    </span>
                                    <span class="badge badge-secondary">
                                        🎯 <%= j.getExperienceLevel().getDisplayName() %>
                                    </span>
                                </div>

                                <p class="job-card-desc">
                                    <%= j.getDescription().length() > 140 ? j.getDescription().substring(0, 140) + "..." : j.getDescription() %>
                                </p>

                                <!-- Skills Tags -->
                                <% if (j.getRequiredSkills() != null && !j.getRequiredSkills().isEmpty()) { %>
                                    <div class="job-skills-row">
                                        <% for (Skill sk : j.getRequiredSkills()) { %>
                                            <span class="job-skill-pill"><%= sk.getName() %></span>
                                        <% } %>
                                    </div>
                                <% } %>

                                <div class="job-card-footer">
                                    <div class="salary-tag">
                                        <% if (j.getSalaryMin() != null && j.getSalaryMin().doubleValue() > 0) { %>
                                            <span>💰 $<%= j.getSalaryMin().intValue() %> - $<%= j.getSalaryMax().intValue() %></span>
                                        <% } else { %>
                                            <span>💰 Competitive / Stipend</span>
                                        <% } %>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/jobs/details?id=<%= j.getId() %>" class="btn btn-sm btn-primary">
                                        View Details →
                                    </a>
                                </div>
                            </div>
                        <% } %>
                    </div>

                    <!-- Pagination -->
                    <% if (totalPages > 1) { %>
                        <div class="pagination-bar">
                            <% if (currentPage > 1) { %>
                                <a href="${pageContext.request.contextPath}/jobs?page=<%= currentPage - 1 %><%= criteria.getKeyword() != null ? "&keyword=" + criteria.getKeyword() : "" %><%= criteria.getJobType() != null ? "&type=" + criteria.getJobType().name() : "" %>" class="page-btn">← Prev</a>
                            <% } %>

                            <% for (int p = 1; p <= totalPages; p++) { %>
                                <a href="${pageContext.request.contextPath}/jobs?page=<%= p %><%= criteria.getKeyword() != null ? "&keyword=" + criteria.getKeyword() : "" %><%= criteria.getJobType() != null ? "&type=" + criteria.getJobType().name() : "" %>" class="page-btn <%= p == currentPage ? "active" : "" %>">
                                    <%= p %>
                                </a>
                            <% } %>

                            <% if (currentPage < totalPages) { %>
                                <a href="${pageContext.request.contextPath}/jobs?page=<%= currentPage + 1 %><%= criteria.getKeyword() != null ? "&keyword=" + criteria.getKeyword() : "" %><%= criteria.getJobType() != null ? "&type=" + criteria.getJobType().name() : "" %>" class="page-btn">Next →</a>
                            <% } %>
                        </div>
                    <% } %>

                <% } else { %>
                    <!-- Empty State -->
                    <div class="empty-state-card">
                        <div class="empty-icon">🔍</div>
                        <h3>No matching opportunities found</h3>
                        <p>Try adjusting your search keyword, removing active filters, or exploring all job types.</p>
                        <a href="${pageContext.request.contextPath}/jobs" class="btn btn-outline" style="margin-top: 1rem;">Reset All Filters</a>
                    </div>
                <% } %>

            </section>
        </div>
    </main>

    <!-- Footer -->
    <footer class="footer">
        <div class="container footer-bottom">
            <p>&copy; 2026 SkillBridge. All rights reserved.</p>
            <p>Empowering Students &amp; Connecting Recruiters</p>
        </div>
    </footer>

</body>
</html>
