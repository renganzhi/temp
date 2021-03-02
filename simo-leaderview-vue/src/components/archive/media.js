import {morenData} from '@/views/Edit/chartJson'
import ppt from '@/components/EditComp/ppt/config.js'
import player from '@/components/EditComp/player/config.js'
import BulletFrame from '@/components/EditComp/BulletFrame/config.js'
// import config from './config.json'
// 改造过渡

const media = {
  name: '百分比图',
  child: [
    {
      text: '图片',
      imgClass: 'icon-n-exportPicture',
      chartType: 'image',
      width: 300,
      height: 300,
      imgSrc: '',
      imgName: '',
      showType: '1',
      linkId: '',
      chartData: {}
    },
    ppt.item,
    player.item,
    BulletFrame.item
    //   config.video.item
  ]
}

export default media
