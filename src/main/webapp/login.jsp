<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Log into SkillBridge - Access your student dashboard, recruiter candidate pipeline, or administrative controls.">
    <title>Log In – SkillBridge</title>
    
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
        <div class="auth-card">
            <!-- Card Header -->
            <div class="auth-card-header">
                <h2>Welcome Back</h2>
                <p>Enter your credentials to access your SkillBridge account</p>
            </div>

            <!-- Flash Error / Success Alerts -->
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-danger" id="auth-error-alert">
                    <span class="alert-icon">⚠️</span>
                    <span><%= request.getAttribute("errorMessage") %></span>
                </div>
            <% } %>

            <% if (request.getAttribute("successMessage") != null) { %>
                <div class="alert alert-success" id="auth-success-alert">
                    <span class="alert-icon">✅</span>
                    <span><%= request.getAttribute("successMessage") %></span>
                </div>
            <% } %>

            <!-- Login Form -->
            <form action="${pageContext.request.contextPath}/login" method="POST" class="auth-form" id="login-form" novalidate>
                <div class="form-group">
                    <label for="email" class="form-label">Email Address</label>
                    <div class="input-wrapper">
                        <input type="email" 
                               id="email" 
                               name="email" 
                               class="form-control" 
                               placeholder="name@example.com" 
                               value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" 
                               required 
                               autocomplete="email">
                        <span class="input-icon">✉️</span>
                    </div>
                    <span class="form-error-text" id="email-error"></span>
                </div>

                <div class="form-group">
                    <div class="form-label-row">
                        <label for="password" class="form-label">Password</label>
                    </div>
                    <div class="input-wrapper">
                        <input type="password" 
                               id="password" 
                               name="password" 
                               class="form-control" 
                               placeholder="••••••••" 
                               required 
                               autocomplete="current-password">
                        <button type="button" class="btn-toggle-password" id="toggle-password" aria-label="Toggle password visibility">👁️</button>
                    </div>
                    <span class="form-error-text" id="password-error"></span>
                </div>

                <button type="submit" class="btn btn-primary btn-block" id="btn-login-submit">
                    <span>Sign In</span>
                    <span>→</span>
                </button>
            </form>

            <!-- Quick Demo Login Helper for Testing -->
            <div class="demo-login-box">
                <p class="demo-title">⚡ Quick Demo Autofill:</p>
                <div class="demo-buttons">
                    <button type="button" class="btn-demo" onclick="autofillLogin('admin@skillbridge.com', 'admin123')">
                        🛡️ Admin
                    </button>
                </div>
            </div>

            <!-- Footer Switch -->
            <div class="auth-card-footer">
                <p>Don't have an account yet? <a href="${pageContext.request.contextPath}/register" class="auth-link">Create Account</a></p>
            </div>
        </div>
    </main>

    <script>
        // Toggle Password Visibility
        const toggleBtn = document.getElementById('toggle-password');
        const passwordInput = document.getElementById('password');
        if (toggleBtn && passwordInput) {
            toggleBtn.addEventListener('click', () => {
                const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);
                toggleBtn.textContent = type === 'password' ? '👁️' : '🙈';
            });
        }

        // Quick Autofill Function for testing
        function autofillLogin(email, password) {
            const emailInput = document.getElementById('email');
            const passInput = document.getElementById('password');
            if (emailInput && passInput) {
                emailInput.value = email;
                passInput.value = password;
            }
        }

        // Client-side Validation
        const loginForm = document.getElementById('login-form');
        if (loginForm) {
            loginForm.addEventListener('submit', (e) => {
                let valid = true;
                const email = document.getElementById('email');
                const password = document.getElementById('password');
                const emailError = document.getElementById('email-error');
                const passwordError = document.getElementById('password-error');

                emailError.textContent = '';
                passwordError.textContent = '';

                if (!email.value.trim() || !email.value.includes('@')) {
                    emailError.textContent = 'Please enter a valid email address.';
                    valid = false;
                }

                if (!password.value) {
                    passwordError.textContent = 'Please enter your password.';
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
