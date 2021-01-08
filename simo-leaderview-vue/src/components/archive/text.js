import {morenData} from '@/views/Edit/chartJson'

import NEWtextArea from '@/components/EditComp/NEWtextArea/config.js'
import pyramid from '@/components/EditComp/pyramid/config.js'
import bubble from '@/components/EditComp/bubble/config.js'


const text = {
    name: '文本',
    child: [
        NEWtextArea.item,
        {
            text: '跑马灯',
            imgClass: 'icon-n-marquee',
            chartType: 'marquee',
            width: 200,
            height: 50,
            bdpx: 0,
            bgClr: '',
            direction: 'left',
            speed: 2,
            clr: '#666f8b',
            bdClr: '',
            ctName: '这是一个跑马灯的演示demo，双击可对文案进行编辑'
          },
          {
            text: '数字翻牌器',
            imgClass: 'icon-n-doubler',
            chartType: 'doubler',
            fontSize: 36,
            bgClr: '#222739',
            bdClr: '', // #0c527c
            clr: '#0088cc',
            width: 300,
            height: 90,
            ctLegendShow: 'true',
            chartData: morenData.single
          },
          {
            text: '时间器',
            imgClass: 'icon-n-time',
            chartType: 'time',
            timeSource: 'local',
            width: 200,
            height: 50,
            fontSize: 18,
            clr: '#666f8b',
            timeFrom: 'local',
            timeType: '1'
          },
          {
            text: '指标展示',
            imgClass: 'icon-n-number',
            chartType: 'number',
            fontSize: 36,
            fontFamily: 'number1',
            clr: '#25aff8',
            width: 300,
            height: 90,
            ctLegendShow: 'true',
            chartData: morenData.single
          },
          pyramid.item,
          bubble.item,
    ]
}

export default text