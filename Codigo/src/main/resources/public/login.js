const hashSaveUser = md5('currentUser');

function toggleScreens() {
    const loginContainer = document.getElementById('login-container');
    const registerContainer = document.getElementById('register-container');
    if (loginContainer && registerContainer) {
        loginContainer.classList.toggle('hidden');
        registerContainer.classList.toggle('hidden');
    }
}

function previewImagem(event) {
    const input = event.target;
    const preview = document.getElementById('foto-preview');
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
        }
        reader.readAsDataURL(input.files[0]);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    
    function mostrarAlerta(elemento, mensagem, tipo) {
        if (!elemento) return;
        elemento.textContent = mensagem;
        elemento.style.display = 'block';
        elemento.style.padding = '10px';
        elemento.style.marginBottom = '15px';
        elemento.style.borderRadius = '5px';
        
        if (tipo === 'erro') {
            elemento.style.backgroundColor = 'rgba(239, 68, 68, 0.2)';
            elemento.style.color = '#ef4444';
            elemento.style.border = '1px solid #ef4444';
        } else {
            elemento.style.backgroundColor = 'rgba(16, 185, 129, 0.2)';
            elemento.style.color = '#10b981';
            elemento.style.border = '1px solid #10b981';
        }
    }

    const loginForm = document.getElementById('login-form');
    const alertaLogin = document.getElementById('alerta-login');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const login = document.getElementById('username').value;
            const senha = document.getElementById('password').value;

            if (!login || !senha) {
                mostrarAlerta(alertaLogin, 'Por favor, preencha usuário e senha.', 'erro');
                return;
            }

            try {
                const response = await fetch('/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: new URLSearchParams({ login: login, senha: senha })
                });

                const data = await response.json();

                if (data.sucesso) {
                    sessionStorage.setItem(hashSaveUser, JSON.stringify(data.usuario));

                        window.location.href = `/dashboard`;
        
                } else {
                    mostrarAlerta(alertaLogin, data.mensagem || 'Usuário ou senha inválidos.', 'erro');
                }
            } catch (error) {
                console.error("Erro no login:", error);
                mostrarAlerta(alertaLogin, 'Erro ao conectar com o servidor.', 'erro');
            }
        });
    }

    const btnSalvar = document.getElementById('btn_salvar');
    const alertaCadastro = document.getElementById('alerta-cadastro');

    if (btnSalvar) {
        btnSalvar.addEventListener('click', async () => {
            const login = document.getElementById('txt_login').value;
            const nome = document.getElementById('txt_nome').value;
            const email = document.getElementById('txt_email').value;
            const senha = document.getElementById('txt_senha').value;
            const senha2 = document.getElementById('txt_senha2').value;
            
            const fotoPreview = document.getElementById('foto-preview').src;
            let fotoInput = "";
            if (!fotoPreview.includes('149071.png')) { 
                fotoInput = fotoPreview;
            }

            if (!login || !nome || !email || !senha) {
                mostrarAlerta(alertaCadastro, 'Preencha todos os campos obrigatórios!', 'erro');
                return;
            }

            if (senha !== senha2) {
                mostrarAlerta(alertaCadastro, 'As senhas não coincidem!', 'erro');
                return;
            }

            try {
                const response = await fetch('/usuario/cadastrar', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ 
                        login: login, 
                        nome: nome, 
                        email: email, 
                        senha: senha,
                        foto: fotoInput
                    })
                });

                const data = await response.json();

                if (data.sucesso) {
                    mostrarAlerta(alertaCadastro, 'Cadastro realizado! Autenticando...', 'sucesso');

                    const authRes = await fetch('/login', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                        body: new URLSearchParams({ login: login, senha: senha })
                    });

                    const authData = await authRes.json();

                    if (authData.sucesso) {
                        sessionStorage.setItem(hashSaveUser, JSON.stringify(authData.usuario));
                        
                        setTimeout(() => {
                            window.location.href = 'quiz.html'; 
                        }, 1000);
                    } else {
                        mostrarAlerta(alertaCadastro, 'Cadastro concluído, mas houve erro no login automático.', 'erro');
                        setTimeout(() => toggleScreens(), 2000);
                    }
                } else {
                    mostrarAlerta(alertaCadastro, data.mensagem, 'erro');
                }
            } catch (error) {
                console.error("Erro no cadastro:", error);
                mostrarAlerta(alertaCadastro, 'Erro ao comunicar com o servidor.', 'erro');
            }
        });
    }
});