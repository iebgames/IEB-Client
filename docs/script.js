document.addEventListener('DOMContentLoaded', () => {
    const ADS_KEY = 'ieb_adswitch';
    const GITHUB_REPO = 'iebgames/IEB-Client';

    const adswitch = document.getElementById('adswitch');
    const adSlots = document.querySelectorAll('.ad-slot');
    const modal = document.getElementById('adDisableModal');
    const keepAdsBtn = document.getElementById('keepAdsBtn');
    const disableAdsBtn = document.getElementById('disableAdsBtn');
    const downloadOverlay = document.getElementById('downloadOverlay');
    const countdownNum = document.getElementById('countdownNum');
    const countdownFile = document.getElementById('countdownFile');
    const skipCountdown = document.getElementById('skipCountdown');

    let adsEnabled = localStorage.getItem(ADS_KEY) !== '0';
    if (adswitch) adswitch.checked = adsEnabled;

    function pushAdsIn(container) {
        if (!adsEnabled || !window.adsbygoogle) return;
        container.querySelectorAll('.adsbygoogle:not([data-ads-loaded])').forEach(el => {
            try {
                (adsbygoogle = window.adsbygoogle || []).push({});
                el.setAttribute('data-ads-loaded', '1');
            } catch (e) {}
        });
    }

    function applyAds(enabled) {
        adsEnabled = enabled;
        adSlots.forEach(slot => slot.classList.toggle('hidden-ads', !enabled));
        if (adswitch) adswitch.checked = enabled;
        localStorage.setItem(ADS_KEY, enabled ? '1' : '0');
        if (enabled) pushAdsIn(document.body);
    }

    applyAds(adsEnabled);

    if (adswitch) {
        adswitch.addEventListener('change', () => {
            if (adswitch.checked) applyAds(true);
            else {
                adswitch.checked = true;
                if (modal) modal.classList.remove('hidden');
            }
        });
    }

    if (keepAdsBtn) keepAdsBtn.addEventListener('click', () => {
        if (modal) modal.classList.add('hidden');
        applyAds(true);
    });

    if (disableAdsBtn) disableAdsBtn.addEventListener('click', () => {
        if (modal) modal.classList.add('hidden');
        applyAds(false);
    });

    // Cookie banner
    const cookieBanner = document.getElementById('cookieBanner');
    const cookieAccept = document.getElementById('cookieAccept');
    if (localStorage.getItem('ieb_cookie_ok') === '1' && cookieBanner) cookieBanner.classList.add('hidden');
    if (cookieAccept) cookieAccept.addEventListener('click', () => {
        localStorage.setItem('ieb_cookie_ok', '1');
        if (cookieBanner) cookieBanner.classList.add('hidden');
    });

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

    let countdownTimer = null;

    // Tarayıcı engellerini aşan güvenli indirme fonksiyonu (GÜNCELLENDİ)
    function startDownload(url, label) {
        if (downloadOverlay) downloadOverlay.classList.add('hidden');
        
        // Görünmez bir link oluşturup simüle ederek pop-up engelleyicileri bypass ediyoruz
        const downloadLink = document.createElement('a');
        downloadLink.href = url;
        downloadLink.setAttribute('download', '');
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);

        loadGitHubDownloads();
    }

    // İndirme overlay katmanı ve geri sayım algoritması
    function showDownloadOverlay(url, label) {
        // Eğer kullanıcı reklamları kapattıysa hiç bekletme, anında indir!
        if (!adsEnabled) {
            startDownload(url, label);
            return;
        }

        if (!downloadOverlay) {
            startDownload(url, label);
            return;
        }

        countdownFile.textContent = label;
        let seconds = 5;
        countdownNum.textContent = seconds;
        downloadOverlay.classList.remove('hidden');

        // Reklam slotlarını adswitch durumuna göre göster/gizle
        const overlayAdSlots = downloadOverlay.querySelectorAll('.ad-slot');
        overlayAdSlots.forEach(s => s.classList.toggle('hidden-ads', !adsEnabled));

        if (adsEnabled) pushAdsIn(downloadOverlay);

        if (countdownTimer) clearInterval(countdownTimer);

        // 5 saniyelik geri sayımı başlat
        countdownTimer = setInterval(() => {
            seconds--;
            countdownNum.textContent = seconds;
            
            // Süre bittiğinde otomatik indirme artık sorunsuz tetiklenecek
            if (seconds <= 0) {
                clearInterval(countdownTimer);
                startDownload(url, label);
            }
        }, 1000);
    }

    if (skipCountdown) {
        skipCountdown.addEventListener('click', () => {
            if (countdownTimer) clearInterval(countdownTimer); // Manuel basıldıysa sayacı durdur
            if (selectedUrl) startDownload(selectedUrl, selectedLabel);
        });
    }

    downloadBtn.addEventListener('click', () => {
        if (!selectedUrl) return;
        showDownloadOverlay(selectedUrl, selectedLabel);
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