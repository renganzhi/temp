
import ppt from '@/components/EditComp/ppt/config.js'
import TextRotation from '@/components/EditComp/TextRotation/config.js'
import player from '@/components/EditComp/player/config.js'
import BulletFrame from '@/components/EditComp/BulletFrame/config.js'
import JSMpeg from '@/components/EditComp/JSMpeg/config.js'
import Newimage from '@/components/EditComp/Newimage/config.js'
// import config from './config.json'
// 改造过渡

const media = {
  name: '百分比图',
  child: [
    // {
    //   text: '图片',
    //   imgClass: 'icon-n-exportPicture',
    //   chartType: 'image',
    //   width: 300,
    //   height: 300,
    //   imgSrc: '',
    //   imgName: '',
    //   showType: '1',
    //   linkId: '',
    //   chartData: {}
    // },
    Newimage.item,
    ppt.item,
    TextRotation.item,
    player.item,
    BulletFrame.item,
    JSMpeg.item
    //   config.video.item
  ]
}

export default media
