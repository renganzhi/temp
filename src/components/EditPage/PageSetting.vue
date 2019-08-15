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
              <select name="intervalTime"
                      v-model="intervalTime">
                <option value="5">5s</option>
                <option value="10">10s</option>
                <option value="30">30s</option>
                <option value="60">1分钟</option>
              </select>
            </div>
            <div class="form-group col-md-6">
              <label class="showSet">切换效果</label>
              <select name="specialEffects"
                      v-model="specialEffects">
                <option value="1">默认</option>
              </select>
            </div>
            <div class="form-group col-md-6">
              <label class="showSet">刷新周期</label>
              <div style="display: inline-block">
                <input name="refreshTime"
                       v-model="refreshTime" /> 秒
              </div>
            </div>
          </form>
          <!-- 表格数据 -->
          <div class="bootstrap-table home-table"
               :style="boxStyle">
            <div class="fixed-table-header"
                 style="height: 36px;">
              <table class="table table-hover"
                     style="table-layout: fixed;">
                <thead>
                  <tr :style="[trStyle,theadTrStyle]">
                    <th>所在页数</th>
                    <th>页面名称</th>
                    <th>是否可见</th>
                    <th>最近一次改动</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr :style="trStyle"
                      v-for="(tr, index) in tableData"
                      :key="index">
                    <td>{{tr.visible ? index + 1 : '--'}}</td>
                    <td>{{tr.name}}</td>
                    <td>
                      <div :class="tr.visible ? 'u-switch u-switch-on' : 'u-switch u-switch-off'">
                        <div></div>
                      </div>
                    </td>
                    <td>{{tr.lastUpdateTime}}</td>
                    <td>
                      <a class="simoLink">置顶</a><a class="simoLink">上移</a><a class="simoLink">下移</a><a class="simoLink">置底</a>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="fixed-table-body"
                 style="padding-bottom: 26px;">
              <table class="table table-hover"
                     style="table-layout: fixed;">
                <tbody>
                  <tr :style="trStyle"
                      v-for="(tr, index) in tableData"
                      :key="index">
                    <td>{{tr.visible ? index + 1 : '--'}}</td>
                    <td>{{tr.name}}</td>
                    <td @click="changeVisiable(index, tr.visible)">
                      <div :class="tr.visible ? 'u-switch u-switch-on' : 'u-switch u-switch-off'">
                        <div></div>
                      </div>
                    </td>
                    <td>{{timeStamp2String(tr.lastUpdateTime)}}</td>
                    <td>
                      <span v-show="tr.visible"><a class="simoLink"
                           @click="moveTop(index)">置顶</a><a class="simoLink"
                           @click="moveUp(index)">上移</a><a class="simoLink"
                           @click="moveDown(index)">下移</a><a class="simoLink"
                           @click="moveBottom(index)">置底</a></span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <!--  <div style="height: 350px">
            <table class="table"
                   id="pictureSetTable"></table>
          </div> -->
        </div>
        <div class="modal-footer">
          <button type="button"
                  @click="save">确定</button>
          <button type="button"
                  data-dismiss="modal">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import qs from 'qs'
export default {
  name: 'pageSetting',
  props: ['showModal'],
  data () {
    return {
      changeSort: false,
      intervalTime: '10',
      specialEffects: '1',
      refreshTime: 30,
      tableData: []
    }
  },
  computed: {
    boxStyle: function () {
      return {
        width: '100% !important',
        height: '350px !important',
        tableLayout: 'fixed',
        overflow: 'hidden'
      }
    },
    trStyle: function () {
      return {
        color: '#f1f1f1 !important'
      }
    },
    theadTrStyle: function () {
      return {
        // backgroundColor: this.item.hdBgClr+ ' !important', // 表头背景色
      }
    }
  },
  mounted: function () {
    var _this = this
    if (this.showModal) {
      $('#homeSetting-modal').modal('show')
    }
    this.getTableData()
    $('#homeSetting-modal').on('hide.bs.modal', function () {
      // 关闭模态框时触发
      _this.$emit('hideModal', { ifSort: _this.changeSort })
      _this.changeSort = false
    })
  },
  methods: {
    getTableData () {
      // 获取表格数据
      this.axios.get('/home/getCarouselTimeConf').then((data) => {
        if (data.success) {
          var res = data.obj
          this.intervalTime = res.intervalTime || '10'
          this.specialEffects = res.specialEffects || '1'
          this.refreshTime = res.refreshTime || 3
          var datas = res.pages.sort(this.visibleSort)
          this.tableData = datas
        } else {
          // tooltip("", data.msg, "error");
        }
      })
    },
    visibleSort (a, b) {
      return Number(b.visible) - Number(a.visible)
    },
    // 格式化data时间
    timeStamp2String (time) {
      var datetime = new Date(time)
      var year = datetime.getFullYear()
      var month = datetime.getMonth() + 1 < 10 ? '0' + (datetime.getMonth() + 1) : datetime.getMonth() + 1
      var date = datetime.getDate() < 10 ? '0' + datetime.getDate() : datetime.getDate()
      var hour = datetime.getHours() < 10 ? '0' + datetime.getHours() : datetime.getHours()
      var minute = datetime.getMinutes() < 10 ? '0' + datetime.getMinutes() : datetime.getMinutes()
      var second = datetime.getSeconds() < 10 ? '0' + datetime.getSeconds() : datetime.getSeconds()
      return year + '-' + month + '-' + date + ' ' + hour + ':' + minute + ':' + second
    },
    changeVisiable (index, val) {
      this.tableData[index].visible = !val
      var temp = this.tableData.splice(index, 1)[0]
      if (val) {
        // 隐藏
        for (var i = index, len = this.tableData.length; i < len; i++) {
          if (!this.tableData[i].visible) {
            return this.tableData.splice(i, 0, temp)
          }
        }
        this.tableData.push(temp)
      } else {
        // 展示
        if (index === 0) {
          return this.tableData.unshift(temp)
        }
        for (var j = index - 1; j >= 0; j--) {
          if (this.tableData[j].visible) {
            return this.tableData.splice(j + 1, 0, temp)
          }
        }
        this.tableData.unshift(temp)
      }
    },
    moveTop: function (index) { // 置顶
      if (index === 0) {
        return
      }
      var temp = this.tableData.splice(index, 1)[0]
      this.tableData.unshift(temp)
    },
    moveUp: function (index) { // 上移
      if (index === 0) {
        return
      }
      this.tableData[index] = this.tableData.splice(index - 1, 1, this.tableData[index])[0]
    },
    moveDown: function (index) { // 下移
      if (index === this.tableData.length - 1) {
        return
      }
      this.tableData[index] = this.tableData.splice(index + 1, 1, this.tableData[index])[0]
    },
    moveBottom: function (index) { // 置底
      if (index === this.tableData.length - 1 || !this.tableData[index + 1].visible) {
        return
      }
      for (var i = index + 1, len = this.tableData.length; i < len; i++) {
        if (!this.tableData[i].visible) {
          var temp = this.tableData.splice(index, 1)[0]
          return this.tableData.splice(i - 1, 0, temp)
        }
      }
      temp = this.tableData.splice(index, 1)[0]
      this.tableData.push(temp)
    },
    save: function () {
      this.tableData.forEach((item, index) => {
        this.tableData[index].pageIndex = index + 1
      })
      var datas = {
        intervalTime: this.intervalTime,
        specialEffects: this.specialEffects,
        refreshTime: this.refreshTime,
        pages: JSON.stringify(this.tableData)
      }
      this.axios({
        method: 'post',
        url: '/home/carousel/conf',
        data: qs.stringify(datas),
        headers: { 'content-type': 'application/x-www-form-urlencoded' }
      }).then((res) => {
        if (res.success) {
          this.changeSort = true
          $('#homeSetting-modal').modal('hide')
        } else {
          // tooltip('', res.msg, 'error')
        }
      })
    }
  },
  watch: {
    showModal: function (newV) {
      if (newV) {
        this.getTableData()
        $('#homeSetting-modal').modal('show')
      }
    }
  }
}
</script>
