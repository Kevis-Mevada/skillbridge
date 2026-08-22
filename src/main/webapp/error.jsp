<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>An Error Occurred | SkillBridge</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body style="min-height: 100vh; display: flex; flex-direction: column; justify-content: center; align-items: center; background: var(--bg-alt); padding: 2rem;">

    <div class="panel-card" style="max-width: 560px; width: 100%; text-align: center; padding: 3rem 2.5rem; box-shadow: var(--shadow-lg);">
        <div style="font-size: 4.5rem; margin-bottom: 1rem; line-height: 1;">⚠️</div>
        <h2 style="font-size: 1.75rem; margin-bottom: 0.75rem; color: var(--text-primary);">Something Went Wrong</h2>
        <p style="color: var(--text-secondary); margin-bottom: 1.5rem; line-height: 1.6;">
            We encountered an issue fulfilling your request. Please try navigating back or returning to the home page.
        </p>

        <% if (exception != null && exception.getMessage() != null) { %>
            <div style="background: var(--bg-alt); padding: 1rem; border-radius: var(--radius-md); font-family: monospace; font-size: 0.85rem; color: var(--danger); text-align: left; margin-bottom: 1.5rem; word-break: break-all;">
                <%= exception.getMessage() %>
            </div>
        <% } %>

        <div style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                ⚡ Return Home
            </a>
            <a href="javascript:history.back()" class="btn btn-outline btn-lg">
                ← Go Back
            </a>
        </div>
    </div>

</body>
</html>
