/**
 * SkillBridge - Core Frontend JavaScript (Vanilla JS)
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Sticky Navbar Elevation on Scroll
    const navbar = document.querySelector('.navbar');
    if (navbar) {
        window.addEventListener('scroll', () => {
            if (window.scrollY > 20) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }
        });
    }

    // 2. Interactive Search Pills
    const searchPills = document.querySelectorAll('.tag-pill');
    const keywordInput = document.getElementById('search-keyword');
    if (searchPills.length > 0 && keywordInput) {
        searchPills.forEach(pill => {
            pill.addEventListener('click', (e) => {
                e.preventDefault();
                keywordInput.value = pill.textContent.trim();
                keywordInput.focus();
            });
        });
    }

    // 3. Stats Counter Animation
    const stats = document.querySelectorAll('.stat-number');
    let animated = false;

    const animateCounters = () => {
        stats.forEach(stat => {
            const target = parseInt(stat.getAttribute('data-target'), 10);
            if (isNaN(target)) return;

            let current = 0;
            const increment = Math.ceil(target / 40);
            const timer = setInterval(() => {
                current += increment;
                if (current >= target) {
                    stat.textContent = target + (stat.getAttribute('data-suffix') || '+');
                    clearInterval(timer);
                } else {
                    stat.textContent = current + (stat.getAttribute('data-suffix') || '');
                }
            }, 30);
        });
    };

    if (stats.length > 0) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting && !animated) {
                    animated = true;
                    animateCounters();
                }
            });
        }, { threshold: 0.5 });

        const statsSection = document.querySelector('.stats-section');
        if (statsSection) {
            observer.observe(statsSection);
        }
    }
});

/**
 * Toast Notification Utility
 * Displays non-intrusive toast messages for errors and success notifications
 */
function showToast(message, type = 'info') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.style.position = 'fixed';
        container.style.bottom = '24px';
        container.style.right = '24px';
        container.style.zIndex = '9999';
        container.style.display = 'flex';
        container.style.flexDirection = 'column';
        container.style.gap = '10px';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast-message toast-${type}`;
    toast.style.padding = '12px 20px';
    toast.style.borderRadius = '8px';
    toast.style.color = '#fff';
    toast.style.fontSize = '0.9rem';
    toast.style.fontWeight = '500';
    toast.style.boxShadow = '0 10px 15px -3px rgba(0,0,0,0.1)';
    toast.style.transition = 'all 0.3s ease';

    if (type === 'success') toast.style.backgroundColor = '#10b981';
    else if (type === 'error') toast.style.backgroundColor = '#ef4444';
    else if (type === 'warning') toast.style.backgroundColor = '#f59e0b';
    else toast.style.backgroundColor = '#4f46e5';

    toast.textContent = message;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(10px)';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}
