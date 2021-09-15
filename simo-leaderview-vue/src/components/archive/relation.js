
import Sunrise from '@/components/EditComp/Sunrise/config.js'
import TreeMap from '@/components/EditComp/TreeMap/config.js'

const relation = {
  name: '关系图',
  child: [
    {
      text: '拓扑',
      imgClass: 'icon-n-topo',
      chartType: 'topo',
      width: 300,
      height: 300,
      tpId: '',
      cityColor: '',
      chartData: {}
    },
    Sunrise.item,
    TreeMap.item
  ]
}

export default relation
