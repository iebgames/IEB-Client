document.addEventListener('DOMContentLoaded', () => {
    // Dropdown and Download Logic
    const select = document.getElementById('versionSelector');
    const trigger = select.querySelector('.select-trigger');
    const options = select.querySelectorAll('.option');
    const downloadBtn = document.getElementById('finalDownload');
    let selectedUrl = null;

    // Toggle Dropdown
    trigger.addEventListener('click', () => {
        select.classList.toggle('open');
    });

    // Option Selection
    options.forEach(option => {
        option.addEventListener('click', () => {
            const text = option.innerText;
            selectedUrl = option.getAttribute('data-url');

            trigger.querySelector('span').innerText = text;
            select.classList.remove('open');

            // Enable download button
            downloadBtn.disabled = false;
            downloadBtn.classList.add('ready');
            downloadBtn.innerText = "DOWNLOAD: " + text.split(' - ')[0];
        });
    });

    // Download Action
    downloadBtn.addEventListener('click', () => {
        if (selectedUrl) {
            window.open(selectedUrl, '_blank');
        }
    });

    // Close on click outside
    window.addEventListener('click', (e) => {
        if (!select.contains(e.target)) {
            select.classList.remove('open');
        }
    });

    // --- Particle Animation (Fire Sparks) ---
    const canvas = document.getElementById('particleCanvas');
    const ctx = canvas.getContext('2d');
    let particles = [];

    function resize() {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
    }
    window.addEventListener('resize', resize);
    resize();

    class Particle {
        constructor() {
            this.init();
        }
        init() {
            this.x = Math.random() * canvas.width;
            this.y = canvas.height + Math.random() * 100;
            this.size = Math.random() * 3 + 1;
            this.speedY = Math.random() * 3 + 1.5; // Slightly faster to reach higher
            this.speedX = (Math.random() - 0.5) * 1.5;
            this.alpha = 1;
            this.decay = Math.random() * 0.005 + 0.002; // Slower decay to go higher
        }
        update() {
            this.y -= this.speedY;
            this.x += this.speedX;
            this.alpha -= this.decay;
            if (this.alpha <= 0) this.init();
        }
        draw() {
            ctx.shadowBlur = 10;
            ctx.shadowColor = 'rgba(255, 100, 0, 0.8)';
            ctx.fillStyle = `rgba(255, ${Math.floor(Math.random() * 155 + 100)}, 50, ${this.alpha})`;
            ctx.beginPath();
            ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
            ctx.fill();
            ctx.shadowBlur = 0; // Reset for performance
        }
    }

    for (let i = 0; i < 150; i++) {
        particles.push(new Particle());
    }

    function animate() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        particles.forEach(p => {
            p.update();
            p.draw();
        });
        requestAnimationFrame(animate);
    }
    animate();

    // Fetch real download count from GitHub API
    fetch('https://api.github.com/repos/iebgames/IEB-Client/releases')
        .then(response => response.json())
        .then(data => {
            let totalDownloads = 0;
            if (Array.isArray(data)) {
                data.forEach(release => {
                    if (release.assets) {
                        release.assets.forEach(asset => {
                            totalDownloads += asset.download_count;
                        });
                    }
                });
                document.getElementById('dl-number').innerText = totalDownloads.toLocaleString();
            }
        })
        .catch(error => {
            console.error('Error fetching download count:', error);
            document.getElementById('dl-number').innerText = "Many"; // Fallback
        });
});
