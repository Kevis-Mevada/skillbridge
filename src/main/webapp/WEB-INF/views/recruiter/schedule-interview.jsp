<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <%@ page import="com.skillbridge.model.User, com.skillbridge.model.Application" %>

        <% User currentUser=(User) session.getAttribute("currentUser"); Application interviewApplication=(Application)
            request.getAttribute("application"); %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">

                <title>Schedule Interview Round – SkillBridge</title>

                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">

                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            </head>

            <body class="dashboard-body">

                <div class="dashboard-layout">

                    <!-- =========================================================
         SIDEBAR
         ========================================================= -->

                    <aside class="sidebar">

                        <div class="sidebar-header">

                            <a href="${pageContext.request.contextPath}/" class="brand-logo">

                                <span class="brand-icon">💼</span>
                                <span>SkillBridge</span>

                            </a>

                            <span class="badge badge-secondary" style="margin-top: 0.5rem;">

                                Employer Portal

                            </span>

                        </div>


                        <!-- USER CARD -->

                        <div class="sidebar-user-card">

                            <div class="user-avatar"
                                style="background: linear-gradient(135deg, #0ea5e9 0%, #3b82f6 100%);">

                                <%= (currentUser !=null && currentUser.getFullName() !=null &&
                                    !currentUser.getFullName().isEmpty()) ? currentUser.getFullName() .substring(0, 1)
                                    .toUpperCase() : "R" %>

                            </div>


                            <div class="user-meta">

                                <h4>
                                    <%= (currentUser !=null) ? currentUser.getFullName() : "Recruiter" %>
                                </h4>

                                <p>
                                    <%= (currentUser !=null) ? currentUser.getEmail() : "" %>
                                </p>

                            </div>

                        </div>


                        <!-- NAVIGATION -->

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


                            <a href="${pageContext.request.contextPath}/recruiter/applicants"
                                class="sidebar-link active">

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


                        <!-- FOOTER -->

                        <div class="sidebar-footer">

                            <a href="${pageContext.request.contextPath}/logout" class="sidebar-link logout-link">

                                <span class="link-icon">🚪</span>
                                <span>Log Out</span>

                            </a>

                        </div>

                    </aside>


                    <!-- =========================================================
         MAIN CONTENT
         ========================================================= -->

                    <main class="dashboard-main">


                        <!-- TOP BAR -->

                        <header class="dashboard-topbar">

                            <div>

                                <h1 style="font-size: 1.75rem;">
                                    Schedule Candidate Interview 🗓️
                                </h1>

                                <p style="color: var(--text-secondary); font-size: 0.9rem;">
                                    Set interview date, meeting link, and candidate
                                    preparation guidelines
                                </p>

                            </div>


                            <div>

                                <a href="${pageContext.request.contextPath}/recruiter/applicants"
                                    class="btn btn-outline btn-sm">

                                    ← Back to Applicants

                                </a>

                            </div>

                        </header>


                        <!-- =====================================================
             CONTENT
             ===================================================== -->

                        <div class="dashboard-content">


                            <% if (interviewApplication !=null) { %>


                                <!-- =================================================
                 CANDIDATE & JOB SUMMARY
                 ================================================= -->

                                <div class="apply-job-header-card">

                                    <div style="display: flex;
                            justify-content: space-between;
                            align-items: center;">

                                        <div>

                                            <h2 style="font-size: 1.35rem;
                                   margin-bottom: 0.2rem;">

                                                Candidate:

                                                <%= interviewApplication.getStudentName() %>

                                            </h2>


                                            <p style="color: var(--text-secondary);
                                  font-size: 0.9rem;">

                                                Applied for:

                                                <strong>

                                                    <%= interviewApplication.getJobTitle() %>

                                                </strong>

                                                • ✉️

                                                <%= interviewApplication.getStudentEmail() %>

                                            </p>

                                        </div>


                                        <div>

                                            <span class="badge badge-primary">

                                                Scheduling Interview

                                            </span>

                                        </div>

                                    </div>

                                </div>


                                <!-- =================================================
                 INTERVIEW FORM
                 ================================================= -->

                                <div class="profile-section-card">


                                    <div class="section-card-header">

                                        <h3>
                                            Interview Round Specifics
                                        </h3>

                                        <p>
                                            This information will be visible to the
                                            student in their Interview Schedule portal
                                        </p>

                                    </div>


                                    <form action="${pageContext.request.contextPath}/recruiter/schedule-interview"
                                        method="POST" class="profile-form">


                                        <!-- APPLICATION ID -->

                                        <input type="hidden" name="applicationId"
                                            value="<%= interviewApplication.getId() %>">


                                        <!-- =================================================
                         ROUND + MODE
                         ================================================= -->

                                        <div class="form-row">


                                            <div class="form-group">

                                                <label class="form-label" for="roundName">

                                                    Interview Round Name *

                                                </label>


                                                <input type="text" id="roundName" name="roundName" class="form-control"
                                                    placeholder="e.g. Technical Round 1 - Core Java &amp; DSA"
                                                    value="Technical Round 1" required>

                                            </div>


                                            <div class="form-group">

                                                <label class="form-label" for="interviewMode">

                                                    Interview Mode *

                                                </label>


                                                <select id="interviewMode" name="interviewMode" class="form-control"
                                                    required>

                                                    <option value="ONLINE" selected>
                                                        Online Meeting (Google Meet / Zoom / Teams)
                                                    </option>

                                                    <option value="IN_PERSON">
                                                        In-Person (Office Headquarters)
                                                    </option>

                                                    <option value="PHONE">
                                                        Phone Call Screening
                                                    </option>

                                                </select>

                                            </div>

                                        </div>


                                        <!-- =================================================
                         DATE + TIME
                         ================================================= -->

                                        <div class="form-row">


                                            <div class="form-group">

                                                <label class="form-label" for="interviewDate">

                                                    Interview Date *

                                                </label>


                                                <input type="date" id="interviewDate" name="interviewDate"
                                                    class="form-control" required>

                                            </div>


                                            <div class="form-group">

                                                <label class="form-label" for="interviewTime">

                                                    Interview Time (24h or HH:mm) *

                                                </label>


                                                <input type="time" id="interviewTime" name="interviewTime"
                                                    class="form-control" required>

                                            </div>

                                        </div>


                                        <!-- =================================================
                         LINK / LOCATION
                         ================================================= -->

                                        <div class="form-group">

                                            <label class="form-label" for="meetingLinkOrLocation">

                                                Meeting Link or Location Address *

                                            </label>


                                            <input type="text" id="meetingLinkOrLocation" name="meetingLinkOrLocation"
                                                class="form-control"
                                                placeholder="e.g. https://meet.google.com/abc-defg-hij or 4th Floor, Tech Hub Tower, Bangalore"
                                                required>

                                        </div>


                                        <!-- =================================================
                         INSTRUCTIONS
                         ================================================= -->

                                        <div class="form-group">

                                            <label class="form-label" for="instructions">

                                                Instructions &amp; Preparation
                                                Guidelines for Candidate

                                            </label>


                                            <textarea id="instructions" name="instructions" class="form-control"
                                                rows="4"
                                                placeholder="e.g. Please be ready with your code editor/IDE (Eclipse, IntelliJ, or VS Code) and join 5 minutes early with a stable internet connection..."></textarea>

                                        </div>


                                        <!-- =================================================
                         BUTTONS
                         ================================================= -->

                                        <div style="display: flex;
                                gap: 1rem;
                                margin-top: 1rem;">


                                            <button type="submit" class="btn btn-primary btn-lg">

                                                Confirm &amp; Schedule Interview 🗓️

                                            </button>


                                            <a href="${pageContext.request.contextPath}/recruiter/applicants"
                                                class="btn btn-outline">

                                                Cancel

                                            </a>

                                        </div>


                                    </form>

                                </div>


                                <% } else { %>


                                    <!-- =================================================
                 APPLICATION NOT FOUND
                 ================================================= -->

                                    <div class="profile-section-card">

                                        <div class="section-card-header">

                                            <h3>
                                                Application Not Found
                                            </h3>

                                            <p>
                                                The selected application could not be found.
                                                Please return to the applicants page and try again.
                                            </p>

                                        </div>


                                        <div style="margin-top: 1rem;">

                                            <a href="${pageContext.request.contextPath}/recruiter/applicants"
                                                class="btn btn-primary">

                                                ← Back to Applicants

                                            </a>

                                        </div>

                                    </div>


                                    <% } %>


                        </div>

                    </main>

                </div>

            </body>

            </html>