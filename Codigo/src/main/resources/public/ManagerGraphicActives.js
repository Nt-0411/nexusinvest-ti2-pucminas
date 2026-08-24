

class ManagerGraphicsActives {
    // Token entregue pela rota /config.js, fora do codigo-fonte
    token = (window.NEXUS_CONFIG && window.NEXUS_CONFIG.brapiToken) || '';
    urlVolume;
    urlChange;
    urlClose;
    urlAll;
    drawChart;
    actives = [];

    async #findActive(activeName) {
        try {
            const urlSingleActive = `https://brapi.dev/api/quote/${activeName}?range=1mo&interval=1d&token=${this.token}`;
            const response = await fetch(urlSingleActive, {
                headers: { 'Authorization': `Bearer ${this.token}` }
            });
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            return response.json();
        } catch (exception) {
            console.error('ERROR IN : ', exception);
        }
    }

    async #fetchTopActives(url) {
        let activesSearched = [];

        try {
            const response = await fetch(url, {
                headers: { 'Authorization': `Bearer ${this.token}` }
            });
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            const data = await response.json();
            let activesReponse = data.stocks || data.funds || data.bdrs || [];
            let id = 1;

            for (let activeReponse of activesReponse) {
                try {
                    const result = (await this.#findActive(activeReponse.stock)).results[0];
                    activesSearched.push({
                        "id": id,
                        "acronym": result.symbol,
                        "img_url": result.logourl,
                        "name": result.longName,
                        "currency": result.currency,
                        "intervals": result.historicalDataPrice,
                        "change": result.regularMarketChangePercent,
                        "points": []
                    });
                    id++;
                } catch (error) {
                    alert(`Falha ao buscar o ativo : ${activeReponse.stock}`);
                    continue;
                }
            }

        } catch (error) {
            console.error('Falha ao buscar cotações:', error);
        }
        return activesSearched;
    }

    #getPointsToGraphics(mode, active) {
        var points = [];
        var color = Math.random() * 255;
        if (mode == "volume") {
            for (let interval of active.intervals) {
                points.push({
                    "labelX": interval.date,
                    "data": (Number)(interval.volume),
                    "color": `rgba(${Math.random() * 255}, ${color}, ${Math.random() * 255}  , 0.6)`
                });
            }
        } else if (mode == "change") {
            points.push({
                "labelX": active.acronym,
                "data": (Number)(active.change).toFixed(2),
                "color": `rgba(${Math.random() * 255}, ${color}, ${Math.random() * 255}  , 0.6)`
            });
        } else if (mode == "simulation") {
            for (let i =1; i < active.intervals.length; i++) {
                let percentChange =  ((active.intervals[i].close - active.intervals[i-1].close) * 100) / active.intervals[i-1].close;
                points.push({
                    "labelX": active.intervals[i].date,
                    "data": percentChange,
                    "color": `rgba(${Math.random() * 255}, ${color}, ${Math.random() * 255}  , 0.6)`
                });
            }
        } else {
            for (let interval of active.intervals) {
                points.push({
                    "labelX": [interval.date, active.acronym],
                    "data": interval.close,
                    "color": `rgba(${Math.random() * 255}, ${color}, ${Math.random() * 255}  , 0.6)`
                });
            }
        }
        return points;
    }

    #getPointsToGraphicSimulation(investimentValue ,active){
        var points = [];
        var color = Math.random() * 255;
        let qtdBuyFix = investimentValue / active.intervals[0].close;
        for (let i =1; i < active.intervals.length; i++) {                
                points.push({
                    "labelX": active.intervals[i].date,
                    "data": (qtdBuyFix * active.intervals[i].close).toFixed(2),
                    "color": `rgba(${Math.random() * 255}, ${color}, ${Math.random() * 255}  , 0.6)`
                });
            }
        return points;
    }

    setUrl(sector,type){
        this.urlVolume = `https://brapi.dev/api/quote/list?sector=${sector}&sortBy=volume&sortOrder=desc&limit=5&page=1&type=${type}&token=${this.token}`;
        this.urlChange = `https://brapi.dev/api/quote/list?sector=${sector}&sortBy=change&sortOrder=desc&limit=5&page=1&type=${type}&token=${this.token}`;
        this.urlClose = `https://brapi.dev/api/quote/list?sector=${sector}&sortBy=close&sortOrder=desc&limit=5&page=1&type=${type}&token=${this.token}`;
        this.urlAll = `https://brapi.dev/api/quote/list?sector=${sector}&sortBy=volume&sortOrder=desc&type=${type}&token=${this.token}`;
    }
    
    async searchTopActives(mode, type, sector) {
        let url;
        this.setUrl(sector,type);
        switch (mode) {
            case "volume":
                url = this.urlVolume;
                break;
            case "change":
                url = this.urlChange;
                break;
            case "close":
                url = this.urlClose;
                break;
            default:
                url = this.urlChange;
        }
        
        this.actives = await this.#fetchTopActives(url);
        for (let i = 0; i < this.actives.length; i++) {
            for (let j = 0; j < this.actives[i].intervals.length; j++) {
                this.actives[i].intervals[j].date = new Intl.DateTimeFormat('pt-BR').format(new Date(this.actives[i].intervals[j].date * 1000));
            }
            this.actives[i].points = this.#getPointsToGraphics(mode, this.actives[i]);
        }
        return this.actives;
    }


    async #fetchAllActivesSector(url) {
        let activesSearched = [];

        try {
            const response = await fetch(url, {
                headers: { 'Authorization': `Bearer ${this.token}` }
            });
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            const data = await response.json();
            let activesReponse = data.stocks || data.funds || data.bdrs || [];
            let id = 1;

            for (let activeReponse of activesReponse) {
                try {
                    activesSearched.push({
                        "id": id,
                        "acronym": activeReponse.stock ,
                        "img_url": activeReponse.logo,
                        "name": activeReponse.name,
                        "change": activeReponse.change,
                        "points": []
                    });
                    id++;
                } catch (error) {
                    alert(`Falha ao buscar o ativo : ${activeReponse.stock}`);
                    continue;
                }
            }

        } catch (error) {
            console.error('Falha ao buscar cotações:', error);
        }
        return activesSearched;
    }

    async searchAllActivesSector(type, sector) {
        let url;
        this.setUrl(sector,type);
        url = this.urlAll;
        this.actives = await this.#fetchAllActivesSector(url);
        return this.actives;
    }

    async searchSingleActive(investimentValue,indexActive){
        let activeSearched;
        try {
            const result = (await this.#findActive(this.actives[indexActive].acronym)).results[0];
            activeSearched = {
                "id": indexActive + 1,
                "acronym": result.symbol,
                "img_url": result.logourl,
                "name": result.longName,
                "currency": result.currency,
                "intervals": result.historicalDataPrice,
                "change": result.regularMarketChangePercent,
                "points": []
            };
            for (let j = 0; j < activeSearched.intervals.length; j++) {
                activeSearched.intervals[j].date = new Intl.DateTimeFormat('pt-BR').format(new Date(activeSearched.intervals[j].date * 1000));
            }
            activeSearched.points = this.#getPointsToGraphicSimulation(investimentValue, activeSearched);
            
            
        }catch (error) {
            alert(`Falha ao buscar o ativo : ${activeSearched.acronym} + ${error}`);  
        }
        return activeSearched;
    }

    drawGraphics(mode, canvas) {
        var draw = new DrawGraphics(mode, canvas);
        draw.setGraphics(this.actives);
        return draw.getTitleGraphic();
    }

    drawGraphic(active,canvas) {
        var draw = new DrawGraphics(null, canvas);
      
        draw.setGraphic(active);
    }

}
