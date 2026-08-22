<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.StudentProfile, com.skillbridge.model.Education, com.skillbridge.model.StudentSkill, com.skillbridge.model.Skill, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    StudentProfile profile = (StudentProfile) request.getAttribute("profile");
    List<Education> educationList = (List<Education>) request.getAttribute("educationList");
    List<StudentSkill> studentSkills = (List<StudentSkill>) request.getAttribute("studentSkills");
    List<Skill> masterSkills = (List<Skill>) request.getAttribute("masterSkills");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile &amp; Skills – SkillBridge</title>
    
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
                <a href="${pageContext.request.contextPath}/student/profile" class="sidebar-link active">
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

        <!-- Main Content -->
        <main class="dashboard-main">
            <!-- Topbar -->
            <header class="dashboard-topbar">
                <div>
                    <h1 style="font-size: 1.75rem;">Manage Profile &amp; Credentials 🎓</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Keep your education, technical skills, and resume updated for recruiters</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/student/dashboard" class="btn btn-outline btn-sm">← Back to Dashboard</a>
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

                <!-- 1. Basic Information Section -->
                <div class="profile-section-card">
                    <div class="section-card-header">
                        <div>
                            <h3>Personal &amp; Contact Details</h3>
                            <p>Basic information displayed on your job applications</p>
                        </div>
                    </div>

                    <form action="${pageContext.request.contextPath}/student/profile" method="POST" class="profile-form">
                        <input type="hidden" name="action" value="updateBasic">

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="fullName">Full Name *</label>
                                <input type="text" id="fullName" name="fullName" class="form-control" value="<%= (currentUser != null) ? currentUser.getFullName() : "" %>" required>
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="email">Email Address (Read-Only)</label>
                                <input type="email" id="email" class="form-control" value="<%= (currentUser != null) ? currentUser.getEmail() : "" %>" disabled>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="phone">Phone Number</label>
                                <input type="tel" id="phone" name="phone" class="form-control" placeholder="+1 (555) 000-0000" value="<%= (currentUser != null && currentUser.getPhone() != null) ? currentUser.getPhone() : "" %>">
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="currentLocation">Current City / Location</label>
                                <input type="text" id="currentLocation" name="currentLocation" class="form-control" placeholder="e.g. Bangalore, India" value="<%= (profile != null && profile.getCurrentLocation() != null) ? profile.getCurrentLocation() : "" %>">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="headline">Professional Headline</label>
                            <input type="text" id="headline" name="headline" class="form-control" placeholder="e.g. Computer Science Student | Aspiring Full Stack Java Developer" value="<%= (profile != null && profile.getHeadline() != null) ? profile.getHeadline() : "" %>">
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="bio">About Me / Summary</label>
                            <textarea id="bio" name="bio" class="form-control" rows="3" placeholder="Briefly describe your passion, career goals, and key competencies..."><%= (profile != null && profile.getBio() != null) ? profile.getBio() : "" %></textarea>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="graduationYear">Expected / Graduation Year</label>
                                <input type="number" id="graduationYear" name="graduationYear" class="form-control" min="1990" max="2040" placeholder="2026" value="<%= (profile != null && profile.getGraduationYear() != null) ? profile.getGraduationYear() : "" %>">
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="cgpa">Overall CGPA (out of 10.0)</label>
                                <input type="number" id="cgpa" name="cgpa" class="form-control" step="0.01" min="0" max="10" placeholder="8.50" value="<%= (profile != null && profile.getCgpa() != null) ? profile.getCgpa() : "" %>">
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="resumeUrl">Resume Link / URL (Google Drive / Cloud)</label>
                                <input type="url" id="resumeUrl" name="resumeUrl" class="form-control" placeholder="https://drive.google.com/your-resume-link" value="<%= (profile != null && profile.getResumeUrl() != null) ? profile.getResumeUrl() : "" %>">
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="linkedinUrl">LinkedIn Profile URL</label>
                                <input type="url" id="linkedinUrl" name="linkedinUrl" class="form-control" placeholder="https://linkedin.com/in/username" value="<%= (profile != null && profile.getLinkedinUrl() != null) ? profile.getLinkedinUrl() : "" %>">
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label" for="githubUrl">GitHub Profile URL</label>
                                <input type="url" id="githubUrl" name="githubUrl" class="form-control" placeholder="https://github.com/username" value="<%= (profile != null && profile.getGithubUrl() != null) ? profile.getGithubUrl() : "" %>">
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="portfolioUrl">Portfolio Website URL</label>
                                <input type="url" id="portfolioUrl" name="portfolioUrl" class="form-control" placeholder="https://myportfolio.dev" value="<%= (profile != null && profile.getPortfolioUrl() != null) ? profile.getPortfolioUrl() : "" %>">
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary">Save Basic Information</button>
                    </form>
                </div>

                <!-- 2. Skills Management Section -->
                <div class="profile-section-card">
                    <div class="section-card-header">
                        <div>
                            <h3>Technical &amp; Core Skills</h3>
                            <p>Add the skills you possess to match with relevant internship/job requirements</p>
                        </div>
                    </div>

                    <!-- Current Skills Tag Cloud -->
                    <div class="skills-cloud">
                        <% if (studentSkills != null && !studentSkills.isEmpty()) { %>
                            <% for (StudentSkill sk : studentSkills) { %>
                                <div class="skill-tag-badge">
                                    <span class="skill-name"><%= sk.getSkillName() %></span>
                                    <span class="skill-level"><%= sk.getProficiencyLevel() %></span>
                                    <form action="${pageContext.request.contextPath}/student/profile" method="POST" style="display: inline;">
                                        <input type="hidden" name="action" value="removeSkill">
                                        <input type="hidden" name="skillId" value="<%= sk.getSkillId() %>">
                                        <button type="submit" class="btn-remove-skill" title="Remove skill" aria-label="Remove skill">×</button>
                                    </form>
                                </div>
                            <% } %>
                        <% } else { %>
                            <p style="color: var(--text-muted); font-size: 0.9rem;">No skills added yet. Select or enter skills below.</p>
                        <% } %>
                    </div>

                    <!-- Add Skill Form -->
                    <form action="${pageContext.request.contextPath}/student/profile" method="POST" class="add-skill-form">
                        <input type="hidden" name="action" value="addSkill">
                        
                        <div class="add-skill-grid">
                            <div class="form-group">
                                <label class="form-label" for="skillId">Select Popular Skill</label>
                                <select name="skillId" id="skillId" class="form-control">
                                    <option value="">-- Choose from Catalogue --</option>
                                    <% if (masterSkills != null) { %>
                                        <% for (Skill s : masterSkills) { %>
                                            <option value="<%= s.getId() %>"><%= s.getName() %> (<%= s.getCategory() %>)</option>
                                        <% } %>
                                    <% } %>
                                </select>
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="customSkillName">Or Enter Custom Skill</label>
                                <input type="text" name="customSkillName" id="customSkillName" class="form-control" placeholder="e.g. Kotlin, Angular">
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="proficiencyLevel">Proficiency</label>
                                <select name="proficiencyLevel" id="proficiencyLevel" class="form-control">
                                    <option value="Beginner">Beginner</option>
                                    <option value="Intermediate" selected>Intermediate</option>
                                    <option value="Advanced">Advanced</option>
                                    <option value="Expert">Expert</option>
                                </select>
                            </div>

                            <div class="form-group" style="justify-content: flex-end;">
                                <button type="submit" class="btn btn-primary" style="height: 42px;">+ Add Skill</button>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- 3. Education Qualifications Section -->
                <div class="profile-section-card">
                    <div class="section-card-header">
                        <div>
                            <h3>Education &amp; Qualifications</h3>
                            <p>Your academic background (degrees, university, grades)</p>
                        </div>
                    </div>

                    <!-- Existing Education List -->
                    <% if (educationList != null && !educationList.isEmpty()) { %>
                        <div class="education-timeline">
                            <% for (Education edu : educationList) { %>
                                <div class="education-item-card">
                                    <div class="education-details">
                                        <h4><%= edu.getDegree() %> in <%= edu.getFieldOfStudy() %></h4>
                                        <p class="edu-inst"><%= edu.getInstitution() %></p>
                                        <p class="edu-meta">
                                            <span>📅 <%= edu.getStartDate() != null ? edu.getStartDate() : "Start" %> – <%= edu.getEndDate() != null ? edu.getEndDate() : "Present" %></span>
                                            <% if (edu.getGradePercentage() != null) { %>
                                                • <span>Score / Grade: <strong><%= edu.getGradePercentage() %>%</strong></span>
                                            <% } %>
                                        </p>
                                    </div>
                                    <form action="${pageContext.request.contextPath}/student/profile" method="POST" onsubmit="return confirm('Are you sure you want to remove this education record?');">
                                        <input type="hidden" name="action" value="deleteEducation">
                                        <input type="hidden" name="educationId" value="<%= edu.getId() %>">
                                        <button type="submit" class="btn btn-sm btn-outline" style="color: var(--danger); border-color: var(--danger);">Delete</button>
                                    </form>
                                </div>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.5rem;">No education records added yet. Add your college/university details below.</p>
                    <% } %>

                    <!-- Add Education Form -->
                    <div class="add-edu-box">
                        <h4 style="margin-bottom: 1rem; font-size: 1.05rem;">+ Add New Qualification</h4>
                        <form action="${pageContext.request.contextPath}/student/profile" method="POST" class="profile-form">
                            <input type="hidden" name="action" value="addEducation">

                            <div class="form-row">
                                <div class="form-group">
                                    <label class="form-label" for="institution">College / University *</label>
                                    <input type="text" id="institution" name="institution" class="form-control" placeholder="e.g. Stanford University / IIT Delhi" required>
                                </div>
                                <div class="form-group">
                                    <label class="form-label" for="degree">Degree / Diploma *</label>
                                    <input type="text" id="degree" name="degree" class="form-control" placeholder="e.g. Bachelor of Technology (B.Tech)" required>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label class="form-label" for="fieldOfStudy">Field of Study / Major *</label>
                                    <input type="text" id="fieldOfStudy" name="fieldOfStudy" class="form-control" placeholder="e.g. Computer Science &amp; Engineering" required>
                                </div>
                                <div class="form-group">
                                    <label class="form-label" for="gradePercentage">Grade Percentage / Marks (%)</label>
                                    <input type="number" id="gradePercentage" name="gradePercentage" class="form-control" step="0.01" min="0" max="100" placeholder="e.g. 88.5">
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label class="form-label" for="startDate">Start Date</label>
                                    <input type="date" id="startDate" name="startDate" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label class="form-label" for="endDate">End Date / Expected</label>
                                    <input type="date" id="endDate" name="endDate" class="form-control">
                                </div>
                            </div>

                            <button type="submit" class="btn btn-outline" style="border-color: var(--primary); color: var(--primary);">+ Save Qualification</button>
                        </form>
                    </div>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
