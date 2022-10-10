import hotspot from '@/components/EditComp/hotspot/config.js'
import ExternalJump from '@/components/EditComp/ExternalJump/config.js'
import IframePop from '@/components/EditComp/IframePop/config.js'
import OrderMenu from '@/components/EditComp/OrderMenu/config.js'
import SmallOrderMenu from '@/components/EditComp/SmallOrderMenu/config.js'
import CityEvent from '@/components/EditComp/CityEvent/config.js'
import StreetMenu from '@/components/EditComp/StreetMenu/config.js'
import WordClouds from '@/components/EditComp/WordClouds/config.js'
import decorator from '@/components/EditComp/decorator/config.js'
import NewBorder from '@/components/EditComp/NewBorder/config.js'
import TDModel from '@/components/EditComp/TDModel/config.js'
import VmVareTopo from '@/components/EditComp/VmVareTopo/config.js'
import XiaLaShu from '@/components/EditComp/XiaLaShu/config.js'
import WuhoMaoBL from '@/components/EditComp/WuhoMaoBL/config.js'
import WuhoOpenBox from '@/components/EditComp/WuhoOpenBox/config.js'
import WuhoPointBox from '@/components/EditComp/WuhoPointBox/config.js'
import WuhoYXHL from '@/components/EditComp/WuhoYXHL/config.js'
import CYZBTX from '@/components/EditComp/CYZBTX/config.js'
import WuHolunboTab from '@/components/EditComp/WuHolunboTab/config.js'
import WoHoNumber from '@/components/EditComp/WoHoNumber/config.js'

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
    ExternalJump.item,
    IframePop.item,
    OrderMenu.item,
    SmallOrderMenu.item,
    CityEvent.item,
    StreetMenu.item,
    WordClouds.item,
    decorator.item,
    TDModel.item,
    VmVareTopo.item,
    XiaLaShu.item,
    WoHoNumber.item,
    WuhoOpenBox.item,
    WuhoPointBox.item,
    WuhoYXHL.item,
    CYZBTX.item,
    WuHolunboTab.item,
    WuhoMaoBL.item
  ]
}

export default others
