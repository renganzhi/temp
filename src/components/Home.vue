<template>
  <div class="wrap">
    <DragResize :isActive="true"
                :w="200"
                :h="200">
      <Vtextarea :item="textItem"
                 :disabled="true">
      </Vtextarea>
    </DragResize>
    <DragResize :isActive="true"
                :w="200"
                :h="200">
      <Vprogress :item="VprogressItem"></Vprogress>
    </DragResize>

    <DragResize :isActive="true"
                :isDraggable="true"
                :w="200"
                :h="200">
      <Vimg :item="VimgItem" />
    </DragResize>

    <DragResize :isActive="true"
                :isDraggable="true"
                :w="200"
                :h="200">
      <Doubler :item="doublerItem"></Doubler>
    </DragResize>

    <DragResize :isActive="true"
                :editable="true"
                :isDraggable="true"
                :w="300"
                :h="300">
      <Vchart :item="chartItem"></Vchart>
    </DragResize>

  </div>
</template>

<script>
import DragResize from './Common/DragResize'
import Vtextarea from './Common/Vtextarea'
import Vprogress from './Common/Vprogress'
import Vimg from './Common/Vimg'
import Doubler from './Common/Doubler'
import Border from './Common/Border'
import Vchart from './Common/Vchart'

export default {
  name: 'Home',
  components: { DragResize, Vtextarea, Vprogress, Vimg, Doubler, Border, Vchart },
  data () {
    return {
      data: '',
      single: {
        name: '繁忙度',
        unit: '%',
        value: 60
      },
      textItem: {
        width: 200,
        height: 50,
        bdpx: 0,
        bgClr: '',
        clr: '#666f8b',
        bdClr: '#3d445a',
        ctName: '请输入文本框内容'
      },
      VprogressItem: {
        width: 300,
        height: 50,
        bgClr: '#33394b',
        barClr: '#1fc3ce',
        clr: '#1fc3ce',
        chartData: {
          name: '繁忙度',
          unit: '%',
          value: 60
        }
      },
      doublerItem: {
        fontSize: 36,
        bgClr: '',
        bdClr: '#0c527c',
        clr: '#25aff8',
        width: 300,
        height: 90,
        chartData: {
          name: '繁忙度',
          unit: '%',
          value: 60
        }
      },
      VimgItem: {
        width: 300,
        height: 300,
        VimgSrc: '',
        chartData: {}
      },
      borderItem: {
        width: 300,
        height: 300,
        bdpx: 0,
        bgClr: 'rgba(17, 22, 35, 0.38)',
        bdClr: ''
      },
      chartItem: {
        id: new Date().getTime(),
        ctName: '饼图',
        ctLegendShow: 'true',
        x: 400,
        y: 100,
        width: 350,
        height: 350,
        colorType: 'defalut',
        ctColors: ['#37a2da', '#32c5e9', '#67e0e3', '#9fe6b8', '#ffdb5c', '#ff9f7f', '#fb7293', '#e062ae', '#e690d1', '#e7bcf3', '#9d96f5', '#8378ea', '#96bfff'],
        ctDataSource: 'static', // 数据来源system\static，默认static
        url: '', // 请求接口
        params: {}, // 请求接口参数
        bdpx: 1, // 边框线宽
        fontSize: 12, // 字号
        lineArea: false, // 折线是否为区域
        imgClass: 'icon-n-pie',
        chartType: 've-pie',
        roseType: 'area', // 与饼图的区别
        slted: true,
        chartData: {
          'columns': [
            '告警级别',
            '数量'
          ],
          'rows': [
            {
              '告警级别': '致命',
              '数量': 233
            },
            {
              '告警级别': '严重',
              '数量': 123
            },
            {
              '告警级别': '警告',
              '数量': 23
            },
            {
              '告警级别': '一般',
              '数量': 155
            },
            {
              '告警级别': '次要',
              '数量': 103
            },
            {
              '告警级别': '通知',
              '数量': 123
            }
          ]
        }
      }
    }
  },
  mounted: function () {
    // 获取大屏内容
    this.axios.get('home/homePage/noConf').then((res) => {
      console.log(res)
    })
  }
}
</script>

<style scoped>
.wrap {
  width: 100%;
  height: 100%;
  background-color: #222739;
}
</style>
