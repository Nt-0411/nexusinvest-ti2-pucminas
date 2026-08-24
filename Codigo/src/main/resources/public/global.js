// Impede cache ao usar botão voltar do navegador
window.addEventListener('pageshow', function(event) {
    if (event.persisted) {
        const hashSaveUser = md5('currentUser');
        const usuarioStr = sessionStorage.getItem(hashSaveUser);
        if (!usuarioStr) {
            window.location.replace('http://localhost:6789/login');
            return;
        }
    }
});

document.addEventListener('DOMContentLoaded', function () {

    // Páginas que NÃO precisam de login (adicionado a Home '/' por segurança)
    const paginasPublicas = ['/login', '/usuario_form.html', '/', '/index.html'];
    const paginaAtual = window.location.pathname;
    
    // Verifica se a página atual exata é a home ou se contém as outras
    const ehPublica = paginasPublicas.some(p => paginaAtual === p || paginaAtual.includes('/login') || paginaAtual.includes('/usuario_form.html'));

    const hashSaveUser = md5('currentUser');
    const usuarioStr = sessionStorage.getItem(hashSaveUser);

    // Se não estiver logado e não for página pública, redireciona
    if (!usuarioStr && !ehPublica) {
        window.location.replace('http://localhost:6789/login');
        return;
    }

    if (!usuarioStr) return;

    const usuario = JSON.parse(usuarioStr);

    // Atualiza nome
    const nomeEl = document.querySelector('.user-info .name');
    if (nomeEl) nomeEl.textContent = usuario.nome || usuario.login;

    // Atualiza perfil mapeando o ID (que vem como idPerfil do Java)
    const roleEl = document.querySelector('.user-info .role');
    const nomesPerfis = { 1: "Conservador", 2: "Moderado", 3: "Arrojado" };
    if (roleEl) roleEl.textContent = nomesPerfis[usuario.idPerfil] || 'Investidor';

    // Insere foto dinamicamente
    const userProfile = document.querySelector('.user-profile');
    if (userProfile && usuario.foto) {
        if (!document.getElementById('header-foto-usuario')) {
            const img = document.createElement('img');
            img.id = 'header-foto-usuario';
            img.src = usuario.foto;
            img.alt = 'Foto do usuário';
            img.style.cssText = 'width:38px; height:38px; border-radius:50%; object-fit:cover; margin-right:10px; border: 2px solid #3b82f6;';
            userProfile.insertBefore(img, userProfile.firstChild);
        }
    }

    // Botão sair - ATUALIZADO PARA REDIRECIONAR PARA A HOME
    const logoutBtn = document.getElementById('LogOutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function () {
            // Remove a sessão do usuário
            sessionStorage.removeItem(hashSaveUser);
            
            // Remove a trava de segurança dos cards da Home, se houver
            localStorage.removeItem('redirecionarAposLogin');
            
            // Redireciona direto para a página inicial (Home Page)
            window.location.replace('/');
        });
    }
});