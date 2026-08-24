// --- SCRIPT PADRÃO DA EQUIPE PARA O MENU ---
document.querySelectorAll('.btn-menu').forEach(function(btn) {
    btn.addEventListener('click', function() { 
        window.location.href = this.getAttribute('value'); 
    });
});

var mobileBtn = document.getElementById('mobile-menu-btn');
if (mobileBtn) {
    mobileBtn.addEventListener('click', function() {
        var sidebar = document.querySelector('.sidebar');
        sidebar.classList.toggle('active');
        var icon = this.querySelector('i');
        icon.classList.toggle('fa-bars');
        icon.classList.toggle('fa-xmark');
    });
}

var logoutBtn = document.getElementById('LogOutBtn');
if (logoutBtn) { 
    logoutBtn.addEventListener('click', function() { 
        sessionStorage.clear(); // Limpa os dados do usuário logado
        window.location.href = '/login'; 
    }); 
}
// --- FIM DO SCRIPT DA EQUIPE ---

// --- SEU SCRIPT DE LÓGICA DA PÁGINA ---

// Usuário logado
const hashSaveUser = md5('currentUser');
const userJSON = sessionStorage.getItem(hashSaveUser);
if (userJSON) {
    const user = JSON.parse(userJSON);
    const nameEl = document.querySelector('.user-info .name');
    const roleEl = document.querySelector('.user-info .role');
    if (nameEl) nameEl.textContent = user.nome;
    if (roleEl) roleEl.textContent = user.perfil || "Investidor";
}

// Pesquisa nos cards
const inputSearch = document.getElementById('input-search');
if (inputSearch) {
    inputSearch.addEventListener('input', function() {
        const termo = this.value.toLowerCase();
        const cards = document.querySelectorAll('.tutorial-card');
        cards.forEach(card => {
            const titulo = card.querySelector('.card-titulo').textContent.toLowerCase();
            const categoria = card.querySelector('.card-categoria').textContent.toLowerCase();
            card.style.display = (titulo.includes(termo) || categoria.includes(termo)) ? 'block' : 'none';
        });
    });
}