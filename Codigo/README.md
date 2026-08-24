# Código do Projeto

Mantenha neste diretório todo o código do projeto. Se necessário, descreva neste arquivo aspectos relevantes da estrutura de diretórios criada para organização do código.

## Códigos SQLs
CREATE TABLE perfil_investidor (
    id_perfil SERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL
);

CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    foto VARCHAR(255),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_perfil INT 
);


CREATE TABLE noticia (
    id_noticia SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    conteudo TEXT,
    url_noticia VARCHAR(500),
    url_imagem VARCHAR(500),
    fonte VARCHAR(100),
    data_publicacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT
);

CREATE TABLE ativo_financeiro (
    id_ativo SERIAL PRIMARY KEY,
    ticket VARCHAR(10) NOT NULL UNIQUE,
    nome_ativo VARCHAR(100) NOT NULL,
    setor VARCHAR(50),
    tipo VARCHAR(50)
);

CREATE TABLE video_educacional (
    id_video SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descricao VARCHAR(600),
    categoria VARCHAR(50),
    duracao INT,
    url_youtube VARCHAR(255)
);

CREATE TABLE "Metas" (
    id_meta SERIAL PRIMARY KEY,
    id_user INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(600),
    "valorTotal" DECIMAL(12,2) NOT NULL,
    "valorAlcancado" DECIMAL(12,2) NOT NULL,
    status BOOLEAN DEFAULT true,
    "dataPrazo" DATE,
    "dataCriacao" TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    "dataAtualizado" TIMESTAMP WITH TIME ZONE
);

CREATE TABLE simulacao_carteira (
    id_simulacao SERIAL PRIMARY KEY,
    saldo DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    data_simulacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL 
);

CREATE TABLE topico_forum (
    id_topico SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    conteudo VARCHAR(600) NOT NULL,
    status VARCHAR(20) DEFAULT 'Ativo',
    data_publicacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL 
);

CREATE TABLE item_simulacao (
    id_item SERIAL PRIMARY KEY,
    quantidade INT NOT NULL,
    data_compra DATE NOT NULL,
    preco_compra DECIMAL(10,4) NOT NULL,
    id_simulacao INT NOT NULL,
    id_ativo INT NOT NULL
);

CREATE TABLE usuario_assiste_video (
    id_usuario INT NOT NULL,
    id_video INT NOT NULL,
    data_visualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'Não Visto',
    PRIMARY KEY (id_usuario, id_video) 
);

CREATE TABLE comentario_topico (
    id_comentario SERIAL PRIMARY KEY,
    conteudo TEXT NOT NULL,
    data_publicacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_topico INT NOT NULL,
    id_usuario INT NOT NULL
);


ALTER TABLE usuario 
    ADD CONSTRAINT fk_usuario_perfil 
    FOREIGN KEY (id_perfil) REFERENCES perfil_investidor(id_perfil);


ALTER TABLE noticia 
    ADD CONSTRAINT fk_noticia_usuario 
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE SET NULL;

ALTER TABLE "Metas" 
    ADD CONSTRAINT fk_metas_usuario 
    FOREIGN KEY (id_user) REFERENCES usuario(id_usuario) ON DELETE CASCADE;

ALTER TABLE simulacao_carteira 
    ADD CONSTRAINT fk_simulacao_usuario 
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE;

ALTER TABLE topico_forum 
    ADD CONSTRAINT fk_topico_usuario 
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE;

ALTER TABLE item_simulacao 
    ADD CONSTRAINT fk_item_simulacao 
    FOREIGN KEY (id_simulacao) REFERENCES simulacao_carteira(id_simulacao) ON DELETE CASCADE;

ALTER TABLE item_simulacao 
    ADD CONSTRAINT fk_item_ativo 
    FOREIGN KEY (id_ativo) REFERENCES ativo_financeiro(id_ativo);

ALTER TABLE usuario_assiste_video 
    ADD CONSTRAINT fk_assiste_usuario 
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE;

ALTER TABLE usuario_assiste_video 
    ADD CONSTRAINT fk_assiste_video 
    FOREIGN KEY (id_video) REFERENCES video_educacional(id_video) ON DELETE CASCADE;

ALTER TABLE comentario_topico 
    ADD CONSTRAINT fk_comentario_topico 
    FOREIGN KEY (id_topico) REFERENCES topico_forum(id_topico) ON DELETE CASCADE;

ALTER TABLE comentario_topico 
    ADD CONSTRAINT fk_comentario_usuario 
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE;


    CREATE TABLE mensagem_forum (
    id_mensagem SERIAL PRIMARY KEY,
    id_topico INT NOT NULL,
    id_usuario INT NOT NULL,
    conteudo TEXT NOT NULL,
    data_publicacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_topico FOREIGN KEY (id_topico) REFERENCES topico_forum(id_topico) ON DELETE CASCADE,
    CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);


INSERT INTO video_educacional (titulo, descricao, categoria, duracao, url_youtube) VALUES

('O MELHOR PLANO PARA INVESTIR EM 2026!', 'Introdução prática sobre o melhor plano para investir em 2026!.', 'Planejamento Financeiro', 22, '6HLFlsAUwOQ'),

('Como SAIR da POBREZA em 1 ANO! (Se fizer isso, SUA VIDA MUDA!)', 'Introdução prática sobre como sair da pobreza em 1 ano!.', 'Educação Financeira', 17, 'kc-pjaoD_1k'),

('Como GANHAR R$50 por DIA com INVESTIMENTOS sem TRABALHAR!', 'Introdução prática sobre como ganhar r$50 por dia com investimentos.', 'Educação Financeira', 19, '56xx6D1o6ZY'),

('VALE A PENA FAZER CONSÓRCIO EM 2026? (O que ninguém te conta)', 'Introdução prática sobre vale a pena fazer consórcio em 2026?.', 'Planejamento Financeiro', 17, 'Zx70hRZ4I3c'),

('MELHORES INVESTIMENTOS PARA 2026 | AÇÕES, FIIS, BITCOIN OU RENDA FIXA?', 'Introdução prática sobre melhores investimentos para 2026.', 'Investimentos', 34, 'feNaG7KQJpQ'),

('OS 12 MAIORES ERROS QUE TODO INVESTIDOR INICIANTE COMETE', 'Introdução prática sobre os 12 maiores erros que todo investidor iniciante comete.', 'Educação Financeira', 10, 'VVxIDkp2MMA'),

('OS PRÓXIMOS 90 DIAS TE FARÃO RICO (o corte da Selic explica o porquê)', 'Introdução prática sobre os próximos 90 dias te farão rico.', 'Educação Financeira', 9, 'uSRKumTgKv0'),

('POBRE OSTENTAÇÃO - O tipo de pobre MAIS IMBECIL que existe!', 'Introdução prática sobre por que ostentação é ridículo!.', 'Investimentos', 9, '27VbFHsv4rQ'),

('PRIMO POBRE e PRIMO RICO - Será que o FUNDO ARCA é um BOM INVESTIMENTO?', 'Introdução prática sobre o fundo arca para quem é pobre.', 'Renda & Patrimônio', 34, 'B2BhYAflaYI'),

('COMO CALCULAR A RENTABILIDADE DOS INVESTIMENTOS', 'Introdução prática sobre rentabilidade (CDI, SELIC e IPCA).', 'Educação Financeira', 16, 'dss4yx6HVl0'),

('Como Quitar um Financiamento de 30 anos em 3 Anos', 'Introdução prática sobre como quitar financiamento rápido.', 'Educação Financeira', 7, '2kfNsNFOK6U'),

('5 Lugares Que os Pobres Amam: E Que os Ricos Evitam!', 'Introdução prática sobre 5 lugares que os ricos evitam!.', 'Renda & Patrimônio', 8, 'kUseQ0WN4w0'),

('TESOURO IPCA+ ou TESOURO PREFIXADO Qual É O MELHOR', 'Introdução prática sobre tesouro ipca+ ou tesouro prefixado.', 'Investimentos', 8, 'BVPLI2CYTHg'),

('AÇÕES DE CRESCIMENTO OU QUE PAGAM DIVIDENDOS?', 'Introdução prática sobre estratégias de dividendos vs crescimento.', 'Investimentos', 7, 'nGU0l0na-YY'),

('5 PIORES INVESTIMENTOS DO BRASIL | NÃO CAIA NESSAS CILADAS', 'Introdução prática sobre 5 piores investimentos do brasil.', 'Educação Financeira', 11, '9bpgf-TaCPA'),

('RENDA FIXA NO NUBANK: PASSO A PASSO PARA INICIANTES', 'Introdução prática sobre renda fixa no nubank.', 'Investimentos', 6, 'zGQcAL7eEAc'),

('ALERTA: A Caixa Entra no Mundo das Bets', 'Introdução prática sobre o impacto das bets nas finanças.', 'Comportamento Financeiro', 2, 'SbHR8h_QizY'),

('#ETFs - COMPREI MEU PRIMEIRO ETF BRASILEIRO!', 'Introdução prática sobre compra de etfs no brasil.', 'Investimentos', 17, 'Mcr4NEhqzOo'),

('#VGIR11 - FICOU MAIS ARRISCADO!', 'Introdução prática sobre o fundo imobiliário vgir11.', 'Educação Financeira', 9, 'Hp3zElOleRo'),

('#VGHF11 - OS MOTIVOS DO RESULTADO FRACO!', 'Análise sobre o resultado do fundo vghf11.', 'Educação Financeira', 16, 'YWYcZk5rMrU'),

('#KNRI11 - AINDA ESTÁ BARATO?', 'Análise do fundo imobiliário knri11.', 'Educação Financeira', 9, 'Pi_iuRTU06g'),

('#TRXF11 - MINHAS OPINIÕES SOBRE AS COMPRAS!', 'Opiniões sobre as aquisições do fundo trxf11.', 'Educação Financeira', 24, 'lF5SlIzhwPQ'),

('#KNSC11 - O MAIOR IMPACTO JÁ VEIO!', 'Introdução prática sobre o fundo knsc11.', 'Educação Financeira', 7, 'W-pXPu3Z-TA'),

('Receba DIVIDENDOS em DÓLAR com essas 3 AÇÕES!', 'Introdução prática sobre ações que pagam em dólar.', 'Investimentos', 24, 'ATvXrQG6XkY'),

('3 FIIs PAGANDO MAIS DE 14% ao ano ISENTO DE IMPOSTOS', 'Introdução prática sobre fiis baratos e com bons dividendos.', 'Investimentos', 16, 'ZeO0C7wkV88'),

('4 FIIs para Comprar e NUNCA Tirar da Carteira!', 'Introdução prática sobre 4 fiis para o longo prazo.', 'Investimentos', 28, 'A5vj4isFV_s'),

('3 AÇÕES QUE PODEM EXPLODIR EM 2026!', 'Introdução prática sobre 3 ações promissoras para 2026.', 'Investimentos', 20, 'MUy8_N6KVvQ'),

('Ele pediu R$100 MILHÕES emprestado pra comprar Bitcoin', 'História sobre empréstimo bilionário para bitcoin.', 'Investimentos', 37, '6Fd7GkGi2Qc'),

('VISITANDO O ESCRITÓRIO DA XP INVESTIMENTOS', 'Introdução prática sobre os bastidores da xp investimentos.', 'Educação Financeira', 56, 'ZM460bF8kic'),

('INVESTI EM UM FII QUE ME PAGA R$ 15 MIL/MÊS', 'Introdução prática sobre rendimentos altos em fiis.', 'Investimentos', 32, 'g_etAzKTXZ8'),

('INVESTIR EM SELIC A 15% É FURADA!', 'Introdução prática sobre por que fugir da selic alta às vezes.', 'Educação Financeira', 8, 'phmJ4TmvF3U'),

('ELA FATURA R$200 MIL POR MÊS | O Plano Perfeito #5', 'Estudo de caso sobre faturamento e planejamento.', 'Planejamento Financeiro', 2, 'P9D2CE1P7q8'),

('A MAIOR CRISE DA HISTÓRIA ESTÁ POR VIR?', 'Como se preparar para crises financeiras globais.', 'Educação Financeira', 12, 'rAchcsT5mFk'),

('AJUDANDO UM PROFESSOR MULTI-MILIONÁRIO A INVESTIR', 'Consultoria prática de investimentos.', 'Educação Financeira', 21, '3iU8v20JMKs'),

('CAIU 15 MILHÕES NA CONTA | Onde vou investir?', 'Introdução prática sobre alocação de grandes patrimônios.', 'Educação Financeira', 16, 'orQHin0svsE'),

('PEDI CONSELHO PARA UM BILIONÁRIO SOBRE COMO INVESTIR', 'Conselhos reais de um bilionário sobre finanças.', 'Educação Financeira', 23, 'h9xuCr53BsA'),

('ENCONTREI UMA JOIA ESCONDIDA NA BOLSA?', 'Análise de uma nova ação para a carteira.', 'Investimentos', 44, '5wIaUoqBnfM'),

('CORREIOS EM COLAPSO: ENTENDA O ROMBO!', 'Análise financeira sobre a situação dos correios.', 'Educação Financeira', 6, 'LXNq4KhSPAg'),

('IOF CANCELADO! O Que Muda em 2026', 'Introdução prática sobre mudanças nos impostos.', 'Planejamento Financeiro', 6, '3zcKvYqqzng'),

('Aprenda o SEGREDO para COMPRAR AÇÕES Mais BARATAS', 'Passo a passo para comprar ações no melhor preço.', 'Investimentos', 10, '3-rWAzmOloQ'),

('A FALÊNCIA DA OI: o alerta que todo investidor precisa', 'Lições sobre o caso de falência da oi.', 'Comportamento Financeiro', 9, 'yhqr_PH-9K4'),

('IPCA+ 8%: A Maior Oportunidade OU Armadilha?', 'Análise sobre rentabilidade de ipca+ 8%.', 'Educação Financeira', 7, '3xoSq0RJCmA'),

('Como Começar a Investir Pelo Nubank | Passo a Passo', 'Tutorial simples para iniciantes no nubank.', 'Educação Financeira', 11, 'PlINMuDXOpo'),

('5 Lugares Que os Ricos Frequentam', 'Introdução prática sobre networking e mentalidade rica.', 'Renda & Patrimônio', 12, 'wobVAbOQHqs'),

('A Maior ARMADILHA do MERCADO FINANCEIRO!', 'Tentar comprar na baixa e vender na alta pode ser perigoso.', 'Investimentos', 9, 'IpgvJQMCwvo'),

('NUBANK: Fim da RENTABILIDADE de 200% do CDI', 'O que mudou na rentabilidade do nubank.', 'Educação Financeira', 7, 'Rcakcy0CXS4'),

('BITCOIN: Por que comecei a investir e estratégia', 'Estratégia pessoal de investimento em cripto.', 'Investimentos', 14, '5wkoRBQO4E8'),

('OURO NA MÁXIMA HISTÓRICA! Vale a pena?', 'Como investir em ouro na prática.', 'Educação Financeira', 6, '7ge-FkdAWxM'),

('DARF: O Que é, Como Emitir e Pagar', 'Tutorial prático sobre emissão de darf.', 'Educação Financeira', 8, 'Zt8oK6IMfgI'),

('Como Ser Rico em Todas as Áreas da Vida?', 'Introdução prática sobre sucesso financeiro e espiritual.', 'Educação Financeira', 8, '_LIiD5WStGQ'),

('3ª GUERRA MUNDIAL à Vista: O Que FAZER?', 'Como proteger investimentos em cenários de guerra.', 'Educação Financeira', 8, 'epnO91JuJec'),

('PIX AUTOMÁTICO: COMO FUNCIONA?', 'Passo a passo simples sobre o pix automático.', 'Educação Financeira', 7, 'rPqALSv61SA'),

('Haddad vai taxar FIIs, LCI e LCA? Fim da isenção!', 'Análise sobre possíveis mudanças na tributação.', 'Investimentos', 8, 'icuKC00Xfjg'),

('Você Está Preparado para a Próxima Crise?', 'Introdução prática sobre preparação financeira.', 'Educação Financeira', 9, 'skxDlpF5cbo'),

('FUNDOS QUASE DE GRAÇA! OPORTUNIDADE NA BOLSA!', 'Análise de fundos subvalorizados.', 'Educação Financeira', 21, 'NekcZB8SWzQ'),

('Carteira de INVESTIMENTOS PARA INICIANTES', 'Como começar com pouco dinheiro agora mesmo.', 'Investimentos', 14, 'yoV7Dv07UHU'),

('CRISE DE 2025: OPORTUNIDADE DO SÉCULO?', 'Empresas que analistas estão recomendando.', 'Educação Financeira', 17, 'GqMI3XXFZFk'),

('Como me Tornei MILIONÁRIA ANTES DOS 30 ANOS!', 'Relato sobre liberdade financeira precoce.', 'Educação Financeira', 14, '8hjAmZH4j4U'),

('QUANTO RENDE 1000, 5000 E 10.000 NO BANCO DO BRASIL?', 'Simulação de investimentos no banco do brasil.', 'Educação Financeira', 9, 'bqB-Sed4jSg'),

('ENCHI O CARRINHO DE FUNDOS IMOBILIÁRIOS!', 'Introdução prática sobre fundos excelentes e baratos.', 'Educação Financeira', 22, 'pEkiwNzm2ag'),

('Como VOCÊ Pode LUCRAR MUITO com a SELIC ALTA', 'Estratégias para aproveitar a selic em 2025.', 'Educação Financeira', 12, '8w3aGYQQvzA'),

('Quanto VOCÊ Precisa Pra VIVER DE RENDA?', 'Cálculo prático para aposentadoria precoce.', 'Renda & Patrimônio', 14, 'UOrNKy0seu8'),

('MELHORES AÇÕES PARA COMPRAR AGORA!', 'Ações que podem valorizar acima de 40%.', 'Investimentos', 21, 'saTmZW38Kf0'),

('SELIC MUDOU, E MINHA CARTEIRA TAMBÉM!', 'O que fazer na alta da selic.', 'Investimentos', 16, 'g_8gKyF2yNI'),

('MELHORES FUNDOS IMOBILIÁRIOS PARA JULHO!', 'Fundos baratos com altos dividendos para julho.', 'Educação Financeira', 20, 'syQe5AaptKQ'),

('Configurando Metas de Longo Prazo', 'Aprenda a definir objetivos claros e mensuráveis.', 'Auto-gestão', 9, 'XgJuqsNKCVo');


INSERT INTO perfil_investidor (descricao) 
VALUES 
('Conservador'), 
('Moderado'), 
('Arrojado');