<template>
  <div id="homeSetting-modal"
       class="modal">
    <div class="modal-dialog modal-lg"
         role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button"
                  class="close"
                  data-dismiss="modal"
                  aria-hidden="true">
            <span aria-hidden="true">&times;</span>
          </button>
          <h4 class="modal-title">
            设置
          </h4>
        </div>
        <div class="modal-body">
          <form autocomplete="off"
                class="row">
            <div class="form-group col-md-6">
              <label>轮播间隔</label>
              <select name="intervalTime">
                <option value="5">5s</option>
                <option value="10">10s</option>
                <option value="30">30s</option>
                <option value="60">1分钟</option>
              </select>
            </div>
            <div class="form-group col-md-6">
              <label class="showSet">切换效果</label>
              <select name="specialEffects">
                <option value="1">默认</option>
              </select>
            </div>

            <div class="form-group col-md-6">
              <label class="showSet">刷新周期</label>
              <div style="display: inline-block">
                <input name="refreshTime" /> 秒
              </div>
            </div>
          </form>
          <!-- 表格数据 -->
          <div style="height: 350px">
            <table class="table"
                   id="pictureSetTable"></table>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button"
                  data-pageSet="save">确定</button>
          <button type="button"
                  data-dismiss="modal">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
(function (win, $) {
  $.comps = $.comps || {}
  $.extend($.comps.homeCarouselConf || (($.comps.homeCarouselConf = {})), {
    tableConf: function () {
      var _this = this
      _this.tableData = _.orderBy(this.tableData, 'visible', 'desc')
      /* this.tableData.sort(function(a,b){
          return b.visible - a.visible;
        }) */
      return {
        autoHeight: true,
        pagination: false,
        data: this.tableData,
        columns: [
          {
            field: 'pageIndex',
            title: '所在页数',
            formatter: function (value, row, index) {
              return row.visible ? index + 1 : '--'
            }
          },
          {
            field: 'name',
            title: '页面名称'
          },
          {
            field: 'visible',
            title: '是否可见',
            formatter: function (value) {
              var className = value ? 'u-switch-on' : 'u-switch-off'
              return '<div class="u-switch ' + className + '" data-pageSet="switch"><div></div></div>'
            }
          },
          {
            field: 'lastUpdateTime',
            title: '最近一次改动',
            formatter: function (value) {
              return timestampformat(value)
            }
          },
          {
            title: '操作',
            width: 160,
            formatter: function (value, row) {
              var res = ''
              if (row.visible) {
                res = '<a data-pageSet="moveTop" class="simoLink">置顶</a>' +
                  '<a data-pageSet="moveUp"  class="simoLink">上移</a>' +
                  '<a data-pageSet="moveDown"  class="simoLink">下移</a>' +
                  '<a data-pageSet="moveBottom"  class="simoLink">置底</a>'
              }
              return res
            }
          }]
      }
    },
    getDom: function () {
      this.$modal = $('#homeSetting-modal')
      this.$fm = this.$modal.find('form')
      this.$table = this.$modal.find('#pictureSetTable')
    },
    open: function (param) {
      var _this = this
      if (!$('#homeSetting-modal').length) {
        $.get('/pages/home/comps/homeSetting.html', function (html) {
          $('body').append(html)
          _this.first()
          _this.init(param)
        })
      } else {
        _this.init(param)
      }
    },
    first: function () {
      this.getDom()
      this.bind()
      this.addValidator()
    },
    init: function (param) {
      this.params = param
      this.getConfData()
      modalShow(this.$modal)
    },
    getConfData: function () {
      var _this = this
      $.api.home.getPageConf({
        callback: function (res) {
          _this.tableData = res.pages
          res.intervalTime = res.intervalTime || '10'
          res.specialEffects = res.specialEffects || '1'
          res.refreshTime = res.refreshTime || 3
          _this.$fm.uxSetForm({
            data: res
          })
          _this.$table.initBootStrapTable(_this.tableConf())
        }
      })
      $.ajax({
        url: '/home/getCarouselTimeConf',
        type: 'get',
        async: false,
        success: function (data) {
          if (data.success) {
            var res = data.obj
            _this.tableData = res.pages
            res.intervalTime = res.intervalTime || '10'
            res.specialEffects = res.specialEffects || '1'
            res.refreshTime = res.refreshTime || 3
            _this.$fm.uxSetForm({
              data: res
            })
            _this.$table.initBootStrapTable(_this.tableConf())
            // (typeof param.callback === 'function') && param.callback(data.obj || [])
          } else {
            // tooltip('', data.msg, 'error')
          }
        }
      })
    },
    moveTop: function (ele, data, index) { // 置顶
      if (index === 0) {
        return
      }
      var obj = this.tableData.splice(index, 1)[0]
      this.tableData.unshift(obj)
      this.$table.initBootStrapTable(this.tableConf())
    },
    moveUp: function (ele, data, index) { // 上移
      if (index === 0) {
        return
      }
      this.tableData[index] = this.tableData.splice(index - 1, 1, this.tableData[index])[0]
      this.$table.initBootStrapTable(this.tableConf())
    },
    moveDown: function (ele, data, index) { // 下移
      if (index === this.tableData.length - 1) {
        return
      }
      this.tableData[index] = this.tableData.splice(index + 1, 1, this.tableData[index])[0]
      this.$table.initBootStrapTable(this.tableConf())
    },
    moveBottom: function (ele, data, index) { // 置底
      if (index === this.tableData.length - 1) {
        return
      }
      var obj = this.tableData.splice(index, 1)[0]
      this.tableData.push(obj)
      this.$table.initBootStrapTable(this.tableConf())
    },
    switch: function (ele, data) {
      $(ele).uxSwitch('toggle')
      data.visible = !data.visible
      this.$table.initBootStrapTable(this.tableConf())
    },
    save: function () {
      var _this = this
      if (!_this.validator.form()) {
        return
      }
      var cb = this.params.callback
      var data = this.$fm.serializeObject()
      var sortDatas = this.$table.bootstrapTable('getData')
      $.each(sortDatas, function (idx, itm) {
        itm.pageIndex = idx + 1
      })
      data.pages = JSON.stringify(sortDatas)
      $.api.home.setPageConf({
        data: data,
        callback: function () {
          typeof cb === 'function' && cb()
          _this.$modal.modal('hide')
          _this = null
          // tooltip('', '操作成功！', 'success')
        }
      })
    },
    addValidator: function () {
      this.validator = this.$fm.uxValidator({
        rules: {
          refreshTime: {
            required: true,
            min: 3,
            digits: true
          }
        }
      })
    },
    bind: function () {
      var _this = this
      this.$modal.off('click', '[data-pageSet]').on('click', '[data-pageSet]', function (e) {
        var type = $(this).attr('data-pageSet')
        var index = $(this).closest('tr').index()
        var data = _this.$table.bootstrapTable('getData')[index]
        typeof _this[type] === 'function' && _this[type](this, data, index)
        e.stopPropagation()
      })
    }
  })
}(window, $))

export default {
  name: 'settingPage',
  props: [],
  data () {
    return {
      tableData: []
    }
  },
  methods: {
    first: function () {
      // this.getDom()
      this.bind()
      this.addValidator()
    },
    init: function (param) {
      this.params = param
      this.getConfData()
      // modalShow(this.$modal)
      $('#homeSetting-modal').modal('show')
    },
    bind: function () {
      var _this = this
      $('#homeSetting-modal').off('click', '[data-pageSet]').on('click', '[data-pageSet]', function (e) {
        var type = $(this).attr('data-pageSet')
        var index = $(this).closest('tr').index()
        var data = _this.$table.bootstrapTable('getData')[index]
        typeof _this[type] === 'function' && _this[type](this, data, index)
        e.stopPropagation()
      })
    },
    getConfData: function () {
      var _this = this
      $.api.home.getPageConf({
        callback: function (res) {
          _this.tableData = res.pages
          res.intervalTime = res.intervalTime || '10'
          res.specialEffects = res.specialEffects || '1'
          res.refreshTime = res.refreshTime || 3
          _this.$fm.uxSetForm({
            data: res
          })
          _this.$table.initBootStrapTable(_this.tableConf())
        }
      })

      $.ajax({
        url: '/api/home/getCarouselTimeConf',
        type: 'get',
        async: false,
        success: function (data) {
          if (data.success) {
            var res = data.obj
            _this.tableData = res.pages
            res.intervalTime = res.intervalTime || '10'
            res.specialEffects = res.specialEffects || '1'
            res.refreshTime = res.refreshTime || 3
            _this.$fm.uxSetForm({
              data: res
            })
            _this.$table.initBootStrapTable(_this.tableConf())
            // (typeof param.callback === 'function') && param.callback(data.obj || [])
          } else {
            // tooltip('', data.msg, 'error')
          }
        }
      })
    }
  }
}
</script>
