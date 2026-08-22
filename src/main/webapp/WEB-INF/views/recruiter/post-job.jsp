<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.Skill, com.skillbridge.model.JobType, com.skillbridge.model.ExperienceLevel, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    List<Skill> masterSkills = (List<Skill>) request.getAttribute("masterSkills");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Post New Opportunity – SkillBridge</title>
    
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
                <a href="${pageContext.request.contextPath}/recruiter/post-job" class="sidebar-link active">
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

        <!-- Main Content Form Area -->
        <main class="dashboard-main">
            <!-- Topbar -->
            <header class="dashboard-topbar">
                <div>
                    <h1 style="font-size: 1.75rem;">Create New Job or Internship ➕</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Fill in the role requirements, experience criteria, and required skills</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/recruiter/manage-jobs" class="btn btn-outline btn-sm">← View All Postings</a>
                </div>
            </header>

            <div class="dashboard-content">
                
                <% if (request.getAttribute("errorMessage") != null) { %>
                    <div class="alert alert-danger">
                        <span class="alert-icon">⚠️</span>
                        <span><%= request.getAttribute("errorMessage") %></span>
                    </div>
                <% } %>

                <div class="profile-section-card">
                    <form action="${pageContext.request.contextPath}/recruiter/post-job" method="POST" class="profile-form">
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="title">Job / Internship Title *</label>
                                <input type="text" id="title" name="title" class="form-control" placeholder="e.g. Backend Java Developer Intern" required>
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="location">Location / Work Mode *</label>
                                <input type="text" id="location" name="location" class="form-control" placeholder="e.g. Bangalore, India or Remote" required>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="jobType">Opportunity Type *</label>
                                <select id="jobType" name="jobType" class="form-control" required>
                                    <option value="INTERNSHIP">Internship</option>
                                    <option value="FULL_TIME" selected>Full Time</option>
                                    <option value="REMOTE">Remote</option>
                                    <option value="PART_TIME">Part Time</option>
                                    <option value="CONTRACT">Contract</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="experienceLevel">Experience Level *</label>
                                <select id="experienceLevel" name="experienceLevel" class="form-control" required>
                                    <option value="FRESHER" selected>Fresher / Student (0 Experience)</option>
                                    <option value="0-1_YEARS">0 - 1 Years</option>
                                    <option value="1-3_YEARS">1 - 3 Years</option>
                                    <option value="3+_YEARS">3+ Years</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="description">Job Description / Overview *</label>
                            <textarea id="description" name="description" class="form-control" rows="4" placeholder="Provide a high-level summary of the opportunity, project context, and team background..." required></textarea>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="responsibilities">Key Responsibilities</label>
                            <textarea id="responsibilities" name="responsibilities" class="form-control" rows="4" placeholder="List the primary day-to-day duties and deliverables expected from the candidate..."></textarea>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="requirements">Requirements &amp; Qualifications</label>
                            <textarea id="requirements" name="requirements" class="form-control" rows="4" placeholder="List degree requirements, prerequisite concepts, or problem-solving expectations..."></textarea>
                        </div>

                        <!-- Required Skills Checklist -->
                        <div class="form-group">
                            <label class="form-label">Required Industry Skills (Select all relevant)</label>
                            <div class="skills-checkbox-grid">
                                <% if (masterSkills != null) { %>
                                    <% for (Skill sk : masterSkills) { %>
                                        <label class="skill-checkbox-item">
                                            <input type="checkbox" name="skillIds" value="<%= sk.getId() %>">
                                            <span><%= sk.getName() %></span>
                                        </label>
                                    <% } %>
                                <% } %>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="salaryMin">Minimum Salary / Stipend ($ or ₹)</label>
                                <input type="number" id="salaryMin" name="salaryMin" class="form-control" step="100" placeholder="e.g. 25000">
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="salaryMax">Maximum Salary / Stipend ($ or ₹)</label>
                                <input type="number" id="salaryMax" name="salaryMax" class="form-control" step="100" placeholder="e.g. 40000">
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="vacancies">Number of Vacancies *</label>
                                <input type="number" id="vacancies" name="vacancies" class="form-control" min="1" value="1" required>
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="deadline">Application Deadline</label>
                                <input type="date" id="deadline" name="deadline" class="form-control">
                            </div>
                        </div>

                        <div style="display: flex; gap: 1rem; margin-top: 1rem;">
                            <button type="submit" class="btn btn-primary btn-lg">Publish Opportunity 🚀</button>
                            <a href="${pageContext.request.contextPath}/recruiter/manage-jobs" class="btn btn-outline">Cancel</a>
                        </div>
                    </form>
                </div>

            </div>
        </main>
    </div>

</body>
</html>
