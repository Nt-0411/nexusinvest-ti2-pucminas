class DrawGraphics {
    #details
    #canvas
    #type
    #text
    #legend
    #tooltip
    #drawChart;
    #mode;
    #labelX = [];
    #dataset = [];
    constructor(mode, canvas) {
        this.#canvas = canvas;
        this.#mode = mode;
    }


    draw() {

        var tooltip = this.#tooltip
        if (this.#drawChart) {
            this.#drawChart.destroy();
        }
        this.#drawChart = new Chart(this.#canvas, {
            type: this.#type,
            data: {
                labels: this.#details.labelX,
                datasets: this.#details.dataset,
            },
            options: {
                responsive: true,

                plugins: {
                    legend: {
                        display: this.#legend,
                        position: 'top',
                        labels: {
                            color: '#fff'
                        }
                    },
                    tooltip,

                    datalabels: {
                        anchor: 'end',
                        align: 'top',
                        color: '#ffff',
                        font: {
                            weight: 'bold'
                        },
                        formatter: value => value
                    }
                },
                borderRadius: 10,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            color: 'white'
                        },
                        grid : {
                            color: '#989c9f'
                        }
                    },
                    x: {
                        ticks: {
                            color: 'white'
                        },
                        grid : {
                            color: '#989c9f'
                        }
                    }
                }
            },
        });

    }

    setGraphicsModeVolume(actives) {
        if (actives.length > 0) {
            this.#labelX = actives[0].points.map(p => p.labelX);
        }
        for (let active of actives) {
            let datas = [];
            for (let point of active.points) {
                datas.push(point.data);
            }

            this.#dataset.push({
                label: active.acronym,
                data: datas,
                borderColor: active.points[0].color,
                backgroundColor: active.points[0].color,
                tension: 0.1
            })
        }
        this.#tooltip = {
            callbacks: {
                label: function (context) {
                    let label = '';

                    if (label) {
                        label += ': ';
                    }
                    if (context.parsed.y !== null) {
                        label += context.parsed.y + " negociadas";
                    }
                    return label;
                }

            }
        }
        this.#text = 'Ativos que possuem mais número de ações negociadas';
        this.#type = 'line';
        this.#legend = true;
    }

    setGraphicsSimulation(active) {
            let datas = [];
            this.#labelX = active.points.map(p => p.labelX);
            for (let point of active.points) {
                datas.push(point.data);
            }

            this.#dataset.push({
                label: active.acronym,
                data: datas,
                borderColor: active.points[0].color,
                backgroundColor: active.points[0].color,
                tension: 0.1
            })
        
        this.#tooltip = {
            callbacks: {
                label: function (context) {
                    let label = '';

                    if (label) {
                        label += ': ';
                    }
                    if (context.parsed.y !== null) {
                        label += "R$ " + context.parsed.y;
                    }
                    return label;
                }

            }
        }
        this.#text = '';
        this.#type = 'line';
        this.#legend = true;
    }

    setGraphicsOthersMode(actives, mode) {

        let datas = [];
        let colors = [];
        let labels = [];
        for (let active of actives) {
            try {
                this.#labelX.push(active.points[0].labelX);
                labels.push(active.acronym + "(%)");
                datas.push(active.points[0].data);
                colors.push(active.points[0].color);
            } catch (error) {
                alert(`Não há históricos de : ${active.acronym}`)
                continue;
            }

        }
        this.#tooltip = {
            callbacks: {
                label: function (context) {
                    let label = '';

                    if (label) {
                        label += ': ';
                    }
                    if (context.parsed.y !== null) {
                        if (mode == 'change') {
                            label += context.parsed.y + "%";
                        } else {
                            label += "R$ " + context.parsed.y;
                        }
                    }
                    return label;
                }
            }
        }
        this.#dataset.push({
            label: this.#labelX,
            data: datas,
            backgroundColor: colors,
            borderWidth: 1
        });
        this.#text = this.#mode == 'change' ? 'Variação percentual dos ativos (%)' : 'Ativos com maior preço (R$)';
        this.#legend = false;
        this.#type = 'bar';
    }

    setGraphics(actives) {
        this.#type = '';
        this.#text;
        this.#legend;
        this.#tooltip = {};

        if (this.#mode == "volume") {
            this.setGraphicsModeVolume(actives);
        } else {
            this.setGraphicsOthersMode(actives, this.#mode);
        }
        let labelX = this.#labelX;
        let dataset = this.#dataset;
        this.#details = { labelX, dataset };
        this.draw();
    }

    setGraphic(active) {
        this.#type = '';
        this.#text;
        this.#legend;
        this.#tooltip = {};
        this.setGraphicsSimulation(active);
        let labelX = this.#labelX;
        let dataset = this.#dataset;
        this.#details = { labelX, dataset };
        this.draw();
        
    }

    getTitleGraphic() {
        return this.#text;
    }
	
	

}

