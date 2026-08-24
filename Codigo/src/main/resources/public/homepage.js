document.addEventListener('DOMContentLoaded', () => {
    
    // ==========================================
    // 1. LÓGICA DA TRAVA DE LOGIN NOS CARDS
    // ==========================================
    const cards = document.querySelectorAll('.card-servico');

    cards.forEach(card => {
        card.addEventListener('click', () => {
            
            const linkDestino = card.getAttribute('data-link');
            const usuarioLogado = sessionStorage.getItem('usuario'); 

            if (usuarioLogado) {
                // Tem usuário na memória: acesso liberado para a rota (ex: /simulation)
                window.location.href = linkDestino;
            } else {
                // NÃO tem usuário: salva o destino e joga para a tela de login
                localStorage.setItem('redirecionarAposLogin', linkDestino);
                window.location.href = '/login'; 
            }
        });
    });

    // ==========================================
    // 2. LÓGICA DO CARROSSEL DE FERRAMENTAS
    // ==========================================
    const container = document.getElementById('service-cards-container');
    const leftArrow = document.querySelector('.left-arrow');
    const rightArrow = document.querySelector('.right-arrow');

    if (container && leftArrow && rightArrow) {
        const scrollAmount = 320; 

        leftArrow.addEventListener('click', () => {
            container.scrollBy({
                top: 0,
                left: -scrollAmount,
                behavior: 'smooth'
            });
        });

        rightArrow.addEventListener('click', () => {
            container.scrollBy({
                top: 0,
                left: scrollAmount,
                behavior: 'smooth'
            });
        });
    }
});