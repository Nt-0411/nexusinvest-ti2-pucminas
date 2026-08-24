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
        sessionStorage.clear(); // Limpa os dados
        window.location.href = '/login'; 
    }); 
}