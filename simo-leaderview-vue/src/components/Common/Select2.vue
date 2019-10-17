<template>
  <select v-if="mapSelect && sameName">
    <!-- :disabled="disData.indexOf(subv.name) === -1" -->
    <option v-for="(subv,index) in obj"
            :value="subv.name"
            :disabled="disData.indexOf(subv.name) !== -1"
            :key="index">{{subv.name}}</option>
  </select>
  <select v-else-if="mapSelect && !sameName">
    <option v-for="(subv,index) in obj"
            :value="subv.value"
            :key="index">{{subv.name}}</option>
  </select>
  <select v-else>
    <option v-if="!obj.notNull"
            value="">-不限-</option>
    <option v-for="(subv,index) in obj.data"
            :value="subv.value"
            :key="index">{{subv.name}}</option>
  </select>
</template>
<script>
export default {
  name: 'select2',
  props: ['obj', 'value', 'mapSelect', 'sameName', 'disData'], // disData 不可被选中的数据项
  data () {
    return {
      myData: {}
    }
  },
  computed: {
    disAbleData: function () {
      return this.disData
    },
    tempArr: function () {
      return []
    }
  },
  mounted: function () {
    // console.log(this.disData)
    var vm = this
    this.init()
    $(this.$el).on('change', function () {
      // console.log('change select')
      vm.$emit('input', $(this).val())
    }).on('select2:selecting', function (e) {
      if (vm.obj.type === 'multi-select' && e.params && e.params.args && e.params.args.data) {
        var v = $(this).val()
        // 这里设置最多可选项
        if (v && vm.obj.maxLength && (v.length > vm.obj.maxLength - 1)) {
          return e.preventDefault()
        }
        if (e.params.args.data.id === '') { // 选择不限
          $(this).val(null)
        } else { // 选中其他
          if (v && v.indexOf('') !== -1) {
            $(this).val([e.params.args.data.id])
          }
        }
      }
    }).on('select2:unselecting', function (e) {
      if (vm.obj.type === 'multi-select' && e.params && e.params.args && e.params.args.data) {
        var v = $(this).val()
        if (v && v.length === 1) {
          if (e.params.args.data.id === '') {
            e.preventDefault()
          }
          $(this).val(null)
        }
      }
    })
  },
  methods: {
    init: function (v) {
      var multi = this.obj.type === 'multi-select'
      var value = typeof v === 'undefined' ? this.value : v
      // var maxLen = this.obj.maxLength || -1
      // if (this.mapSelect && this.sameName && !value) {
      //   value = this.obj[0].name
      // }
      // console.log('select2 init: ' + value)
      $(this.$el).select2({
        multiple: multi,
        closeOnSelect: !multi
        // maximumSelectionLength: maxLen, // 该属性设置最多可选项有bug
      }).val(value).trigger('change')
    }
  },
  watch: {
    value: function (value, oldV) {
      if (value !== oldV) {
        $(this.$el).val(value).trigger('change.select2')
      }
      if (this.mapSelect && this.sameName && !value) {
        if (oldV) {
          // this.value = oldV
          // $(this.$el).val(oldV).trigger('change.select2')
        }
      }
    },
    // disData: function (oldV, newV) {
    //   // if (!_.isEqual(newV, oldV)) {
    //   //   // console.log(newV)
    //   // }
    // },
    'obj': function (newV) {
      if (this.mapSelect) {
        var _this = this
        this.$nextTick(function () {
          _this.init(_this.value ? _this.value : _this.sameName ? newV[0].name : newV[0].value)
          _this = null
        })
      }
    },
    'obj.data': function (newV) {
      if (!this.mapSelect) {
        var _this = this
        this.$nextTick(function () {
          _this.init((!_this.obj.notNull || !newV.length) ? null : newV[0].value)
          _this = null
        })
      }
    }
  },
  destroyed: function () {
    $(this.$el).off().select2('destroy')
    if ($(this.$el).find('.tooltip').length > 0) {
      $(this.$el).find('[title]').tooltip('destroy')
    }
  }
}
</script>
