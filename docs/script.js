document.addEventListener('DOMContentLoaded', () => {
    const GITHUB_REPO = 'iebgames/IEB-Client';

    // Cookie banner (GDPR compliance)
    const cookieBanner = document.getElementById('cookieBanner');
    const cookieAccept = document.getElementById('cookieAccept');
    
    // Check if consent already given (global or local)
    const consent = localStorage.getItem('ieb_cookie_consent_docs') || localStorage.getItem('ieb-cookie-consent');
    
    if (consent === 'accepted') {
        if (cookieBanner) cookieBanner.classList.add('hidden');
        loadAdSense();
    } else if (consent === 'rejected') {
        if (cookieBanner) cookieBanner.classList.add('hidden');
    } else {
        if (cookieBanner) cookieBanner.classList.remove('hidden');
    }

    if (cookieAccept) {
        cookieAccept.addEventListener('click', () => {
            localStorage.setItem('ieb_cookie_consent_docs', 'accepted');
            if (cookieBanner) cookieBanner.classList.add('hidden');
            loadAdSense();
        });
    }

    function loadAdSense() {
        // Trigger all ins.adsbygoogle elements to load
        document.querySelectorAll('.adsbygoogle:not([data-ads-loaded])').forEach(el => {
            try {
                (window.adsbygoogle = window.adsbygoogle || []).push({});
                el.setAttribute('data-ads-loaded', '1');
            } catch (e) {}
        });
    }

    // GitHub download count
    function loadGitHubDownloads() {
        fetch(`https://api.github.com/repos/${GITHUB_REPO}/releases`)
            .then(r => r.ok ? r.json() : [])
            .then(releases => {
                let total = 0;
                if (Array.isArray(releases)) {
                    releases.forEach(rel => {
                        if (rel.assets) rel.assets.forEach(a => { total += a.download_count || 0; });
                    });
                }
                const formatted = total.toLocaleString('en-US');
                const el = document.getElementById('dl-number');
                const footerEl = document.getElementById('footer-dl-number');
                if (el) el.textContent = formatted;
                if (footerEl) footerEl.textContent = formatted;
            })
            .catch(() => {
                const el = document.getElementById('dl-number');
                if (el) el.textContent = 'N/A';
            });
    }
    loadGitHubDownloads();

    // Version selector
    const select = document.getElementById('versionSelector');
    if (!select) return;

    const trigger = select.querySelector('.select-trigger');
    const options = select.querySelectorAll('.option');
    const downloadBtn = document.getElementById('finalDownload');
    let selectedUrl = null;
    let selectedLabel = '';

    trigger.addEventListener('click', () => select.classList.toggle('open'));

    options.forEach(option => {
        option.addEventListener('click', () => {
            selectedLabel = option.innerText;
            selectedUrl = option.getAttribute('data-url');
            trigger.querySelector('span').innerText = selectedLabel;
            select.classList.remove('open');
            downloadBtn.disabled = false;
            downloadBtn.classList.add('ready');
            downloadBtn.innerText = 'DOWNLOAD: ' + selectedLabel.split(' - ')[0];
        });
    });

    window.addEventListener('click', e => {
        if (!select.contains(e.target)) select.classList.remove('open');
    });

    // Redirect to the dedicated download page
    downloadBtn.addEventListener('click', () => {
        if (!selectedUrl) return;
        const fileUrl = encodeURIComponent(selectedUrl);
        const fileName = encodeURIComponent(selectedLabel);
        window.location.href = `download.html?url=${fileUrl}&name=${fileName}`;
    });

    // Particles (Arka Plan Efekti)
    const canvas = document.getElementById('particleCanvas');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    let particles = [];

    function resize() {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
    }
    window.addEventListener('resize', resize);
    resize();

    class Particle {
        constructor() { this.init(); }
        init() {
            this.x = Math.random() * canvas.width;
            this.y = canvas.height + Math.random() * 100;
            this.size = Math.random() * 3 + 1;
            this.speedY = Math.random() * 3 + 1.5;
            this.speedX = (Math.random() - 0.5) * 1.5;
            this.alpha = 1;
            this.decay = Math.random() * 0.005 + 0.002;
        }
        update() {
            this.y -= this.speedY;
            this.x += this.speedX;
            this.alpha -= this.decay;
            if (this.alpha <= 0) this.init();
        }
        draw() {
            ctx.fillStyle = `rgba(255, ${Math.floor(Math.random() * 155 + 100)}, 50, ${this.alpha})`;
            ctx.beginPath();
            ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
            ctx.fill();
        }
    }

    for (let i = 0; i < 150; i++) particles.push(new Particle());

    function animate() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        particles.forEach(p => { p.update(); p.draw(); });
        requestAnimationFrame(animate);
    }
    animate();
});