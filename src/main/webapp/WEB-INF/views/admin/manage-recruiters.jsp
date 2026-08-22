<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skillbridge.model.User, com.skillbridge.model.RecruiterProfile, java.util.List" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    List<RecruiterProfile> recruiters = (List<RecruiterProfile>) request.getAttribute("recruiters");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Employers &amp; Recruiters – SkillBridge Admin</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="dashboard-body">

    <div class="dashboard-layout">
        <!-- Sidebar Navigation -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <a href="${pageContext.request.contextPath}/" class="brand-logo">
                    <span class="brand-icon" style="background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);">🛡️</span>
                    <span>SkillBridge</span>
                </a>
                <span class="badge badge-secondary" style="margin-top: 0.5rem; background: var(--accent-light); color: var(--accent);">Admin Governance</span>
            </div>

            <div class="sidebar-user-card">
                <div class="user-avatar" style="background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);">
                    A
                </div>
                <div class="user-meta">
                    <h4><%= (currentUser != null) ? currentUser.getFullName() : "Administrator" %></h4>
                    <p>System Superuser</p>
                </div>
            </div>

            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link">
                    <span class="link-icon">📊</span>
                    <span>Admin Overview</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/students" class="sidebar-link">
                    <span class="link-icon">🎓</span>
                    <span>Manage Students</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/recruiters" class="sidebar-link active">
                    <span class="link-icon">🏢</span>
                    <span>Manage Employers</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/jobs" class="sidebar-link">
                    <span class="link-icon">📋</span>
                    <span>Moderate Jobs</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/applications" class="sidebar-link">
                    <span class="link-icon">📑</span>
                    <span>All Applications</span>
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
                    <h1 style="font-size: 1.75rem;">Manage Hiring Organizations 🏢</h1>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Review registered employer companies, recruiter verification, and account access</p>
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

                <div class="panel-card" style="padding: 2rem;">
                    <% if (recruiters != null && !recruiters.isEmpty()) { %>
                        <div style="overflow-x: auto;">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Company Name</th>
                                        <th>Recruiter Representative</th>
                                        <th>Industry &amp; Size</th>
                                        <th>Location / Website</th>
                                        <th>Account Status</th>
                                        <th style="text-align: right;">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (RecruiterProfile rp : recruiters) { %>
                                        <tr>
                                            <td>
                                                <strong><%= rp.getCompanyName() %></strong>
                                                <% if (rp.isVerified()) { %>
                                                    <span class="badge badge-success" style="font-size: 0.7rem; padding: 0.15rem 0.45rem;">Verified</span>
                                                <% } %>
                                            </td>
                                            <td>
                                                <strong><%= rp.getUserFullName() %></strong>
                                                <p style="font-size: 0.75rem; color: var(--text-muted);">✉️ <%= rp.getUserEmail() %></p>
                                                <% if (rp.getUserPhone() != null && !rp.getUserPhone().isEmpty()) { %>
                                                    <p style="font-size: 0.75rem; color: var(--text-muted);">📞 <%= rp.getUserPhone() %></p>
                                                <% } %>
                                            </td>
                                            <td>
                                                <span><%= rp.getIndustry() != null ? rp.getIndustry() : "IT / Software" %></span>
                                                <p style="font-size: 0.75rem; color: var(--text-secondary);"><%= rp.getCompanySize() != null ? rp.getCompanySize() : "" %></p>
                                            </td>
                                            <td>
                                                <span>📍 <%= rp.getLocation() != null ? rp.getLocation() : "Remote" %></span>
                                                <% if (rp.getCompanyWebsite() != null && !rp.getCompanyWebsite().isEmpty()) { %>
                                                    <p style="font-size: 0.75rem;">
                                                        <a href="<%= rp.getCompanyWebsite() %>" target="_blank" rel="noopener noreferrer" style="color: var(--primary);">Visit Website ↗</a>
                                                    </p>
                                                <% } %>
                                            </td>
                                            <td>
                                                <span class="badge <%= rp.isUserActive() ? "badge-success" : "badge-danger" %>">
                                                    <%= rp.isUserActive() ? "Active" : "Deactivated" %>
                                                </span>
                                            </td>
                                            <td style="text-align: right;">
                                                <div style="display: flex; gap: 0.5rem; justify-content: flex-end;">
                                                    <form action="${pageContext.request.contextPath}/admin/recruiters" method="POST" style="display: inline;">
                                                        <input type="hidden" name="action" value="toggle">
                                                        <input type="hidden" name="userId" value="<%= rp.getUserId() %>">
                                                        <input type="hidden" name="currentStatus" value="<%= rp.isUserActive() %>">
                                                        <button type="submit" class="btn btn-sm btn-outline">
                                                            <%= rp.isUserActive() ? "Deactivate" : "Activate" %>
                                                        </button>
                                                    </form>
                                                    <form action="${pageContext.request.contextPath}/admin/recruiters" method="POST" onsubmit="return confirm('Are you sure you want to permanently delete this recruiter and company profile?');" style="display: inline;">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="userId" value="<%= rp.getUserId() %>">
                                                        <button type="submit" class="btn btn-sm btn-outline" style="color: var(--danger); border-color: var(--border-color);" title="Delete Recruiter">
                                                            🗑️
                                                        </button>
                                                    </form>
                                                </div>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    <% } else { %>
                        <div class="empty-state-card">
                            <div class="empty-icon">🏢</div>
                            <h3>No employer companies registered yet</h3>
                        </div>
                    <% } %>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
