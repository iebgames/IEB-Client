document.addEventListener('DOMContentLoaded', () => {
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
            downloadBtn.innerText = "İNDİR: " + text.split(' - ')[0];
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
});
