<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Page Not Found | SkillBridge</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body style="min-height: 100vh; display: flex; flex-direction: column; justify-content: center; align-items: center; background: var(--bg-alt); padding: 2rem;">

    <div class="panel-card" style="max-width: 540px; width: 100%; text-align: center; padding: 3rem 2.5rem; box-shadow: var(--shadow-lg);">
        <div style="font-size: 5rem; margin-bottom: 1rem; line-height: 1;">🧭</div>
        <h1 style="font-size: 3.5rem; font-weight: 800; color: var(--primary); margin-bottom: 0.5rem;">404</h1>
        <h2 style="font-size: 1.5rem; margin-bottom: 1rem; color: var(--text-primary);">Oops! Page Not Found</h2>
        <p style="color: var(--text-secondary); margin-bottom: 2rem; line-height: 1.6;">
            The page you are looking for might have been removed, had its name changed, or is temporarily unavailable.
        </p>

        <div style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                ⚡ Return Home
            </a>
            <a href="${pageContext.request.contextPath}/jobs" class="btn btn-outline btn-lg">
                🔍 Explore Jobs
            </a>
        </div>
    </div>

</body>
</html>
