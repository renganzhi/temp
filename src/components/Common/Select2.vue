<template>
  <select>
    <option v-if="!obj.notNull"
            value="">-不限-</option>
    <!-- <option value="">{{obj}}</option> -->
    <option v-for="(subv,index) in obj.data"
            :value="subv.value"
            :key="index">{{subv.name}}</option>
  </select>
</template>
<script>
export default {
  name: 'select2',
  props: ['obj', 'value'],
  data () {
    return {
      myData: {}
    }
  },
  computed: {
    tempArr: function () {
      return []
    }
  },
  mounted: function () {
    var vm = this
    this.init()
    $(this.$el).on('change', function () {
      // console.log('change select');
      vm.$emit('input', $(this).val())
    }).on('select2:selecting', function (e) {
      if (vm.obj.type === 'multi-select' && e.params && e.params.args && e.params.args.data) {
        var v = $(this).val()
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
      // console.log('optchange', this.obj.key, v)
      $(this.$el).select2({
        multiple: multi,
        closeOnSelect: !multi
      }).val(value).trigger('change')
    }
  },
  watch: {
    value: function (value, oldV) {
      if (value !== oldV) {
        $(this.$el).val(value).trigger('change.select2')
      }
    },
    'obj.data': function (newV) {
      var _this = this
      this.$nextTick(function () {
        _this.init((!_this.obj.notNull || !newV.length) ? null : newV[0].value)
        _this = null
      })
    }
  },
  destroyed: function () {
    $(this.$el).off().select2('destroy')
  }
}
</script>
