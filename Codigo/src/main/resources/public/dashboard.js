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
        const hashSaveUser = md5('currentUser');
        sessionStorage.removeItem(hashSaveUser);
        window.location.href = '/login'; 
    }); 
}

var actives = [];
var managerGraphic = new ManagerGraphicsActives();

async function updateGraphic() {
    $('.graphic-container').empty();
    $('.watchlist-content').empty();

    $('.graphic-container').append('<div class="loader-container"><div class="loader"></div></div>');
    $('.graphic-container').append('<canvas id="chartCanvas"></canvas>');

    actives = await managerGraphic.searchTopActives(
        $('#filter_value').val(),
        $('#filter_estate_fund').val(),
        $('#filter_sector').val()
    );

    var titleGraphic = managerGraphic.drawGraphics($('#filter_value').val(), $('#chartCanvas'));
    $('.loader-container').fadeOut(300, function() { $(this).remove(); });
    $('.graphic-container').prepend(`<h2 class="chart-title">${titleGraphic}</h2>`);

    for (var active of actives) {
        $('.watchlist-content').append(
            `<div class="asset-card">
                <img src="${active.img_url}">
                <div class="asset-info">
                    <strong>${active.acronym}</strong>
                    <p>${active.name ? active.name.substring(0, 25) + '...' : 'Nome indisponível'}</p>
                </div>
            </div>`
        );
    }
}

$(document).ready(function() {
    updateGraphic();
    $('#filter_value').change(function() { updateGraphic(); });
    $('#filter_estate_fund').change(function() { updateGraphic(); });
    $('#filter_sector').change(function() { updateGraphic(); });
});