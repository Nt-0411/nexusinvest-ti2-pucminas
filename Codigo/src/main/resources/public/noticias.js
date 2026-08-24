// Altere a variável no início do seu ficheiro:
const backendUrl = "http://localhost:6789/api/noticias";
const containerCarrosel = document.querySelector(".carrossel-container");
var allNotices = [];

// Trava de segurança BLINDADA
function validarLink(url) {
  if (url && url.startsWith("http")) {
    return { href: url, target: "_blank", onClick: "" };
  }
  // Se não houver link, o preventDefault bloqueia a criação da aba nova
  return { 
    href: "#", 
    target: "_self", 
    onClick: "event.preventDefault(); alert('A notícia original não possui um link válido na fonte.');" 
  };
}

async function loadNoticias() {
  try {
    const response = await fetch(backendUrl);
    const noticias = await response.json();

    if (!noticias || noticias.length === 0) {
      containerCarrosel.innerHTML = "<p>Nenhuma notícia encontrada!</p>";
      return;
    }

    allNotices = noticias;
    const destaque = noticias.slice(0, 3);
    let stringHtml = `
      <div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner">
    `;

    destaque.forEach((element, index) => {
      const activeClass = index === 0 ? "active" : "";
      
      const temImagemValida = element.urlImagem && element.urlImagem.startsWith("http");
      const img = temImagemValida ? element.urlImagem : "https://picsum.photos/800/400?random=" + Math.random();
      
      let descricao = element.conteudo || "Sem descrição disponível.";
      if (descricao.length > 300) descricao = descricao.substring(0, 300) + "[...]";

      // Passa o link pela nossa trava de segurança
      const linkSeguro = validarLink(element.urlNoticia);

      stringHtml += `
        <div class="carousel-item ${activeClass}">
          <img src="${img}" class="d-block" alt="Notícia" onerror="this.onerror=null; this.src='https://picsum.photos/800/400?random=${Math.random()}';">
          <div class="carousel-caption">
            <h5>${element.titulo}</h5>
            <p>${descricao}</p>
            <a href="${linkSeguro.href}" target="${linkSeguro.target}" onclick="${linkSeguro.onClick}" class="btn btn-light btn-sm">Ler mais</a>
          </div>
        </div>
      `;
    });

    stringHtml += `
        </div>
        <button class="carousel-control-prev" type="button"
          data-bs-target="#carouselExampleAutoplaying" data-bs-slide="prev">
          <span class="carousel-control-prev-icon"></span>
        </button>
        <button class="carousel-control-next" type="button"
          data-bs-target="#carouselExampleAutoplaying" data-bs-slide="next">
          <span class="carousel-control-next-icon"></span>
        </button>
      </div>
    `;

    containerCarrosel.innerHTML = stringHtml;
    loadCards(noticias.slice(3, 9));

  } catch (error) {
    console.error("Erro ao carregar notícias:", error);
    containerCarrosel.innerHTML = "<p>Erro ao carregar notícias.</p>";
  }
}

function loadCards(noticias) {
  let cards = document.querySelector(".cardsnoticias");
  cards.innerHTML = ""; 

  noticias.forEach(element => {
    let descricao = element.conteudo || "Sem descrição disponível.";
    if (descricao.length > 150) descricao = descricao.substring(0, 150) + "...";
    
    const temImagemValida = element.urlImagem && element.urlImagem.startsWith("http");
    const img = temImagemValida ? element.urlImagem : "https://picsum.photos/300/200?random=" + Math.random();
    
    // Passa o link pela nossa trava de segurança
    const linkSeguro = validarLink(element.urlNoticia);

    cards.innerHTML += `
      <a href="${linkSeguro.href}" target="${linkSeguro.target}" onclick="${linkSeguro.onClick}" style="text-decoration:none; color:inherit;">
        <article class="noticia-card">
          <img src="${img}" alt="Imagem da notícia" onerror="this.onerror=null; this.src='https://picsum.photos/300/200?random=${Math.random()}';">
          <div class="noticia-card-body">
            <h3>${element.titulo}</h3>
            <p>${descricao}</p>
            <small style="color: var(--primary-blue); font-weight: 600;">
               <i class="fa-solid fa-link"></i> ${element.fonte || "Mercado"}
            </small>
          </div>
        </article>
      </a>
    `;
  });
}

function searchBar() {
  const $input = $(".search-box input");
  const $results = $("#search-results");

  $input.on("input", function () {
    const termo = $(this).val().toLowerCase().trim();
    $results.empty();
    if (!termo) { $results.hide(); return; }

    const filtrados = allNotices.filter(n =>
      (n.titulo && n.titulo.toLowerCase().includes(termo)) ||
      (n.conteudo && n.conteudo.toLowerCase().includes(termo))
    );

    if (filtrados.length === 0) { $results.hide(); return; }

    filtrados.slice(0, 5).forEach(noticia => {
      // Passa o link pela nossa trava de segurança
      const linkSeguro = validarLink(noticia.urlNoticia);
      
      $results.append(`
        <a href="${linkSeguro.href}" target="${linkSeguro.target}" onclick="${linkSeguro.onClick}" style="text-decoration: none;">
          <div>
            <strong>${noticia.titulo}</strong>
          </div>
        </a>
      `);
    });
    $results.show();
  });

  $(document).click(function (e) {
    if (!$(e.target).closest(".search-box").length) $results.hide();
  });
}

window.onload = async () => {
  await loadNoticias();
  searchBar();
};