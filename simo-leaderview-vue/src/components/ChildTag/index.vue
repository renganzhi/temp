<template>
    <div v-if="itemsShow" >
        <label>{{ item.name }}</label>
        <template v-if="item.tag == 'select'">
            <select v-model="selectedItem[item.key]">
                <option
                    v-for="(option, optIndex) in item.options"
                    :key="`${option.name}_${optIndex}`"
                    :value="option.value"
                    >{{ option.name }}
                    </option>
            </select>
        </template>
        <template v-if="item.tag == 'input'">
            <input  :type="item.type || 'number'"
                    v-model="selectedItem[item.key]">
        </template>
        <template v-if="item.tag == 'Color'">
            <div class="color-w200">
                <Vcolor :data="selectedItem[item.key]"
                        :key="10"
                        :type="[item.key]"
                        @getdata="ChildGetColor"></Vcolor>
            </div>
        </template>
        <template v-if="item.tag === 'GradientColor'">
            <div class="color-w200">
              <div class="gradient"
                    :style="{'background': 'linear-gradient(45deg, ' +  twoColor0  +',' + twoColor1 + ')'}">
                  <div class="color-w15">
                    <Vcolor :data="twoColor0"
                        :key="10"
                        :type="item.key"
                        :ColorNum="1"
                        @getdata="ChildGetColor"></Vcolor>
                  </div>
                  <div class="color-w15"
                      style="float: right">
                    <Vcolor :data="twoColor1"
                        :key="10"
                        :type="item.key"
                        :ColorNum="2"
                        @getdata="ChildGetColor"></Vcolor>
                  </div>
              </div>
            </div>
        </template>
        <template v-if="item.tag === 'ColorArray'">
            <div class="form-group colorsConf">
              <span>序号</span>
              <span class="color-w70 text">颜色</span>
            </div>
            <div class="form-group colorsConf"
                v-for="(v,index) in selectedItem.ctColors"
                :key="index">
            <span class="colorOrder">{{index+1}}</span>
            <div class="gradient"
                    v-if="selectedItem.ifGradual==='true'"
                    @click="myreverseColor(index)"
                    :style="{'background': 'linear-gradient(45deg, ' + selectedItem.ctColors[index][0]  +',' + selectedItem.ctColors[index][1] + ')'}">
                <div class="color-w15">
                <Vcolor :data="selectedItem.ctColors[index][0]"
                        :index="index"
                        @getdata="mygetColorStart"></Vcolor>
                </div>
                <div class="color-w15"
                    style="float: right">
                <Vcolor :data="selectedItem.ctColors[index][1]"
                        :index="index"
                        @getdata="mygetGradColor"></Vcolor>
                </div>
            </div>
            <div v-else>
                <div class="color-w200"
                    style="float: left; width: 140px;">
                <Vcolor :data="selectedItem.ctColors[index]"
                        type="ctColors"
                        :index="index"
                        @getdata="mygetSingleColor"></Vcolor>
                </div>
            </div>
            <i class="icon-n-add"
                @click="addColor(index + 1)"></i>
            <i class="icon-n-toUp"
                @click="moveUp(index)"></i>
            <i class="icon-n-putin"
                @click="moveDown(index)"></i>
            <i class="icon-n-deleteNew"
                @click="delColor(index)"></i>
            </div>
        </template>
    </div>
</template>
<script>
import Vcolor from '@/components/Common/Vcolor'
export default {
  name: 'ChildTag',
  components: {Vcolor},
  props: ['item', 'selectedItem'],
  computed: {
    itemsShow: function () {
      let canShow = true
      for (const key in this.item.parentKey) {
        if (this.selectedItem[key] !== this.item.parentKey[key]) {
          canShow = false
        }
      }
      return canShow
    },
    twoColor0: function () {
      if (this.item.tag === 'GradientColor') {
        return this.selectedItem[this.item.key][0]
      }
    },
    twoColor1: function () {
      if (this.item.tag === 'GradientColor') {
        return this.selectedItem[this.item.key][1]
      }
    }
  },
  methods: {
    ChildGetColor (data) {
      this.$parent.getColor(data)
    },
    mygetSingleColor (data) {
      this.$parent.getSingleColor(data)
    },
    addColor (data) {
      this.$parent.addColor(data)
    },
    moveUp (data) {
      this.$parent.moveUp(data)
    },
    mygetColorStart (data) {
      this.$parent.getColorStart(data)
    },
    myreverseColor (data) {
      this.$parent.reverseColor(data)
    },
    mygetGradColor (data) {
      this.$parent.getGradColor(data)
    },
    moveDown (data) {
      this.$parent.moveDown(data)
    },
    delColor (data) {
      this.$parent.delColor(data)
    }
  }
}
</script>
