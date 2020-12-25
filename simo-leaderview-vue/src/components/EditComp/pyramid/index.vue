<template>
  <div class="pyram_box">
    <svg
      version="1.1"
      xmlns="http://www.w3.org/2000/svg"
      xmlns:xlink="http://www.w3.org/1999/xlink"
      x="0px"
      y="0px"
      viewBox="0 0 760 476"
      style="enable-background:new 0 0 760 476;"
      xml:space="preserve"
    >
     <!-- <rect width="100%" height="100%"
  style="fill:none;stroke-width:1;stroke:rgb(0,0,0)"/> -->
      <g :transform="`translate(${760 / 2}, 175)`" >
        <g
          v-for="(item , index) in chartData"
          :key="`g_${index}`"
          class="tri_1"
          :transform="`scale(${getScale(index)},${getScale(index)})`"
        >
          <polygon class="st0" :points="`0,0 ${pointX},${pointY} 0,${minHeight}`" :fill="getColor(index).colors[1]" />
          <polygon class="st1" :points="`0,0 ${-pointX},${pointY} 0,${minHeight}`" :fill="getColor(index).colors[0]" />
        </g>

        <g v-for="(item , index) in chartData" :key="`label_line_${index}`" class="tri_1">
          <g
            v-if="index % 2 != 0"
            :transform="`translate(${getLabelTranslateX(index)},${getLabelTranslateY(index)})`"
          >
            <path
              class="st2"
              d="M264.8,32.5v-1h-195v0.4L52.4,51.2c-1-0.8-2.3-1.2-3.7-1.2c-3.3,0-6,2.7-6,6s2.7,6,6,6s6-2.7,6-6
			c0-1.6-0.6-3-1.6-4.1l17.5-19.3L264.8,32.5L264.8,32.5z"
            />
          </g>

          <g
            v-else
            :transform="`scale(-1, 1) translate(${getLabelTranslateX(index)},${getLabelTranslateY(index)})`"
          >
            <path
              class="st2"
              d="M264.8,32.5v-1h-195v0.4L52.4,51.2c-1-0.8-2.3-1.2-3.7-1.2c-3.3,0-6,2.7-6,6s2.7,6,6,6s6-2.7,6-6
			c0-1.6-0.6-3-1.6-4.1l17.5-19.3L264.8,32.5L264.8,32.5z"
            />
          </g>
        </g>
        <g v-for="(item , index) in chartData" :key="`label_${index}`" class="tri_1">
          <g
            :transform="`translate(${getLabelTranslateX2(index)},${getLabelTranslateY(index) +30})`"
            :text-anchor="`${index % 2 != 0 ? 'end' : 'start'}`"
            fill="#fff"
          >
            <text class="label_name">
              <tspan>{{item.value}}/</tspan>
              <tspan class fill="red">{{item.alarm}}</tspan></text>
            <text class="label_value" dy="25" fill="#0ac2fe">{{item.name}}
</text>
          </g>
        </g>
      </g>
    </svg>
  </div>
</template>

<script>
import * as d3 from "d3";

// import Mock from "mockjs"
// let testData = Mock.mock({
//   'list|5': [{
//     'name|+1': ['交换机', '路由器', '服务器', '主机', '其他设备'],
//     'value|10-100': 100,
//     'alarm|1-10': 10
//   }]
// })['list']

export default {
  name: "PyramidChart",
  props: ['item'],
  data () {
    return {
      // chartData: testData
      // chartData: Array.from({ length: 5 }, (v, i) =>
      //   Math.floor(Math.random() * 1000)
      // ),
    };
  },
  computed: {
    chartData () {
      return this.item.chartData.rows
    },
    len () {
      return this.chartData.length
    },
    minHeight () {
      return 225 / 0.5 / (this.len + 1)
    },
    pointX () {
      return this.minHeight * Math.sin(Math.PI / 4.4)
    },
    pointY () {
      return this.minHeight * Math.cos(Math.PI / 4.4)
    },
  },
  methods: {
    getScale (index) {
      return 0.5 * (this.len - index + 1)
    },
    getColor (i) {
      let color11 = "#37ccca";
      let color12 = "#2238a5";
      let gradientColor = d3.interpolate(
        { colors: ["#37ccca", "#3ee6e3"] },
        { colors: ["#2238a5", "#344bbf"] }
      );
      return gradientColor(1 - (1 / this.len) * i);
    },
    getLabelTranslateX(index) {
      // 49.25为基准偏移
      return ( 0.5 + 0.5 * index ) * this.pointX - 49.25;
      // return this.pointX - 49.25;
      //   return index % 2 == 0 ? -positionX : positionX
    },
    getLabelTranslateX2(index) {
      // 49.25为基准偏移
      let positionX = 0.5 * (index - 1.2) * this.pointX;
      // console.log(positionX);
      if (index % 2 != 0) {
        return positionX + 220;
      }
      return -positionX - 220;
    },
    getLabelTranslateY(index) {
      // 56.56为基准偏移
      return ( 0.75 + 0.5 * index ) * this.pointY - 56.56;
      // return this.pointY - 56.56;
      // return - 56.56;
    },
  },
};
</script>

<style lang="scss" scoped>
.pyram_box {
  width: 100%;
  height: 100%;
  //   .st1 {
  // 	transform: rotate(40deg);
  //   }
  .svg_wrap {
    // fill: 'purple';
    // strokeWidth: 1;
    // stroke: 'rgb(0,0,0)'

  }
  .st2 {
    fill-rule: evenodd;
    clip-rule: evenodd;
    fill: rgba(250, 250, 250, 0.33);
  }

  .label_name,
  .label_value {
    color: #ffffff;
    font-size: 24px;
  }
}
</style>
