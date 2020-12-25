export const axis = [
    {
        name: "x轴标注倾斜",
        key: "rotate",
        tag: "select",
        options: [0, 10, 20, 30, 40, -10, -20, -30, -40 ].map(d => ({
            name: d,
            value: d
        }))
    }
    // {
    //     name: "文字剪裁",
    //     key: "wordsCut",
    //     tag: "select",
    //     options: [
    //         {
    //             name: "自适应",
    //             val: true
    //         },
    //         {
    //             name: "不剪裁",
    //             val: false
    //         }
    //     ]
    // }
];


export default {
    item: {
        text: "极坐标柱状图",
        imgClass: "el-icon-s-grid",
        chartType: "polarBar",
        colorful: false,
        ifGradual: "false",
        chartData: {
            columns: ["时间", "地点", "人数"],
            rows: [
                { 时间: "星期一", 地点: "北京", 人数: 1000 },
                { 时间: "星期二", 地点: "上海", 人数: 400 },
                { 时间: "星期三", 地点: "杭州", 人数: 800 },
                { 时间: "星期二", 地点: "深圳", 人数: 200 },
                { 时间: "星期三", 地点: "长春", 人数: 100 },
                { 时间: "星期五", 地点: "南京", 人数: 300 },
                { 时间: "星期四", 地点: "江苏", 人数: 800 },
                { 时间: "星期一", 地点: "北京", 人数: 700 },
                { 时间: "星期三", 地点: "上海", 人数: 300 },
                { 时间: "星期二", 地点: "杭州", 人数: 500 }
            ]
        }
    },
    styles: {
        base: [],
        lengend: [],
        chart: [],
        axis,
    }
};
