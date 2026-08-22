<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.RecruiterProfile" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    RecruiterProfile profile = (RecruiterProfile) request.getAttribute("profile");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Company Profile &amp; Settings – SkillBridge</title>
    
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
                    <h4><%= (profile != null && profile.getCompanyName() != null) ? profile.getCompanyName() : "Company" %></h4>
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
                <a href="${pageContext.request.contextPath}/recruiter/interviews" class="sidebar-link">
                    <span class="link-icon">🗓️</span>
                    <span>Interviews</span>
                </a>
                <a href="${pageContext.request.contextPath}/recruiter/profile" class="sidebar-link active">
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
                    <h1 style="font-size: 1.75rem;">Company Profile &amp; Settings 🏢</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Information shown to candidate applicants across all your postings</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/recruiter/dashboard" class="btn btn-outline btn-sm">← Back to Dashboard</a>
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

                <div class="profile-section-card">
                    <div class="section-card-header">
                        <h3>Organization &amp; Recruiter Information</h3>
                        <p>Keep your employer branding accurate to attract top fresher talent</p>
                    </div>

                    <form action="${pageContext.request.contextPath}/recruiter/profile" method="POST" class="profile-form">
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="companyName">Company / Organization Name *</label>
                                <input type="text" id="companyName" name="companyName" class="form-control" value="<%= (profile != null && profile.getCompanyName() != null) ? profile.getCompanyName() : "" %>" required>
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="companyWebsite">Company Website URL</label>
                                <input type="url" id="companyWebsite" name="companyWebsite" class="form-control" placeholder="https://example.com" value="<%= (profile != null && profile.getCompanyWebsite() != null) ? profile.getCompanyWebsite() : "" %>">
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="industry">Industry / Sector</label>
                                <input type="text" id="industry" name="industry" class="form-control" placeholder="e.g. Information Technology, FinTech, E-Commerce" value="<%= (profile != null && profile.getIndustry() != null) ? profile.getIndustry() : "" %>">
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="companySize">Company Size</label>
                                <select id="companySize" name="companySize" class="form-control">
                                    <option value="1-10 Employees" <%= (profile != null && "1-10 Employees".equals(profile.getCompanySize())) ? "selected" : "" %>>1-10 Employees (Startup)</option>
                                    <option value="11-50 Employees" <%= (profile != null && "11-50 Employees".equals(profile.getCompanySize())) ? "selected" : "" %>>11-50 Employees</option>
                                    <option value="50-200 Employees" <%= (profile != null && "50-200 Employees".equals(profile.getCompanySize())) ? "selected" : "" %>>50-200 Employees (Growth)</option>
                                    <option value="200-500 Employees" <%= (profile != null && "200-500 Employees".equals(profile.getCompanySize())) ? "selected" : "" %>>200-500 Employees</option>
                                    <option value="500+ Employees" <%= (profile != null && "500+ Employees".equals(profile.getCompanySize())) ? "selected" : "" %>>500+ Employees (Enterprise)</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="location">Headquarters / Office Location</label>
                            <input type="text" id="location" name="location" class="form-control" placeholder="e.g. Bangalore, India" value="<%= (profile != null && profile.getLocation() != null) ? profile.getLocation() : "" %>">
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="companyDescription">About Company / Culture Summary</label>
                            <textarea id="companyDescription" name="companyDescription" class="form-control" rows="4" placeholder="Describe your company's mission, values, work culture, and what makes it exciting for students..."><%= (profile != null && profile.getCompanyDescription() != null) ? profile.getCompanyDescription() : "" %></textarea>
                        </div>

                        <div class="section-card-header" style="margin-top: 1.5rem;">
                            <h3>Recruiter Representative Contact</h3>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="fullName">Recruiter Contact Name *</label>
                                <input type="text" id="fullName" name="fullName" class="form-control" value="<%= (currentUser != null) ? currentUser.getFullName() : "" %>" required>
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="phone">Contact Phone Number</label>
                                <input type="tel" id="phone" name="phone" class="form-control" placeholder="+1 (555) 019-2834" value="<%= (currentUser != null && currentUser.getPhone() != null) ? currentUser.getPhone() : "" %>">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Recruiter Email Address (Login)</label>
                            <input type="email" class="form-control" value="<%= (currentUser != null) ? currentUser.getEmail() : "" %>" disabled>
                        </div>

                        <button type="submit" class="btn btn-primary btn-lg" style="margin-top: 1rem;">Save Company Profile</button>
                    </form>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
