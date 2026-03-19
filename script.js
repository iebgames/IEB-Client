/* ===================== IEB CLIENT - script.js ===================== */

// ─── State ──────────────────────────────────────────────────────────
const state = {
  menuOpen: false,
  enabledModules: new Set(),
  drag: { active: false, startX: 0, startY: 0, origX: 0, origY: 0 }
};

// ─── DOM refs ────────────────────────────────────────────────────────
const menu      = document.getElementById('menu');
const closeBtn  = document.getElementById('closeBtn');
const catBtns   = document.querySelectorAll('.cat-btn');
const catPanels = document.querySelectorAll('.cat-panel');
const modules   = document.querySelectorAll('.module');
const activeHUD = document.getElementById('activeHUD');
const toast     = document.getElementById('toast');
const clock     = document.getElementById('clock');
const header    = document.getElementById('menuHeader');
const tabHint   = document.getElementById('tabHint');

// ─── Tab key toggle ──────────────────────────────────────────────────
document.addEventListener('keydown', (e) => {
  if (e.key === 'Tab') {
    e.preventDefault();
    toggleMenu();
  }
});

function toggleMenu() {
  state.menuOpen = !state.menuOpen;
  if (state.menuOpen) {
    menu.classList.remove('hidden');
    document.body.classList.add('menu-open');
    tabHint.style.display = 'none';
  } else {
    menu.classList.add('hidden');
    document.body.classList.remove('menu-open');
  }
}

closeBtn.addEventListener('click', () => toggleMenu());

// ─── Category tabs ───────────────────────────────────────────────────
catBtns.forEach(btn => {
  btn.addEventListener('click', () => {
    catBtns.forEach(b => b.classList.remove('active'));
    catPanels.forEach(p => p.classList.remove('active'));
    btn.classList.add('active');
    const target = document.getElementById('cat-' + btn.dataset.cat);
    if (target) target.classList.add('active');
  });
});

// ─── Module toggle (click header) ────────────────────────────────────
modules.forEach(mod => {
  const header = mod.querySelector('.mod-header');
  const checkbox = mod.querySelector('input[type="checkbox"]');
  const name = mod.dataset.mod;

  header.addEventListener('click', (e) => {
    if (e.target.closest('.toggle')) return; // let toggle handle it
    checkbox.checked = !checkbox.checked;
    handleModuleToggle(mod, name, checkbox.checked);
  });

  checkbox.addEventListener('change', () => {
    handleModuleToggle(mod, name, checkbox.checked);
  });
});

function handleModuleToggle(mod, name, enabled) {
  if (enabled) {
    mod.classList.add('enabled');
    state.enabledModules.add(name);
    showToast(`✅ ${name} aktifleştirildi`);
  } else {
    mod.classList.remove('enabled');
    state.enabledModules.delete(name);
    showToast(`❌ ${name} devre dışı`);
  }
  updateHUD();
}

// ─── Range slider live values & Middle Click Edit ─────────────────────
document.querySelectorAll('.setting-row input[type="range"]').forEach(slider => {
  const valSpan = slider.nextElementSibling;
  
  function updateValueDisplay() {
    if (valSpan && valSpan.classList.contains('val')) {
      valSpan.textContent = parseFloat(slider.value).toFixed(
        slider.step && slider.step.includes('.') ? slider.step.split('.')[1].length : 0
      );
    }
  }

  if (valSpan && valSpan.classList.contains('val')) {
    slider.addEventListener('input', updateValueDisplay);
  }

  // Middle-click to set custom number
  const row = slider.closest('.setting-row');
  if (row) {
    row.addEventListener('mousedown', (e) => {
      if (e.button === 1) { // Middle click = 1
        e.preventDefault();
        let newVal = prompt(`Yeni değeri girin (Mevcut: ${slider.value}):`, slider.value);
        if (newVal !== null && newVal.trim() !== "" && !isNaN(newVal)) {
          let parsedVal = parseFloat(newVal);
          
          // Dynamically adjust min/max if the typed value exceeds them
          if (parsedVal > parseFloat(slider.max)) slider.max = parsedVal;
          if (parsedVal < parseFloat(slider.min)) slider.min = parsedVal;
          
          slider.value = parsedVal;
          updateValueDisplay();
          
          // Dispatch events for external listeners
          slider.dispatchEvent(new Event('input'));
          slider.dispatchEvent(new Event('change'));
        }
      }
    });
  }
});

// ─── Active Modules HUD ──────────────────────────────────────────────
function updateHUD() {
  activeHUD.innerHTML = '';
  state.enabledModules.forEach(name => {
    const el = document.createElement('div');
    el.className = 'hud-module';
    el.textContent = name;
    activeHUD.appendChild(el);
  });
}

// ─── Toast notification ──────────────────────────────────────────────
let toastTimeout;
function showToast(msg) {
  toast.textContent = msg;
  toast.classList.add('show');
  clearTimeout(toastTimeout);
  toastTimeout = setTimeout(() => toast.classList.remove('show'), 1800);
}

// ─── Clock ───────────────────────────────────────────────────────────
function updateClock() {
  const now = new Date();
  clock.textContent = now.toLocaleTimeString('tr-TR');
}
updateClock();
setInterval(updateClock, 1000);

// ─── Draggable menu ──────────────────────────────────────────────────
const menuEl = document.getElementById('menu');
const menuHeader = document.getElementById('menuHeader');

menuHeader.addEventListener('mousedown', (e) => {
  if (e.target.closest('#closeBtn')) return;
  state.drag.active = true;
  const rect = menuEl.getBoundingClientRect();
  state.drag.startX = e.clientX;
  state.drag.startY = e.clientY;
  state.drag.origX = rect.left;
  state.drag.origY = rect.top;
  menuEl.style.transition = 'none';
  e.preventDefault();
});

document.addEventListener('mousemove', (e) => {
  if (!state.drag.active) return;
  const dx = e.clientX - state.drag.startX;
  const dy = e.clientY - state.drag.startY;
  let newX = state.drag.origX + dx;
  let newY = state.drag.origY + dy;

  // Clamp to viewport
  const w = menuEl.offsetWidth;
  const h = menuEl.offsetHeight;
  newX = Math.max(0, Math.min(window.innerWidth - w, newX));
  newY = Math.max(0, Math.min(window.innerHeight - h, newY));

  menuEl.style.left = newX + 'px';
  menuEl.style.top  = newY + 'px';
  menuEl.style.transform = 'none';
});

document.addEventListener('mouseup', () => {
  if (state.drag.active) {
    state.drag.active = false;
    menuEl.style.transition = '';
  }
});

// ─── Background Canvas (Particle Grid) ───────────────────────────────
const canvas  = document.getElementById('bgCanvas');
const ctx     = canvas.getContext('2d');
let particles = [];
let animId;
const PARTICLE_COUNT = 60;

function resize() {
  canvas.width  = window.innerWidth;
  canvas.height = window.innerHeight;
}
resize();
window.addEventListener('resize', resize);

class Particle {
  constructor() { this.reset(); }
  reset() {
    this.x  = Math.random() * canvas.width;
    this.y  = Math.random() * canvas.height;
    this.vx = (Math.random() - 0.5) * 0.4;
    this.vy = (Math.random() - 0.5) * 0.4;
    this.r  = Math.random() * 2 + 0.5;
    this.alpha = Math.random() * 0.5 + 0.1;
    this.color = Math.random() > 0.5 ? '124,58,255' : '0,229,255';
  }
  update() {
    this.x += this.vx;
    this.y += this.vy;
    if (this.x < 0 || this.x > canvas.width || this.y < 0 || this.y > canvas.height) this.reset();
  }
  draw() {
    ctx.beginPath();
    ctx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
    ctx.fillStyle = `rgba(${this.color},${this.alpha})`;
    ctx.fill();
  }
}

for (let i = 0; i < PARTICLE_COUNT; i++) particles.push(new Particle());

function drawConnections() {
  for (let i = 0; i < particles.length; i++) {
    for (let j = i + 1; j < particles.length; j++) {
      const dx = particles[i].x - particles[j].x;
      const dy = particles[i].y - particles[j].y;
      const dist = Math.sqrt(dx*dx + dy*dy);
      if (dist < 120) {
        ctx.beginPath();
        ctx.moveTo(particles[i].x, particles[i].y);
        ctx.lineTo(particles[j].x, particles[j].y);
        ctx.strokeStyle = `rgba(124,58,255,${0.12 * (1 - dist/120)})`;
        ctx.lineWidth = 0.5;
        ctx.stroke();
      }
    }
  }
}

function animate() {
  animId = requestAnimationFrame(animate);
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  // Draw gradient bg
  const grad = ctx.createRadialGradient(
    canvas.width/2, canvas.height/2, 0,
    canvas.width/2, canvas.height/2, Math.max(canvas.width, canvas.height)/1.5
  );
  grad.addColorStop(0, 'rgba(20,10,40,1)');
  grad.addColorStop(1, 'rgba(5,5,15,1)');
  ctx.fillStyle = grad;
  ctx.fillRect(0, 0, canvas.width, canvas.height);

  // Subtle grid lines
  ctx.strokeStyle = 'rgba(124,58,255,0.04)';
  ctx.lineWidth = 1;
  const gridSize = 60;
  for (let x = 0; x < canvas.width; x += gridSize) {
    ctx.beginPath(); ctx.moveTo(x, 0); ctx.lineTo(x, canvas.height); ctx.stroke();
  }
  for (let y = 0; y < canvas.height; y += gridSize) {
    ctx.beginPath(); ctx.moveTo(0, y); ctx.lineTo(canvas.width, y); ctx.stroke();
  }

  drawConnections();
  particles.forEach(p => { p.update(); p.draw(); });

  // Vignette
  const vig = ctx.createRadialGradient(
    canvas.width/2, canvas.height/2, canvas.height*0.3,
    canvas.width/2, canvas.height/2, canvas.height
  );
  vig.addColorStop(0, 'rgba(0,0,0,0)');
  vig.addColorStop(1, 'rgba(0,0,0,0.7)');
  ctx.fillStyle = vig;
  ctx.fillRect(0, 0, canvas.width, canvas.height);
}

animate();

// ─── Hint text animation ─────────────────────────────────────────────
// Show hint briefly on load
setTimeout(() => {
  tabHint.style.opacity = '0';
  tabHint.style.transition = 'opacity 1s';
  setTimeout(() => {
    tabHint.style.display = 'none';
    tabHint.style.transition = '';
    tabHint.style.opacity = '1';
  }, 1000);
}, 4000);

// Show hint again if menu is closed
document.addEventListener('keydown', (e) => {
  if (e.key === 'Tab' && !state.menuOpen) {
    tabHint.style.display = 'none'; // hide once opened
  }
});

// ─── Console credits ─────────────────────────────────────────────────
console.log('%c⚔ IEB Client v2.0', 'color:#7c3aff;font-size:20px;font-weight:bold;font-family:monospace');
console.log('%cby IEB Games | Tab tuşuna bas → menü aç', 'color:#00e5ff;font-size:12px');
