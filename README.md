# SkillBridge – Internship & Job Application Management System

A robust, enterprise-grade full-stack web application designed to bridge the gap between students/freshers looking for internships and jobs, recruiters posting opportunities and managing their hiring pipelines, and administrators governing platform security and operations.

Built **strictly with Core Java, Jakarta Servlets, JSP, JDBC, Vanilla CSS3, Vanilla JavaScript, PostgreSQL, Apache Tomcat 10+, and Maven** — with **zero reliance on Spring Boot, React, Angular, Tailwind, or Bootstrap**.

---

## 🌟 Key Features by User Role

### 🎓 1. Student / Job Seeker Module
- **Authentication & Security**: Secure registration, login, session timeout, and BCrypt password encryption.
- **Dynamic Profile & Portfolio**:
  - Personal information, career headline, bio, location, and graduation year.
  - Interactive **Skills Tag Cloud** with 4 proficiency levels (*Beginner, Intermediate, Advanced, Expert*).
  - **Education Timeline CRUD** (institution, degree, field of study, timeline, grades).
  - External portfolio links: Cloud Resume URL, GitHub, LinkedIn, and personal portfolio.
  - **Real-Time Profile Strength Indicator** (% bar calculating completion).
- **Opportunity Discovery Engine**:
  - Live search by job title, company name, keywords, and description.
  - Multi-criteria filter sidebar: *Job Type (Internship, Full Time, Remote, Part Time, Contract)*, *Experience Level (Fresher, 0-1, 1-3, 3+ years)*, *Location*, and *Skills tags*.
  - Detailed Opportunity page with company profile, salary range, vacancies, requirements, responsibilities, and application deadline countdown.
- **Application & Lifecycle Tracking**:
  - 1-Click Application form with custom cover letter notes and auto-filled cloud resume verification.
  - **5-Stage Visual Pipeline Progress Tracker**:
    $$\text{Applied} \longrightarrow \text{Under Review} \longrightarrow \text{Shortlisted} \longrightarrow \text{Interview} \longrightarrow \text{Selected / Rejected}$$
  - Candidate notes box displaying real-time feedback and instructions from recruiters.
  - Ability to withdraw pending applications safely.
- **Saved Jobs / Bookmarking**: Save opportunities to revisit and apply later.
- **Interview Portal**: View upcoming technical and HR interview rounds with direct meeting launch links (*Google Meet, Zoom, Teams*) or in-person office addresses.

---

### 💼 2. Recruiter / Employer Module
- **Company Branding & Settings**: Manage company name, official website, industry domain, company size, office location, and culture summary.
- **Hiring Metrics Dashboard**: Live metric cards tracking *Active Postings, Total Candidates Applied, Under Review, Shortlisted, Interviews Scheduled, and Hired Candidates*.
- **Job Posting & Lifecycle Management**:
  - Create rich job and internship postings with custom requirements, responsibilities, salary ranges, vacancies, deadlines, and multi-selected required skill tags.
  - Edit existing postings, toggle active/closed visibility, or permanently delete expired listings.
- **Applicant Pipeline & Resume Review**:
  - Filter applicants by specific job opening and pipeline status.
  - One-click inspection of candidate resumes, academic CGPA, GitHub, and LinkedIn profiles.
  - Inline application status transitions (`APPLIED` → `UNDER_REVIEW` → `SHORTLISTED` → `INTERVIEW` → `SELECTED` / `REJECTED`) with personalized candidate feedback notes.
- **Interview Scheduling Engine**:
  - Schedule interview rounds (e.g. *Technical Round 1, System Design, HR Discussion*).
  - Support for `ONLINE` (video meeting links), `IN_PERSON` (office premises), and `PHONE` screening modes.
  - Real-time candidate notification and status updates (*Scheduled, Completed, Rescheduled, Cancelled*).

---

### 🛡️ 3. Administrator / Governance Module
- **Platform Analytics**: High-level telemetry measuring *Total Registered Students, Verified Employers, Active Opportunities, Total Applications Submitted, and Interviews Conducted*.
- **Student Account Governance**: View candidate roster, inspect academic records, activate/deactivate accounts, and delete inactive users.
- **Employer Moderation**: View all hiring organizations, verify company credentials, and manage recruiter account access.
- **Job Moderation Engine**: System-wide oversight over all active job postings with 1-click status override or removal of spam listings.
- **Global Applications Audit**: Comprehensive log of all submissions across all companies.

---

## 🏗️ Architecture & Design Patterns

The application follows the clean **MVC (Model-View-Controller) + DAO (Data Access Object) + Service Layer Architecture**:

```
SkillBridge Web Application
├── Presentation Layer (JSP + Custom Vanilla CSS + Vanilla JS)
│     ├── /WEB-INF/views/student/   (Student Dashboard, Profile, Applications, Saved Jobs, Interviews)
│     ├── /WEB-INF/views/recruiter/ (Recruiter Dashboard, Post Job, Manage Jobs, Applicants, Interviews)
│     ├── /WEB-INF/views/admin/     (Admin Dashboard, Manage Users, Moderate Jobs, Applications)
│     └── /jobs.jsp, /job-details.jsp, /login.jsp, /register.jsp, /index.jsp
│
├── Controller Layer (Jakarta Servlets)
│     ├── Auth: LoginServlet, RegisterServlet, LogoutServlet
│     ├── Student: StudentDashboardServlet, StudentProfileServlet, ApplyJobServlet, StudentApplicationsServlet, SavedJobsServlet, StudentInterviewsServlet
│     ├── Recruiter: RecruiterDashboardServlet, PostJobServlet, EditJobServlet, ManageJobsServlet, RecruiterApplicantsServlet, ScheduleInterviewServlet
│     ├── Admin: AdminDashboardServlet, AdminStudentsServlet, AdminRecruitersServlet, AdminJobsServlet, AdminApplicationsServlet
│     └── Public: JobBrowseServlet, JobDetailsServlet
│
├── Security & Interceptor Layer (Jakarta Filter)
│     └── AuthFilter (Role-Based Access Control: STUDENT, RECRUITER, ADMIN + Anti-Caching)
│
├── Service Layer (Business Logic & Workflow Integrity)
│     └── UserService, StudentService, JobService, ApplicationService, RecruiterService, InterviewService, AdminService
│
├── Data Access Layer (JDBC DAOs with PreparedStatements)
│     └── UserDAO, StudentProfileDAO, RecruiterDAO, SkillDAO, EducationDAO, JobDAO, ApplicationDAO, SavedJobDAO, InterviewDAO
│
├── Database Connection Provider
│     └── DBConnection (Optimized HikariCP connection pooling / Thread-safe JDBC connections)
│
└── Database (PostgreSQL 12+)
      └── 11 Normalized Tables with Foreign Keys, Cascades, CHECK Constraints, and Performance Indexes
```

---

## 💻 Technology Stack

| Component | Technology | Description |
| :--- | :--- | :--- |
| **Language** | Java 17 (LTS) | Core Java (OOP, Collections, Streams, Multithreading) |
| **Web Tier** | Jakarta Servlet 6.0 & JSP 3.1 | Request routing, MVC controller flow, dynamic rendering |
| **Persistence** | JDBC with `PreparedStatement` | SQL injection-safe relational database operations |
| **Database** | PostgreSQL 12+ | Normalized schema, constraints, cascading deletes, indexes |
| **Styling** | Custom Vanilla CSS3 | Custom Design Token System, CSS Variables, Glassmorphism, Responsive Grid/Flexbox |
| **Client Scripting**| Modern Vanilla JavaScript (ES6+) | Dynamic DOM manipulation, real-time validation, interactive modals |
| **Security** | jBCrypt 0.4 | Industry-standard salted password hashing |
| **Build & Packaging** | Apache Maven 3.8+ | Dependency management and `.war` deployment packaging |
| **Servlet Container** | Apache Tomcat 10.1+ / 11+ | Compatible with Jakarta EE 10 namespace |

---

## 🗄️ Database Schema & Configuration

### 1. Database Credentials (`db.properties`)
Located in `src/main/resources/db.properties`:
```properties
db.driver=org.postgresql.Driver
db.url=jdbc:postgresql://localhost:5432/skillbridge
db.username=postgres
db.password=root
```

### 2. Initializing the Database
Ensure PostgreSQL is running locally on port `5432`. Create the database and run the schema script:

```bash
# 1. Open PostgreSQL Shell (psql)
psql -U postgres

# 2. Create database
CREATE DATABASE skillbridge;

# 3. Connect to database
\c skillbridge

# 4. Execute the schema & seeds file
\i database/schema.sql
```

*(Alternatively, run the automated database setup utility: `mvn compile exec:java -Dexec.mainClass="com.skillbridge.util.DatabaseInitializer"`)*

---

## 🚀 Build & Deployment Instructions

### Prerequisites
1. **Java Development Kit (JDK 17 or higher)**
2. **Apache Maven 3.8+**
3. **Apache Tomcat 10.1+ or 11+** (Supports Jakarta EE namespace)
4. **PostgreSQL Server 12+**

### Step 1: Clone & Build with Maven
```bash
# Navigate to the project root directory
cd Skillbridge

# Clean and package the application into a WAR file
mvn clean package
```
This generates `target/skillbridge.war`.

### Step 2: Deploy to Apache Tomcat
1. Copy `target/skillbridge.war` into the Tomcat `webapps/` folder:
   ```bash
   cp target/skillbridge.war /path/to/tomcat/webapps/
   ```
2. Start Apache Tomcat:
   - **Windows**: `bin\startup.bat`
   - **Linux / macOS**: `bin/startup.sh`
3. Access the web application in your browser:
   ```
   http://localhost:8080/skillbridge
   ```

---

## 🔑 Pre-Seeded Demo Credentials

| Role | Email | Password | Details |
| :--- | :--- | :--- | :--- |
| **🛡️ System Admin** | `admin@skillbridge.com` | `admin123` | Full administrative governance & moderation access |
| **💼 Recruiter 1** | `recruiter@nexuscloud.io` | `recruiter123` | Nexus Cloud Technologies (2 live postings) |
| **💼 Recruiter 2** | `hr@apextech.com` | `recruiter123` | Apex Innovations Ltd (1 live posting) |
| **🎓 Student** | *(Self-Register or create via UI)* | *(Your Password)* | Register at `/register` as Student |

---

## 🗺️ Application URL Sitemap & RBAC Guide

### 🌐 Public Endpoints
- `GET /` or `GET /index.jsp` — Landing Page showcasing featured jobs, statistics, and platform highlights.
- `GET /jobs` — Job & Internship Search Directory with multi-criteria filters.
- `GET /jobs/details?id={id}` — Detailed Opportunity view with recruiter profile.
- `GET/POST /login` — Unified Authentication endpoint with automatic role dispatch.
- `GET/POST /register` — Candidate and Recruiter self-service registration.
- `GET /logout` — Invalidate session and redirect to landing page.

### 🎓 Student Endpoints (`/student/*`)
- `GET /student/dashboard` — Student Dashboard with metrics and profile strength.
- `GET/POST /student/profile` — Profile settings, education timeline, and skill proficiency.
- `GET/POST /student/apply?jobId={id}` — Job application submission form.
- `GET/POST /student/applications` — Real-time 5-stage status tracker and withdrawal.
- `GET/POST /student/saved-jobs` & `/student/save-job` — Bookmarked opportunities list.
- `GET /student/interviews` — Scheduled interview rounds and video meeting links.

### 💼 Recruiter Endpoints (`/recruiter/*`)
- `GET /recruiter/dashboard` — Hiring metrics and applicant overview.
- `GET/POST /recruiter/post-job` — Create new job/internship listings with skill tags.
- `GET/POST /recruiter/edit-job?id={id}` — Update existing postings.
- `GET/POST /recruiter/manage-jobs` — View postings, toggle active/closed, or delete.
- `GET /recruiter/applicants` — Review candidate resumes, CGPA, and portfolio links.
- `POST /recruiter/application/status` — Advance candidate pipeline (`Applied` → `Selected`/`Rejected`).
- `GET/POST /recruiter/schedule-interview` — Schedule technical/HR rounds.
- `GET/POST /recruiter/interviews` — Manage scheduled candidate rounds.
- `GET/POST /recruiter/profile` — Update company profile and employer branding.

### 🛡️ Admin Endpoints (`/admin/*`)
- `GET /admin/dashboard` — Platform telemetry and health statistics.
- `GET/POST /admin/students` — Manage student user accounts and toggle active state.
- `GET/POST /admin/recruiters` — Manage employer companies and recruiter status.
- `GET/POST /admin/jobs` — Platform-wide job moderation and removal.
- `GET /admin/applications` — System-wide job application pipeline audit.

---

## 🔒 Security Best Practices Implemented
1. **SQL Injection Prevention**: 100% parameterized `PreparedStatement` queries across all DAOs.
2. **Password Cryptography**: BCrypt hashing with strong salt rounds via `PasswordUtil`.
3. **Session Hijacking Mitigation**: `HttpOnly` cookies and strict 60-minute session expiration.
4. **Role-Based Access Control (RBAC)**: Centralized `AuthFilter` intercepting all `/student/*`, `/recruiter/*`, and `/admin/*` routes.
5. **Anti-Caching Headers**: Prevents sensitive user portal data from remaining in public browser history.
6. **Graceful Error Handling**: Custom `404.jsp`, `500.jsp`, and `error.jsp` preventing stack trace leaks.

---

## 📄 License & Attribution
Developed with ❤️ as a full-stack Java demonstration project for **SkillBridge – Internship & Job Application Management System**.
#   s k i l l b r i d g e  
 "# skillbridge" 
