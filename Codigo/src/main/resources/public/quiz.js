document.addEventListener('DOMContentLoaded', () => {
    
    // Token entregue pela rota /config.js, fora do codigo-fonte
    const brapiApiKey = (window.NEXUS_CONFIG && window.NEXUS_CONFIG.brapiToken) || '';
    const API_URL_JAVA = '/usuario'; 

    const hashSaveUser = md5('currentUser');
    let usuarioCorrenteJSON = sessionStorage.getItem(hashSaveUser);
    let usuarioCorrente = usuarioCorrenteJSON ? JSON.parse(usuarioCorrenteJSON) : null;

    if (!usuarioCorrente) {
        alert("Você precisa estar logado para realizar o quiz.");
        window.location.href = '/login'; 
        return;
    }

    const quizData = [
        {
            "id": "1",
            "question": "Qual é o seu principal objetivo ao investir?",
            "options": [
                { "text": "(A) Proteger meu dinheiro, sem correr riscos.", "value": 1 },
                { "text": "(B) Ter uma boa rentabilidade, aceitando pequenos riscos.", "value": 3 },
                { "text": "(C) Obter altos retornos, mesmo que envolva riscos elevados.", "value": 5 }
            ]
        },
        {
            "id": "2",
            "question": "Por quanto tempo você pretende deixar o dinheiro investido?",
            "options": [
                { "text": "(A) Menos de 1 ano.", "value": 1 },
                { "text": "(B) Entre 1 e 5 anos.", "value": 3 },
                { "text": "(C) Mais de 5 anos.", "value": 5 }
            ]
        },
        {
            "id": "3",
            "question": "Como você reagiria se seus investimentos sofressem uma queda de 10% em um mês?",
            "options": [
                { "text": "(A) Venderia tudo imediatamente para evitar mais perdas.", "value": 1 },
                { "text": "(B) Manteria o investimento e esperaria se recuperar.", "value": 3 },
                { "text": "(C) Aproveitaria para comprar mais, vendo como uma oportunidade.", "value": 5 }
            ]
        },
        {
            "id": "4",
            "question": "Qual dessas afirmações mais se aproxima da sua relação com o risco?",
            "options": [
                { "text": "(A) Prefiro segurança, mesmo que o retorno seja baixo.", "value": 1 },
                { "text": "(B) Aceito algum risco se houver chance de retorno maior.", "value": 3 },
                { "text": "(C) Busco o maior retorno possível, mesmo que isso signifique grandes riscos.", "value": 5 }
            ]
        },
        {
            "id": "5",
            "question": "Qual é a sua experiência com investimentos?",
            "options": [
                { "text": "(A) Sou iniciante e ainda estou aprendendo.", "value": 1 },
                { "text": "(B) Já fiz alguns investimentos, mas de forma conservadora.", "value": 3 },
                { "text": "(C) Invisto com frequência e entendo bem o mercado.", "value": 5 }
            ]
        },
        {
            "id": "6",
            "question": "Qual porcentagem do seu patrimônio você estaria disposto a investir em produtos de alto risco?",
            "options": [
                { "text": "(A) 0 a 10%.", "value": 1 },
                { "text": "(B) 10 a 30%.", "value": 3 },
                { "text": "(C) Acima de 30%.", "value": 5 }
            ]
        },
        {
            "id": "7",
            "question": "Qual dessas situações mais se encaixa na sua atual condição financeira?",
            "options": [
                { "text": "(A) Tenho dívidas ou reservas pequenas.", "value": 1 },
                { "text": "(B) Tenho alguma reserva e estabilidade financeira.", "value": 3 },
                { "text": "(C) Tenho boa reserva e posso assumir riscos sem comprometer minha segurança.", "value": 5 }
            ]
        },
        {
            "id": "8",
            "question": "Quando você pensa em investir, o que mais pesa na sua decisão?",
            "options": [
                { "text": "(A) Segurança e previsibilidade.", "value": 1 },
                { "text": "(B) Equilíbrio entre risco e retorno.", "value": 3 },
                { "text": "(C) Potencial de ganho elevado.", "value": 5 }
            ]
        },
        {
            "id": "9",
            "question": "Se você recebesse um rendimento inesperado (ex: bônus ou herança), o que faria?",
            "options": [
                { "text": "(A) Colocaria em um investimento seguro ou poupança.", "value": 1 },
                { "text": "(B) Aplicaria parte em renda fixa e parte em renda variável.", "value": 3 },
                { "text": "(C) Investiria a maior parte em ações, cripto ou startups.", "value": 5 }
            ]
        },
        {
            "id": "10",
            "question": "Como você define o seu conhecimento sobre economia e finanças?",
            "options": [
                { "text": "(A) Limitado — entendo o básico.", "value": 1 },
                { "text": "(B) Médio — acompanho notícias e sei analisar algumas opções.", "value": 3 },
                { "text": "(C) Avançado — estudo e entendo o funcionamento do mercado.", "value": 5 }
            ]
        }
    ];

    const recomendacoesData = {
        "Conservador": {
            "titulo": "Foco em Segurança e Liquidez",
            "produtos": [
                "Tesouro Selic (LFT)",
                "CDBs de bancos grandes (com FGC)",
                "Fundos de Renda Fixa DI (Taxa Zero)"
            ]
        },
        "Moderado": {
            "titulo": "Equilíbrio entre Risco e Retorno",
            "produtos": [
                "Fundos Multimercado (Gestão Ativa)",
                "Tesouro IPCA+ (Inflação)",
                "Debêntures Incentivadas (Empresas Sólidas)",
                "Fundos de Investimento Imobiliário (FIIs)"
            ]
        },
        "Arrojado": {
            "titulo": "Busca por Alta Rentabilidade",
            "produtos": [
                "Ações (Bolsa de Valores - Ibovespa)",
                "Fundos de Ações (Small Caps)",
                "BDRs (Ações de empresas internacionais)",
                "ETFs (Fundos de Índice)",
                "Criptomoedas (Baixa exposição)"
            ]
        }
    };

    const quizForm = document.getElementById('quizForm');
    const submitButton = document.getElementById('submitQuiz');
    const resultContainer = document.getElementById('result');
    const resultProfile = document.getElementById('resultProfile');
    const resultDescription = document.getElementById('resultDescription');
    const recommendationsContainer = document.getElementById('resultRecommendations'); 
    const realTimeContainer = document.getElementById('realTimeData');
    const quizContainer = document.querySelector('.quiz-container');
    const continueBtn = document.getElementById('continueBtn');
    
    var profile;

    const mapPerfilId = {
        'Conservador': 1,
        'Moderado': 2,
        'Arrojado': 3
    };

    async function updateUserProfile(profileName) {
        if (!usuarioCorrente.idUsuario) return;

        const idPerfil = mapPerfilId[profileName];

        try {
            const response = await fetch(`${API_URL_JAVA}/${usuarioCorrente.idUsuario}`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id_perfil: idPerfil })
            });

            if (!response.ok) throw new Error('Falha ao salvar perfil no banco PostgreSQL');

            usuarioCorrente.idPerfil = idPerfil;
            sessionStorage.setItem(hashSaveUser, JSON.stringify(usuarioCorrente));
            console.log("Perfil salvo com sucesso no PostgreSQL.");

        } catch (error) {
            console.error("Erro ao salvar perfil do usuário:", error);
        }
    }

    function loadQuiz() {
        resultContainer.style.display = 'none';
        
        if (!quizData || quizData.length === 0) {
            quizContainer.innerHTML = `<p style="color: red; text-align: center;">Nenhuma pergunta encontrada.</p>`;
            submitButton.style.display = 'none';
            return;
        }

        quizData.forEach((questionItem) => {
            const questionElement = document.createElement('div');
            questionElement.classList.add('question-block');
            
            const questionTitle = document.createElement('p');
            questionTitle.classList.add('question-text');
            questionTitle.innerText = `${questionItem.id}. ${questionItem.question}`;
            questionElement.appendChild(questionTitle);
            
            const optionsElement = document.createElement('div');
            optionsElement.classList.add('options-block');
            
            questionItem.options.forEach((option, optionIndex) => {
                const optionId = `q${questionItem.id}-option${optionIndex}`;
                const optionLabel = document.createElement('label');
                optionLabel.setAttribute('for', optionId);
                
                const radioButton = document.createElement('input');
                radioButton.type = 'radio';
                radioButton.name = `question-${questionItem.id}`; 
                radioButton.id = optionId;
                radioButton.value = option.value; 
                radioButton.required = true;
                
                optionLabel.appendChild(radioButton);
                optionLabel.appendChild(document.createTextNode(` ${option.text}`));
                optionsElement.appendChild(optionLabel);
            });
            questionElement.appendChild(optionsElement);
            quizForm.appendChild(questionElement);
        });
    }

    function loadRecommendations(profileName) {
        recommendationsContainer.innerHTML = '<h3>Recomendações de Investimento:</h3>'; 
        
        const data = recomendacoesData[profileName];
        
        if (data) {
            const title = document.createElement('p');
            title.style.fontWeight = 'bold';
            title.innerText = data.titulo;
            recommendationsContainer.appendChild(title);
            
            const productList = document.createElement('ul');
            productList.classList.add('recommendation-list');
            data.produtos.forEach(produto => {
                const li = document.createElement('li');
                li.innerText = produto;
                productList.appendChild(li);
            });
            recommendationsContainer.appendChild(productList);
        } else {
            recommendationsContainer.innerHTML += '<p>Nenhuma recomendação encontrada para este perfil.</p>';
        }
    }

    async function loadRealTimeData(profileName) {
        realTimeContainer.innerHTML = '<h3>Cotações em Tempo Real:</h3>';
        
        let tickers = '';

        switch (profileName) {
            case 'Conservador': tickers = 'BBAS3'; break;
            case 'Moderado': tickers = 'BBDC4'; break;
            case 'Arrojado': tickers = 'BOVA11'; break;
        }

        if (tickers === '') return;

        try {
            const response = await fetch(`https://brapi.dev/api/quote/${tickers}?token=${brapiApiKey}`, {
                method: 'GET'                
            });

            if (!response.ok) {
                throw new Error(`Erro na API brapi.dev: ${response.status} - ${response.statusText}`);
            }

            const data = await response.json();
            
            data.results.forEach(stock => {
                const price = stock.regularMarketPrice;
                const changePercent = stock.regularMarketChangePercent;
                const stockElement = document.createElement('div');
                stockElement.classList.add('stock-quote');
                const changeClass = changePercent >= 0 ? 'positive' : 'negative';
                const changeSign = changePercent >= 0 ? '+' : '';

                stockElement.innerHTML = `
                    <div class="stock-info">
                        ${stock.symbol} 
                        <span style="font-weight: normal; font-size: 0.9rem;">(${stock.shortName})</span>
                    </div>
                    <div class="stock-price">R$ ${price.toFixed(2)}</div>
                    <div class="stock-change ${changeClass}">
                        ${changeSign}${changePercent.toFixed(2)}%
                    </div>
                `;
                realTimeContainer.appendChild(stockElement);
            });

        } catch (error) {
            console.error("Erro ao carregar dados em tempo real:", error);
            realTimeContainer.innerHTML += `<p style="color: red;">Não foi possível carregar as cotações.</p>`;
        }
    }

    async function showResults(event) {
        event.preventDefault();

        if (quizData.length === 0) {
            alert('Os dados do quiz ainda não foram carregados.');
            return;
        }
        
        const formData = new FormData(quizForm);
        let totalScore = 0;
        let questionsAnswered = 0;
        
        for (const entry of formData.entries()) {
            totalScore += parseInt(entry[1], 10);
            questionsAnswered++;
        }
        
        if (questionsAnswered < quizData.length) {
            alert('Por favor, responda todas as perguntas.');
            return; 
        }
        
        let description = '';

        if (totalScore <= 23) { 
            profile = 'Conservador';
            description = 'Você prioriza a segurança acima de tudo e prefere não correr riscos, mesmo que isso signifique uma rentabilidade menor. Suas escolhas são focadas em proteger seu patrimônio.';
        } else if (totalScore <= 36) { 
            profile = 'Moderado';
            description = 'Você busca um equilíbrio entre segurança e rentabilidade. Está disposto a aceitar alguns riscos controlados em troca de um retorno melhor, mas ainda preza pela estabilidade.';
        } else { 
            profile = 'Arrojado';
            description = 'Você entende bem o mercado e está disposto a assumir riscos mais elevados em busca de alta rentabilidade. Você vê as quedas como oportunidades e foca no longo prazo.';
        }

        await updateUserProfile(profile);
        
        resultProfile.innerText = `Seu Perfil: ${profile}`;
        resultDescription.innerText = description;
        
        recommendationsContainer.innerHTML = '';
        realTimeContainer.innerHTML = '';

        loadRecommendations(profile);
        await loadRealTimeData(profile);

        resultContainer.style.display = 'block';
        quizContainer.style.display = 'none';
    }
    
    if (continueBtn) {
        continueBtn.addEventListener('click', () => {
            window.location.href = `/dashboard`;
        });
    }

    loadQuiz();
    submitButton.addEventListener('click', showResults);
});