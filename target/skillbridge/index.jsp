<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="SkillBridge - The premier platform connecting ambitious students and freshers with top-tier internships and full-time tech roles.">
    <title>SkillBridge – Internship &amp; Job Application Management System</title>
    
    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <!-- Header & Navigation -->
    <header class="navbar">
        <div class="container nav-container">
            <a href="${pageContext.request.contextPath}/" class="brand-logo" id="nav-brand-logo">
                <span class="brand-icon">⚡</span>
                <span>SkillBridge</span>
            </a>

            <nav class="nav-links">
                <a href="${pageContext.request.contextPath}/" class="nav-link active">Home</a>
                <a href="${pageContext.request.contextPath}/jobs" class="nav-link">Explore Jobs</a>
                <a href="#roles" class="nav-link">How It Works</a>
                <a href="#pipeline" class="nav-link">Application Pipeline</a>
            </nav>

            <div class="nav-actions">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline" id="nav-login-btn">Log In</a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary" id="nav-register-btn">Get Started</a>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <main>
        <!-- Hero Section -->
        <section class="hero">
            <div class="container hero-grid">
                <div class="hero-content">
                    <div class="hero-badge">
                        <span>✨</span> Next-Gen Career Launchpad for Freshers
                    </div>
                    <h1 class="hero-title">
                        Bridge the gap between <span class="gradient-text">skills</span> and your dream career.
                    </h1>
                    <p class="hero-subtitle">
                        SkillBridge empowers students to land verified internships and entry-level positions, while providing recruiters with powerful applicant tracking and interview scheduling.
                    </p>

                    <!-- Search Bar Mockup / Quick Search Action -->
                    <form action="${pageContext.request.contextPath}/jobs" method="GET" class="hero-search-box" id="hero-search-form">
                        <div class="search-input-group">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
                            <input type="text" name="keyword" id="search-keyword" placeholder="Job title, skill (e.g. Java, React), or role..." required>
                        </div>
                        <div class="search-divider"></div>
                        <div class="search-input-group">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path><circle cx="12" cy="10" r="3"></circle></svg>
                            <input type="text" name="location" id="search-location" placeholder="City or Remote">
                        </div>
                        <button type="submit" class="btn btn-primary" id="hero-search-submit">Search Opportunities</button>
                    </form>

                    <!-- Popular Tags -->
                    <div class="popular-tags">
                        <span>Trending:</span>
                        <a href="#" class="tag-pill">Java</a>
                        <a href="#" class="tag-pill">Frontend</a>
                        <a href="#" class="tag-pill">Full-Stack</a>
                        <a href="#" class="tag-pill">Internship</a>
                        <a href="#" class="tag-pill">PostgreSQL</a>
                    </div>
                </div>

                <!-- Hero Interactive Visual Card -->
                <div class="hero-visual">
                    <div class="hero-card-preview">
                        <!-- Application Tracker Mini-Mockup -->
                        <div class="preview-status-step">
                            <div class="step-bullet completed">
                                <div class="bullet-icon">✓</div>
                                <span>Applied</span>
                            </div>
                            <div class="step-bullet completed">
                                <div class="bullet-icon">✓</div>
                                <span>Reviewed</span>
                            </div>
                            <div class="step-bullet active">
                                <div class="bullet-icon">3</div>
                                <span>Interview</span>
                            </div>
                            <div class="step-bullet">
                                <div class="bullet-icon">4</div>
                                <span>Offer</span>
                            </div>
                        </div>

                        <!-- Live Opportunity Card Preview -->
                        <div class="live-job-preview-card">
                            <div class="job-preview-header">
                                <div>
                                    <h4 style="font-size: 1.1rem; margin-bottom: 0.2rem;">Full Stack Java Developer Intern</h4>
                                    <p style="font-size: 0.85rem; color: var(--text-secondary);">Nexus Cloud Technologies • Bangalore / Remote</p>
                                </div>
                                <span class="badge badge-success">Internship</span>
                            </div>
                            <div style="display: flex; gap: 0.5rem; margin-top: 0.75rem; flex-wrap: wrap;">
                                <span class="badge badge-primary">Java 17</span>
                                <span class="badge badge-info">PostgreSQL</span>
                                <span class="badge badge-secondary">Servlets/JSP</span>
                            </div>
                        </div>

                        <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 1.25rem;">
                            <span style="font-size: 0.85rem; color: var(--text-muted);">Status: <strong style="color: var(--primary);">Interview Scheduled</strong></span>
                            <a href="${pageContext.request.contextPath}/register" class="btn btn-sm btn-primary">Apply Now</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Stats Bar -->
        <section class="stats-section">
            <div class="container stats-grid">
                <div class="stat-item">
                    <h3 class="stat-number" data-target="2500" data-suffix="+">0+</h3>
                    <p>Verified Opportunities</p>
                </div>
                <div class="stat-item">
                    <h3 class="stat-number" data-target="150" data-suffix="+">0+</h3>
                    <p>Top Hiring Companies</p>
                </div>
                <div class="stat-item">
                    <h3 class="stat-number" data-target="12000" data-suffix="+">0+</h3>
                    <p>Student Applications Tracked</p>
                </div>
                <div class="stat-item">
                    <h3 class="stat-number" data-target="96" data-suffix="%">0%</h3>
                    <p>Placement Satisfaction</p>
                </div>
            </div>
        </section>

        <!-- Roles Section (3 Core Pillars) -->
        <section class="section-padding" id="roles">
            <div class="container">
                <div class="section-header">
                    <span class="subheading">Tailored For Everyone</span>
                    <h2>A unified ecosystem for your hiring & career journey</h2>
                    <p>Whether you're taking your first career step, hiring fresh talent, or overseeing platform operations.</p>
                </div>

                <div class="roles-grid">
                    <!-- Student Role Card -->
                    <div class="role-card student">
                        <div>
                            <div class="role-icon">🎓</div>
                            <h3>For Students &amp; Freshers</h3>
                            <p>Build your professional portfolio, explore verified internships, apply with one click, and track your interview rounds in real-time.</p>
                            <ul class="role-feature-list">
                                <li class="role-feature-item">
                                    <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><polyline points="20 6 9 17 4 12"></polyline></svg>
                                    Interactive profile, skills & education builder
                                </li>
                                <li class="role-feature-item">
                                    <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><polyline points="20 6 9 17 4 12"></polyline></svg>
                                    Instant status tracking & interview notifications
                                </li>
                                <li class="role-feature-item">
                                    <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><polyline points="20 6 9 17 4 12"></polyline></svg>
                                    Bookmark favourite roles & tailored filters
                                </li>
                            </ul>
                        </div>
                        <a href="${pageContext.request.contextPath}/register?role=STUDENT" class="btn btn-primary" style="width: 100%;">Join as Student</a>
                    </div>

                    <!-- Recruiter Role Card -->
                    <div class="role-card recruiter">
                        <div>
                            <div class="role-icon">💼</div>
                            <h3>For Employers &amp; Recruiters</h3>
                            <p>Post internships and entry-level jobs, review verified student profiles, shortlist candidates, and schedule interview rounds seamlessly.</p>
                            <ul class="role-feature-list">
                                <li class="role-feature-item">
                                    <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><polyline points="20 6 9 17 4 12"></polyline></svg>
                                    Job & Internship post management
                                </li>
                                <li class="role-feature-item">
                                    <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><polyline points="20 6 9 17 4 12"></polyline></svg>
                                    Applicant filtering & candidate pipeline
                                </li>
                                <li class="role-feature-item">
                                    <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><polyline points="20 6 9 17 4 12"></polyline></svg>
                                    Built-in interview scheduling system
                                </li>
                            </ul>
                        </div>
                        <a href="${pageContext.request.contextPath}/register?role=RECRUITER" class="btn btn-outline" style="width: 100%; border-color: var(--secondary); color: var(--secondary);">Post a Job / Intern</a>
                    </div>

                    <!-- Admin Role Card -->
                    <div class="role-card admin">
                        <div>
                            <div class="role-icon">🛡️</div>
                            <h3>For System Administrators</h3>
                            <p>Maintain quality and security across the platform with full administrative control over user accounts, job postings, and system metrics.</p>
                            <ul class="role-feature-list">
                                <li class="role-feature-item">
                                    <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><polyline points="20 6 9 17 4 12"></polyline></svg>
                                    Student & recruiter account management
                                </li>
                                <li class="role-feature-item">
                                    <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><polyline points="20 6 9 17 4 12"></polyline></svg>
                                    Job moderation & fraud prevention
                                </li>
                                <li class="role-feature-item">
                                    <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24"><polyline points="20 6 9 17 4 12"></polyline></svg>
                                    Platform-wide application analytics
                                </li>
                            </ul>
                        </div>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline" style="width: 100%; border-color: var(--accent); color: var(--accent);">Admin Portal</a>
                    </div>
                </div>
            </div>
        </section>

        <!-- Pipeline Workflow Section -->
        <section class="container" id="pipeline">
            <div class="pipeline-section">
                <h2>End-to-End Application Pipeline</h2>
                <p class="pipeline-subtitle">SkillBridge standardizes every step of the hiring journey for full transparency</p>
                <div class="pipeline-steps">
                    <div class="pipeline-step-card">
                        <div class="pipeline-step-num">1</div>
                        <h4>Applied</h4>
                        <p>Student submits resume and profile details to the job posting.</p>
                    </div>
                    <div class="pipeline-step-card">
                        <div class="pipeline-step-num">2</div>
                        <h4>Under Review</h4>
                        <p>Recruiter reviews candidate qualifications, skills, and portfolio.</p>
                    </div>
                    <div class="pipeline-step-card">
                        <div class="pipeline-step-num">3</div>
                        <h4>Shortlisted</h4>
                        <p>Candidate passes preliminary screening and qualifies for next rounds.</p>
                    </div>
                    <div class="pipeline-step-card">
                        <div class="pipeline-step-num">4</div>
                        <h4>Interview</h4>
                        <p>Recruiter schedules technical or HR interview rounds with meeting links.</p>
                    </div>
                    <div class="pipeline-step-card">
                        <div class="pipeline-step-num">5</div>
                        <h4>Selected / Offer</h4>
                        <p>Final decision is communicated with offer updates and onboarding info.</p>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <footer class="footer">
        <div class="container footer-grid">
            <div class="footer-brand">
                <h3>⚡ SkillBridge</h3>
                <p>An enterprise-grade, full-stack Internship &amp; Job Application Management System designed to empower students and streamline campus and fresher recruitment.</p>
            </div>
            <div class="footer-column">
                <h4>For Candidates</h4>
                <div class="footer-links">
                    <a href="${pageContext.request.contextPath}/jobs" class="footer-link">Browse Jobs</a>
                    <a href="${pageContext.request.contextPath}/jobs?type=INTERNSHIP" class="footer-link">Internships</a>
                    <a href="${pageContext.request.contextPath}/register" class="footer-link">Create Account</a>
                </div>
            </div>
            <div class="footer-column">
                <h4>For Employers</h4>
                <div class="footer-links">
                    <a href="${pageContext.request.contextPath}/register?role=RECRUITER" class="footer-link">Post an Internship</a>
                    <a href="${pageContext.request.contextPath}/login" class="footer-link">Recruiter Login</a>
                    <a href="${pageContext.request.contextPath}/#roles" class="footer-link">Hiring Solutions</a>
                </div>
            </div>
            <div class="footer-column">
                <h4>Technology</h4>
                <div class="footer-links">
                    <span class="footer-link">Core Java &amp; Servlets</span>
                    <span class="footer-link">JSP &amp; Jakarta EE 10</span>
                    <span class="footer-link">PostgreSQL 12+</span>
                    <span class="footer-link">Vanilla CSS3 &amp; JS</span>
                </div>
            </div>
        </div>
        <div class="container footer-bottom">
            <p>&copy; 2026 SkillBridge. All rights reserved. Built with Core Java, Servlets, JSP &amp; PostgreSQL.</p>
            <p>MVC + DAO Architectural Standard</p>
        </div>
    </footer>

    <!-- JavaScript -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
