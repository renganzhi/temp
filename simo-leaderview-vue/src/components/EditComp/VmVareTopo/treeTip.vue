<template>
  <div
    class="tp-tip"
    :style="style"
  >
    <p> 名称 : {{ tip.name }}</p>
    <template v-if="tip.type==='computeResource' || tip.type == 'hostSystems' || tip.type == 'virtualMachines'">
      <p v-if="tip.type!=='computeResource'">
        IP地址 : {{ tip.monitoring ? (tip.ip || tip.ipAddress || '--') : '--' }}
      </p>
      <p v-if="tip.type!=='computeResource'">
        电源状态 : {{ vmPowerStatus[tip.powerState || tip.power_state].text }}
      </p>
      <p> 状态 : {{ neStatus[tip.status].text }}</p>
      <p> CPU利用率 : {{ tip.monitoring ? addUnit(tip[isUndefined(tip.cpuUsage) ? (isUndefined( tip.cpu_usage) ? 'usedCPU':'cpu_usage'): 'cpuUsage'],'%') :'--' }}</p>
      <p> 内存利用率 : {{ tip.monitoring ? addUnit(tip[isUndefined(tip.memoryUsage) ? (isUndefined(tip.memory_usage) ? 'usedMem' : 'memory_usage') : 'memoryUsage'],'%') :'--' }}</p>
    </template>

    <template v-if="tip.type==='datastores'">
      <p> 状态 : {{ neStatus[tip.status].text }}</p>
      <p> 磁盘使用率 : {{ tip.monitoring ? addUnit(tip.diskUsage,'%') : '--' }}</p>
    </template>
  </div>
</template>
<script>
export default {
  name: 'TreeTip',
  props: {
    tip: {
      type: Object,
      default: () => {}
    },
    left: {
      type: Number,
      default: -99999
    },
    top: {
      type: Number,
      default: 0
    }
  },
  data () {
    return {
      neStatus:{
  Loading: {
    text: '检测中',
    icon: 'icon-n-loading',
    color: '#cad6dd'
  },
  Good: {
    text: '正常',
    icon: 'icon-n-good',
    color: '#26c281'
  },
  Warning: {
    text: '告警',
    icon: 'icon-n-alert',
    color: '#f3c200'
  },
  Unconnection: {
    text: '失联',
    icon: 'icon-n-unconnection',
    color: '#f32d5c'
  },
  Unknown: {
    text: '未监控',
    icon: 'icon-n-unknow',
    color: '#50607c'
  }
},
      vmPowerStatus:{
  poweredOn: {
    text: '打开',
    icon: 'icon-solid-circle',
    color: '#26c281'
  },
  poweredOff: {
    text: '关闭',
    icon: 'icon-solid-circle',
    color: '#f32d5c'
  },
  Unknown: {
    text: '未知',
    icon: 'icon-solid-circle'
  }
}
    }
  },
  computed: {
    style () {
      return {
        left: this.left + 'px',
        top: this.top + 'px'
      }
    }
  },
  methods: {
    isUndefined (d) {
      return typeof d === 'undefined'
    },
  addUnit (value, unit, index) { // 给对应的数据加单位
    if (typeof value === 'undefined' || value === 'undefined' || value === 'NaN' || value === 'null' || value === null || value==='' || value==='--') {
      return '--'
    }
    if ((typeof value) === 'number' || !!Number(value)) {
      if (value % 1) {
        value = parseFloat(value).toFixed(2)
        value = Number(value) ? value : 0
      }
    }
    if (!unit) {
      return value
    }
    switch (unit) {
      case 'bytesToSize':
        return this.bytesToSize(value, index)
      case 'duringTime': // 13天2时18分27秒
        return this.duringTime(value, index)
      default:
        if (value || value === 0) {
          unit = unit || ''
          return value + ' ' + unit
        } else {
          return '--'
        }
    }
  },
  }
}
</script>
