import Vue from "vue";
import echarts from "echarts"

function getVirtulData(year) {
    year = year || '2017';
    var date = +echarts.number.parseDate(year + '-01-01');
    var end = +echarts.number.parseDate((+year + 1) + '-01-01');
    var dayTime = 3600 * 24 * 1000;
    var data = [];
    for (var time = date; time < end; time += dayTime) {
        data.push([
            echarts.format.formatTime('yyyy-MM-dd', time),
            Math.floor(Math.random() * 10000)
        ]);
    }
    return data;
}

export default Vue.component("heatmap", {
    template: `<div :style="style">热力图</div>`,
    // render (createElement) {
    //     return createElement('div', {
    //        class: [],
    //        style: {} 
    //     }, this.$slots.default)
    // },
    props: ['item'],
    data() {
        return {
            type: 'heatmap',
            count: 0
        };
    },
    computed: {
        style () {
            return {
                width: `${this.item.width}px`,
                height: `${this.item.height}px`
            }
        }
    },
    mounted () {
        this.option = {
            title: {
                top: 30,
                left: 'center',
                text: '2016年某人每天的步数'
            },
            tooltip: {},
            visualMap: {
                min: 0,
                max: 10000,
                type: 'piecewise',
                orient: 'horizontal',
                left: 'center',
                top: 65,
                textStyle: {
                    color: '#000'
                }
            },
            calendar: {
                top: 120,
                left: 30,
                right: 30,
                cellSize: ['auto', 13],
                range: '2016',
                itemStyle: {
                    borderWidth: 0.5
                },
                yearLabel: {show: false}
            },
            series: {
                type: 'heatmap',
                coordinateSystem: 'calendar',
                data: getVirtulData(2016)
            }
        };
        this.chart = echarts.init(this.$el);
        this.chart.setOption(this.option)

        console.log(this.chart)
        // this.chart.setOptions(option)
    },
    watch: {
        'item.width' (newVal, oldVal) {
            // console.log('chang width');
            this.resize()
        },
        'item.height' (newVal, oldVal) {
            // console.log('chang height');
            this.resize()
        },
       'item.chartData' (newVal, oldVal) {
           console.log('change data');
       }
    },
    methods: {
        resize () {
            this.chart.resize();
        },
        rerender () {

        }
    }
});
