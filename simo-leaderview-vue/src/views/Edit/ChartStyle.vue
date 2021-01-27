<template>
    <div>
        <section v-for="(section, sectionIndex) in ['lengend', 'base', 'axis']" :key="`section${sectionIndex}`">
              <div  v-if="section === 'axis' ? ['v-line', 've-histogram', 've-bar'].includes(curType) : true">
                <div class="m-gap form-group" v-show="configOptions[section] && configOptions[section].length > 0">{{sectionMap[section]}}</div>
                <div
                    class="form-group cols2"
                    v-for="(item, index) in configOptions[section]"
                    :key="`axis_${index}`"
                    v-show="judgeShowOption(section, item.key)"
                >
                    <label>{{ item.name }}</label>
                    <div class="color-w200" >
                        <template v-if="item.tag == 'select'">
                            <select :value="configItems[item.key]" @change="change(item.key, 'select', $event)">
                                <option
                                    v-for="(option, optIndex) in item.options"
                                    :key="`${option.name}_${optIndex}`"
                                    :value="option.value"
                                    >{{ option.name }}</option
                                >
                            </select>
                        </template>
                        <template v-else-if="item.tag == 'input'">
                            <input
                                class="w-90"
                                :type="item.type || text"
                                :value="configItems[item.key]"
                                @change="change(item.key, 'input', $event)"
                            />
                            {{ item.unit || "" }}
                        </template>
                        <template v-else-if="item.tag == 'singleColor'">
                            <Vcolor :data="configItems[item.key]"
                                :key="11"
                                type="bgClr"
                                @getdata="change(item.key, 'singleColor', $event)"></Vcolor>
                        </template>
                        <template v-else-if="item.tag == 'rangeColor'">
                            <RangeColor :data="configItems[item.key]" @getdata="change(item.key, 'rangeColor', $event)"></RangeColor>
                            <!-- :data="configItems[item.key]"
                                :key="12"
                                type="ctColors"
                                @getdata="change(item.key, 'singleColor', $event)" -->
                        </template>
                    </div>
                </div>
              </div>
        </section>
    </div>
</template>

<script>
// import { axis } from "@/components/EditComp/Vchart/config";

// console.log(axis);

import Vcolor from '@/components/Common/Vcolor'
import RangeColor from '@/components/Common/RangeColor'

export default {
  name: 'chartStyle',
  props: ['configItems'],
  components: {Vcolor, RangeColor},
  data () {
    return {
      sectionMap: {
        lengend: '图例配置',
        base: '图表样式',
        axis: '坐标轴样式'
      },
      configOptions: {}
      // lengend: [],
      // axis: axis,
      // chart: []
    }
  },
  computed: {
    curType () {
      return this.configItems.chartType
    },
    base () {
      return this.configItems.base || []
    },
    lengend () {
      return this.configItems.lengend || []
    },
    axis () {
      return this.configItems.axis || []
    }
  },
  mounted () {
    this.initOptions(this.curType)
  },
  methods: {
    initOptions (type) {
      if (type.indexOf('ve-') > -1) {
        this.configOptions = require(`@/components/EditComp/Vchart/config`).default.styles
      } else {
        this.configOptions = require(`@/components/EditComp/${type}/config`).default.styles
      }
    },
    change (key, tag, event) {
      // console.log(event.target.value);
      if (tag == 'singleColor') {
        const value = event.color
        this.$emit('change', key, value)
      } else if (tag == 'rangeColor') {
        const value = event.color
        this.$emit('change', key, value)
      } else {
        // select, input
        let value = event.target.value
        if (value === 'true' || value === 'false') {
          value = value === 'true'
        }
        console.log(key, value)
        this.$emit('change', key, value)
      }
    },
    judgeShowOption (section, key) {
      // console.log(section, key, this.configOptions[section][key]);
      var cur = this.configOptions[section].filter(d => d.key == key)[0]
      if (cur.hasOwnProperty('dep')) {
        // && this.configOptions[item.dep.targetKey] == item.dep.targetVal
        const {targetKey, targetVal} = cur.dep
        if (this.configItems[targetKey] != targetVal) {
          // console.log(key)
          return false
        }
      }
      return true
    }
  },
  watch: {
    // 'item.ctLegendShow' (newVal, oldVal) {
    //     this.$emit('change', 'ctLegendShow', newVal);
    // }
    'configItems.chartType' (newVal, oldVal) {
      this.initOptions(newVal)
      console.log('type', newVal, this.configOptions)
    }
  }
}
</script>

<style scoped></style>
