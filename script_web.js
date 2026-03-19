document.addEventListener('DOMContentLoaded', () => {
    const dropdownBtn = document.getElementById('dropdownBtn');
    const dropdownList = document.getElementById('dropdownList');

    dropdownBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        dropdownList.classList.toggle('show');
    });

    window.addEventListener('click', () => {
        dropdownList.classList.remove('show');
    });

    // Simple smooth scroll
    window.scrollToDownload = () => {
        document.getElementById('download').scrollIntoView({ behavior: 'smooth' });
    };
});
