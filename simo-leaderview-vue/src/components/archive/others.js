import hotspot from '@/components/EditComp/hotspot/config.js'
import decorator from '@/components/EditComp/decorator/config.js'
import NewBorder from '@/components/EditComp/NewBorder/config.js'
import TDModel from '@/components/EditComp/TDModel/config.js'
import VmVareTopo from '@/components/EditComp/VmVareTopo/config.js'
import XiaLaShu from '@/components/EditComp/XiaLaShu/config.js'
import WuhoMaoBL from '@/components/EditComp/WuhoMaoBL/config.js'

const others = {
  name: '其他',
  child: [
    // {
    //   text: '边框',
    //   imgClass: 'icon-n-rect',
    //   chartType: 'border',
    //   borderType: 'simple', // 内置stable, 简单simple
    //   imgSrc: '',
    //   radius: 0,
    //   width: 300,
    //   height: 300,
    //   bdpx: 1,
    //   showType: '1',
    //   colorful: false,
    //   directionLinear: 180,
    //   bgClr: 'rgba(255, 255, 255, 0.02)',
    //   barClrs: ['rgba(255, 255, 255, 0.02)', 'rgba(255, 255, 255, 0.02)'],
    //   bdClr: '#175278'
    // },
    NewBorder.item,
    hotspot.item,
    decorator.item,
    TDModel.item,
    VmVareTopo.item,
    XiaLaShu.item,
    WuhoMaoBL.item
  ]
}

export default others
