import hotspot from '@/components/EditComp/hotspot/config.js'
import decorator from '@/components/EditComp/decorator/config.js'


const others = {
    name: '其他',
    child: [
        {
            text: '边框',
            imgClass: 'icon-n-rect',
            chartType: 'border',
            borderType: 'simple', // 内置stable, 简单simple
            imgSrc: '',
            radius: 0,
            width: 300,
            height: 300,
            bdpx: 1,
            showType: '1',
            colorful: false,
            bgClr: 'rgba(255, 255, 255, 0.02)',
            barClrs: ['rgba(255, 255, 255, 0.02)', 'rgba(255, 255, 255, 0.02)'],
            bdClr: '#175278'
          },
          hotspot.item,
          decorator.item,

    ]
}

export default others