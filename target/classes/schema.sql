-- ==============================================================================
-- SkillBridge Database Schema
-- Database: PostgreSQL 12+
-- Description: Normalized schema for Internship & Job Application Management System
-- ==============================================================================

-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS interviews CASCADE;
DROP TABLE IF EXISTS saved_jobs CASCADE;
DROP TABLE IF EXISTS applications CASCADE;
DROP TABLE IF EXISTS job_skills CASCADE;
DROP TABLE IF EXISTS jobs CASCADE;
DROP TABLE IF EXISTS education CASCADE;
DROP TABLE IF EXISTS student_skills CASCADE;
DROP TABLE IF EXISTS skills CASCADE;
DROP TABLE IF EXISTS recruiter_profiles CASCADE;
DROP TABLE IF EXISTS student_profiles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 1. USERS TABLE
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('STUDENT', 'RECRUITER', 'ADMIN')),
    phone VARCHAR(25),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. STUDENT PROFILES TABLE
CREATE TABLE student_profiles (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    headline VARCHAR(255),
    bio TEXT,
    resume_url VARCHAR(500),
    github_url VARCHAR(255),
    linkedin_url VARCHAR(255),
    portfolio_url VARCHAR(255),
    graduation_year INT CHECK (graduation_year >= 1990 AND graduation_year <= 2040),
    cgpa NUMERIC(4,2) CHECK (cgpa >= 0.0 AND cgpa <= 10.0),
    current_location VARCHAR(120),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. RECRUITER PROFILES TABLE
CREATE TABLE recruiter_profiles (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    company_name VARCHAR(180) NOT NULL,
    company_website VARCHAR(255),
    company_logo VARCHAR(500),
    company_description TEXT,
    company_size VARCHAR(50),
    industry VARCHAR(100),
    location VARCHAR(150),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. SKILLS MASTER TABLE
CREATE TABLE skills (
    id SERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    category VARCHAR(60) DEFAULT 'Technical'
);

-- 5. STUDENT SKILLS (Many-to-Many)
CREATE TABLE student_skills (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    skill_id INT NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    proficiency_level VARCHAR(30) DEFAULT 'Intermediate' CHECK (proficiency_level IN ('Beginner', 'Intermediate', 'Advanced', 'Expert')),
    CONSTRAINT uq_student_skill UNIQUE (student_id, skill_id)
);

-- 6. EDUCATION DETAILS TABLE
CREATE TABLE education (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    institution VARCHAR(200) NOT NULL,
    degree VARCHAR(100) NOT NULL,
    field_of_study VARCHAR(120) NOT NULL,
    start_date DATE,
    end_date DATE,
    grade_percentage NUMERIC(5,2) CHECK (grade_percentage >= 0.0 AND grade_percentage <= 100.0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 7. JOBS & INTERNSHIPS TABLE
CREATE TABLE jobs (
    id SERIAL PRIMARY KEY,
    recruiter_id INT NOT NULL REFERENCES recruiter_profiles(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    requirements TEXT,
    responsibilities TEXT,
    job_type VARCHAR(30) NOT NULL CHECK (job_type IN ('FULL_TIME', 'PART_TIME', 'INTERNSHIP', 'REMOTE', 'CONTRACT')),
    location VARCHAR(150) NOT NULL,
    experience_level VARCHAR(30) NOT NULL CHECK (experience_level IN ('FRESHER', '0-1_YEARS', '1-3_YEARS', '3+_YEARS')),
    salary_min NUMERIC(12,2) DEFAULT 0.00,
    salary_max NUMERIC(12,2) DEFAULT 0.00,
    vacancies INT NOT NULL DEFAULT 1 CHECK (vacancies > 0),
    deadline DATE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 8. JOB SKILLS REQUIRED (Many-to-Many)
CREATE TABLE job_skills (
    id SERIAL PRIMARY KEY,
    job_id INT NOT NULL REFERENCES jobs(id) ON DELETE CASCADE,
    skill_id INT NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    CONSTRAINT uq_job_skill UNIQUE (job_id, skill_id)
);

-- 9. APPLICATIONS TABLE
CREATE TABLE applications (
    id SERIAL PRIMARY KEY,
    job_id INT NOT NULL REFERENCES jobs(id) ON DELETE CASCADE,
    student_id INT NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    cover_letter TEXT,
    resume_url VARCHAR(500),
    status VARCHAR(30) NOT NULL DEFAULT 'APPLIED' CHECK (status IN ('APPLIED', 'UNDER_REVIEW', 'SHORTLISTED', 'INTERVIEW', 'SELECTED', 'REJECTED')),
    notes TEXT,
    applied_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_job_student_application UNIQUE (job_id, student_id)
);

-- 10. SAVED / BOOKMARKED JOBS
CREATE TABLE saved_jobs (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    job_id INT NOT NULL REFERENCES jobs(id) ON DELETE CASCADE,
    saved_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_student_saved_job UNIQUE (student_id, job_id)
);

-- 11. INTERVIEWS TABLE
CREATE TABLE interviews (
    id SERIAL PRIMARY KEY,
    application_id INT NOT NULL REFERENCES applications(id) ON DELETE CASCADE,
    interview_date DATE NOT NULL,
    interview_time TIME NOT NULL,
    interview_mode VARCHAR(30) NOT NULL DEFAULT 'ONLINE' CHECK (interview_mode IN ('ONLINE', 'IN_PERSON', 'PHONE')),
    meeting_link_or_location TEXT NOT NULL,
    round_name VARCHAR(100) NOT NULL DEFAULT 'Technical Round 1',
    instructions TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED', 'RESCHEDULED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================================================
-- INDEXES FOR PERFORMANCE OPTIMIZATION
-- ==============================================================================
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_jobs_recruiter ON jobs(recruiter_id);
CREATE INDEX idx_jobs_type ON jobs(job_type);
CREATE INDEX idx_jobs_experience ON jobs(experience_level);
CREATE INDEX idx_jobs_active ON jobs(is_active);
CREATE INDEX idx_applications_job ON applications(job_id);
CREATE INDEX idx_applications_student ON applications(student_id);
CREATE INDEX idx_applications_status ON applications(status);
CREATE INDEX idx_saved_jobs_student ON saved_jobs(student_id);
CREATE INDEX idx_interviews_application ON interviews(application_id);

-- ==============================================================================
-- SEED DATA
-- Default Admin Account: admin@skillbridge.com / admin123
-- BCrypt hash for 'admin123': $2a$10$wNqg1/vK0xX6dG1B6XvGxeRzYgq3i5l.X9J2Q4xT5jU7rO6rN9Yy2
-- ==============================================================================
INSERT INTO users (email, password_hash, full_name, role, phone, is_active)
VALUES 
('admin@skillbridge.com', '$2a$10$wNqg1/vK0xX6dG1B6XvGxeRzYgq3i5l.X9J2Q4xT5jU7rO6rN9Yy2', 'System Administrator', 'ADMIN', '+1-800-555-0199', TRUE)
ON CONFLICT (email) DO NOTHING;

-- Pre-populate Popular Industry Skills
INSERT INTO skills (name, category) VALUES
('Java', 'Backend Development'),
('Python', 'Programming Languages'),
('SQL / PostgreSQL', 'Database Management'),
('HTML5 & CSS3', 'Frontend Development'),
('JavaScript', 'Frontend Development'),
('Spring Boot', 'Frameworks'),
('React.js', 'Frontend Frameworks'),
('Node.js', 'Backend Frameworks'),
('Git & GitHub', 'DevOps & Tools'),
('Docker', 'DevOps & Tools'),
('RESTful API Design', 'Web Services'),
('Data Structures & Algorithms', 'Computer Science Fundamentals'),
('Machine Learning', 'Data Science'),
('Cloud Computing (AWS/Azure/GCP)', 'Cloud'),
('Communication & Teamwork', 'Soft Skills')
ON CONFLICT (name) DO NOTHING;

-- Seed Recruiter Accounts (Password: recruiter123 -> $2a$10$wNqg1/vK0xX6dG1B6XvGxeRzYgq3i5l.X9J2Q4xT5jU7rO6rN9Yy2)
INSERT INTO users (id, email, password_hash, full_name, role, phone, is_active)
VALUES 
(2, 'recruiter@nexuscloud.io', '$2a$10$wNqg1/vK0xX6dG1B6XvGxeRzYgq3i5l.X9J2Q4xT5jU7rO6rN9Yy2', 'Sarah Jenkins', 'RECRUITER', '+1-555-0144', TRUE),
(3, 'hr@apextech.com', '$2a$10$wNqg1/vK0xX6dG1B6XvGxeRzYgq3i5l.X9J2Q4xT5jU7rO6rN9Yy2', 'David Chen', 'RECRUITER', '+1-555-0188', TRUE)
ON CONFLICT (email) DO NOTHING;

-- Reset users_id_seq if needed
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

-- Seed Recruiter Profiles
INSERT INTO recruiter_profiles (id, user_id, company_name, company_website, company_description, company_size, industry, location)
VALUES
(1, 2, 'Nexus Cloud Technologies', 'https://nexuscloud.io', 'Leading enterprise cloud infrastructure and developer tooling startup building next-gen web platforms.', '50-200 Employees', 'Information Technology', 'Bangalore, India'),
(2, 3, 'Apex Innovations Ltd', 'https://apextech.com', 'High-growth fintech software company providing secure payment gateways and API microservices.', '200-500 Employees', 'FinTech', 'Remote / San Francisco')
ON CONFLICT DO NOTHING;

SELECT setval('recruiter_profiles_id_seq', (SELECT MAX(id) FROM recruiter_profiles));

-- Seed Job Postings
INSERT INTO jobs (id, recruiter_id, title, description, requirements, responsibilities, job_type, location, experience_level, salary_min, salary_max, vacancies, deadline, is_active)
VALUES
(1, 1, 'Full Stack Java Developer Intern', 
 'We are looking for an ambitious Full Stack Java Intern to join our core product engineering team. You will work on designing robust REST APIs, writing clean Java backend services, and collaborating on our responsive web applications.',
 'Solid understanding of Core Java 17+, Object-Oriented Programming, SQL databases (PostgreSQL), and HTML/CSS/JavaScript fundamentals. Good problem solving skills.',
 'Develop and maintain Java Servlet/JSP based micro-modules; Write unit tests and optimize PostgreSQL queries; Participate in daily agile standups and code reviews.',
 'INTERNSHIP', 'Bangalore / Hybrid', 'FRESHER', 25000.00, 35000.00, 3, CURRENT_DATE + INTERVAL '45 days', TRUE),

(2, 1, 'Associate Software Engineer (Java / Backend)',
 'Join Nexus Cloud as an Associate Backend Engineer. You will build high-concurrency APIs, handle database migrations, and build enterprise services.',
 'Degree in Computer Science or related field. Hands-on experience with Java, Spring Boot or Jakarta EE, PostgreSQL, and Git version control.',
 'Architect database schemas; Implement secure session and authentication workflows; Troubleshoot production logs and maintain system reliability.',
 'FULL_TIME', 'Bangalore, India', '0-1_YEARS', 600000.00, 850000.00, 2, CURRENT_DATE + INTERVAL '60 days', TRUE),

(3, 2, 'Junior Frontend Web Developer',
 'Apex Innovations is hiring a Junior Frontend Developer to craft pixel-perfect user interfaces, dynamic dashboards, and performant web portals.',
 'Strong proficiency in HTML5, CSS3, modern Vanilla JavaScript (ES6+), responsive design principles, and UI/UX design tokens.',
 'Implement interactive web dashboards; Ensure cross-browser compatibility; Optimize client-side rendering and accessibility standards.',
 'REMOTE', 'Remote (Global)', 'FRESHER', 450000.00, 650000.00, 2, CURRENT_DATE + INTERVAL '30 days', TRUE)
ON CONFLICT DO NOTHING;

SELECT setval('jobs_id_seq', (SELECT MAX(id) FROM jobs));

-- Seed Job Skills Mapping
INSERT INTO job_skills (job_id, skill_id) VALUES
(1, 1), (1, 3), (1, 4), (1, 5), (1, 9),
(2, 1), (2, 3), (2, 6), (2, 11),
(3, 4), (3, 5), (3, 7), (3, 9)
ON CONFLICT DO NOTHING;
