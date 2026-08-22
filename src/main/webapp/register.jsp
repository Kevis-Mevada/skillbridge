<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Create your SkillBridge account as a Student or Recruiter.">
    <title>Create Account – SkillBridge</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-body">

    <!-- Minimal Top Bar -->
    <header class="auth-header">
        <div class="container auth-nav">
            <a href="${pageContext.request.contextPath}/" class="brand-logo">
                <span class="brand-icon">⚡</span>
                <span>SkillBridge</span>
            </a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-sm btn-outline">← Back to Home</a>
        </div>
    </header>

    <!-- Auth Container -->
    <main class="auth-wrapper">
        <div class="auth-card register-card">
            <!-- Header -->
            <div class="auth-card-header">
                <h2>Join SkillBridge</h2>
                <p>Select your account type to get started with your career or hiring journey</p>
            </div>

            <!-- Flash Error Alerts -->
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-danger" id="auth-error-alert">
                    <span class="alert-icon">⚠️</span>
                    <span><%= request.getAttribute("errorMessage") %></span>
                </div>
            <% } %>

            <!-- Role Selector Tabs -->
            <%
                String currentRole = (String) request.getAttribute("selectedRole");
                if (currentRole == null || currentRole.isEmpty()) currentRole = "STUDENT";
                boolean isRecruiter = "RECRUITER".equalsIgnoreCase(currentRole);
            %>
            <div class="role-selector-tabs">
                <button type="button" class="role-tab <%= !isRecruiter ? "active" : "" %>" id="tab-student" onclick="selectRole('STUDENT')">
                    <span class="tab-icon">🎓</span>
                    <span>Student / Fresher</span>
                </button>
                <button type="button" class="role-tab <%= isRecruiter ? "active" : "" %>" id="tab-recruiter" onclick="selectRole('RECRUITER')">
                    <span class="tab-icon">💼</span>
                    <span>Recruiter / Employer</span>
                </button>
            </div>

            <!-- Registration Form -->
            <form action="${pageContext.request.contextPath}/register" method="POST" class="auth-form" id="register-form" novalidate>
                <!-- Hidden Role Field -->
                <input type="hidden" name="role" id="role-input" value="<%= currentRole %>">

                <div class="form-row">
                    <!-- Full Name -->
                    <div class="form-group">
                        <label for="fullName" class="form-label">Full Name *</label>
                        <div class="input-wrapper">
                            <input type="text" 
                                   id="fullName" 
                                   name="fullName" 
                                   class="form-control" 
                                   placeholder="e.g. Alex Johnson" 
                                   value="<%= request.getAttribute("fullName") != null ? request.getAttribute("fullName") : "" %>" 
                                   required>
                            <span class="input-icon">👤</span>
                        </div>
                        <span class="form-error-text" id="fullName-error"></span>
                    </div>

                    <!-- Email Address -->
                    <div class="form-group">
                        <label for="email" class="form-label">Email Address *</label>
                        <div class="input-wrapper">
                            <input type="email" 
                                   id="email" 
                                   name="email" 
                                   class="form-control" 
                                   placeholder="name@example.com" 
                                   value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" 
                                   required>
                            <span class="input-icon">✉️</span>
                        </div>
                        <span class="form-error-text" id="email-error"></span>
                    </div>
                </div>

                <!-- Recruiter-Only Company Field -->
                <div class="form-group" id="company-field-group" style="<%= isRecruiter ? "" : "display: none;" %>">
                    <label for="companyName" class="form-label">Company / Organization Name *</label>
                    <div class="input-wrapper">
                        <input type="text" 
                               id="companyName" 
                               name="companyName" 
                               class="form-control" 
                               placeholder="e.g. Acme Technologies Inc." 
                               value="<%= request.getAttribute("companyName") != null ? request.getAttribute("companyName") : "" %>">
                        <span class="input-icon">🏢</span>
                    </div>
                    <span class="form-error-text" id="companyName-error"></span>
                </div>

                <!-- Phone Number -->
                <div class="form-group">
                    <label for="phone" class="form-label">Phone Number (Optional)</label>
                    <div class="input-wrapper">
                        <input type="tel" 
                               id="phone" 
                               name="phone" 
                               class="form-control" 
                               placeholder="+1 (555) 019-2834" 
                               value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : "" %>">
                        <span class="input-icon">📞</span>
                    </div>
                </div>

                <div class="form-row">
                    <!-- Password -->
                    <div class="form-group">
                        <label for="password" class="form-label">Password * (Min 6 chars)</label>
                        <div class="input-wrapper">
                            <input type="password" 
                                   id="password" 
                                   name="password" 
                                   class="form-control" 
                                   placeholder="••••••••" 
                                   required>
                        </div>
                        <span class="form-error-text" id="password-error"></span>
                    </div>

                    <!-- Confirm Password -->
                    <div class="form-group">
                        <label for="confirmPassword" class="form-label">Confirm Password *</label>
                        <div class="input-wrapper">
                            <input type="password" 
                                   id="confirmPassword" 
                                   name="confirmPassword" 
                                   class="form-control" 
                                   placeholder="••••••••" 
                                   required>
                        </div>
                        <span class="form-error-text" id="confirmPassword-error"></span>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary btn-block" id="btn-register-submit">
                    <span>Create Account</span>
                    <span>→</span>
                </button>
            </form>

            <!-- Footer Link -->
            <div class="auth-card-footer">
                <p>Already have an account? <a href="${pageContext.request.contextPath}/login" class="auth-link">Log In</a></p>
            </div>
        </div>
    </main>

    <script>
        function selectRole(role) {
            const roleInput = document.getElementById('role-input');
            const studentTab = document.getElementById('tab-student');
            const recruiterTab = document.getElementById('tab-recruiter');
            const companyGroup = document.getElementById('company-field-group');

            roleInput.value = role;

            if (role === 'RECRUITER') {
                recruiterTab.classList.add('active');
                studentTab.classList.remove('active');
                companyGroup.style.display = 'block';
            } else {
                studentTab.classList.add('active');
                recruiterTab.classList.remove('active');
                companyGroup.style.display = 'none';
            }
        }

        // Client-side Validation
        const registerForm = document.getElementById('register-form');
        if (registerForm) {
            registerForm.addEventListener('submit', (e) => {
                let valid = true;

                const fullName = document.getElementById('fullName');
                const email = document.getElementById('email');
                const password = document.getElementById('password');
                const confirmPassword = document.getElementById('confirmPassword');
                const roleInput = document.getElementById('role-input');
                const companyName = document.getElementById('companyName');

                const fullNameError = document.getElementById('fullName-error');
                const emailError = document.getElementById('email-error');
                const passwordError = document.getElementById('password-error');
                const confirmPasswordError = document.getElementById('confirmPassword-error');
                const companyNameError = document.getElementById('companyName-error');

                fullNameError.textContent = '';
                emailError.textContent = '';
                passwordError.textContent = '';
                confirmPasswordError.textContent = '';
                if (companyNameError) companyNameError.textContent = '';

                if (!fullName.value.trim()) {
                    fullNameError.textContent = 'Please enter your full name.';
                    valid = false;
                }

                if (!email.value.trim() || !email.value.includes('@')) {
                    emailError.textContent = 'Please enter a valid email address.';
                    valid = false;
                }

                if (roleInput.value === 'RECRUITER' && (!companyName.value || !companyName.value.trim())) {
                    companyNameError.textContent = 'Company name is required for recruiters.';
                    valid = false;
                }

                if (!password.value || password.value.length < 6) {
                    passwordError.textContent = 'Password must be at least 6 characters.';
                    valid = false;
                }

                if (password.value !== confirmPassword.value) {
                    confirmPasswordError.textContent = 'Passwords do not match.';
                    valid = false;
                }

                if (!valid) {
                    e.preventDefault();
                }
            });
        }
    </script>
</body>
</html>
